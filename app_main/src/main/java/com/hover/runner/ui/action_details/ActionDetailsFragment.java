package com.hover.runner.ui.action_details;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.hover.sdk.api.HoverParameters;
import com.hover.runner.MainActivity;
import com.hover.runner.R;
import com.hover.runner.adapters.TransactionRecyclerAdapter;
import com.hover.runner.adapters.VariableRecyclerAdapter;
import com.hover.runner.api.Apis;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.interfaces.CustomOnClickListener;
import com.hover.runner.interfaces.ParserClickListener;
import com.hover.runner.interfaces.VariableEditinterface;
import com.hover.runner.ui.parsers.ParsersActivity;
import com.hover.runner.ui.transactionDetails.TransactionDetailsActivity;
import com.hover.runner.ui.webview.WebViewActivity;
import com.hover.runner.utils.UIHelper;
import com.hover.runner.utils.Utils;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ActionDetailsFragment extends Fragment implements ParserClickListener, CustomOnClickListener, VariableEditinterface {

    private Timer timer= new Timer();
    private ActionDetailsViewModel actionDetailsViewModel;
    private static final int TEST_SINGLE = 305;
    private TextView toolText, subtoolText, descTitle,descContent,descLink,operatorsText,stepsText,parsersText,
            transacText,successText,pendingText,failureText, testSingleActiontext;
    private LinearLayout topLayout;
    private StatusEnums mostRecentStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.action_details_fragment, container, false);

        toolText = view.findViewById(R.id.actionDetails_toolbarText);
        subtoolText = view.findViewById(R.id.actionDetails_subtoolText);
        descTitle = view.findViewById(R.id.actionDetails_statusTitle);

        descContent = view.findViewById(R.id.actionDetails_statusDesc);
        descLink = view.findViewById(R.id.actionDetails_statusLink);
        topLayout = view.findViewById(R.id.actionDetailsTopLayoutId);

        operatorsText = view.findViewById(R.id.operators_content);
        stepsText = view.findViewById(R.id.steps_content);
        parsersText = view.findViewById(R.id.parsers_content);
        transacText = view.findViewById(R.id.transactionNo_content);
        successText = view.findViewById(R.id.successCount_content);
        pendingText = view.findViewById(R.id.pendingCount_content);
        failureText = view.findViewById(R.id.failedCount_content);
        testSingleActiontext = view.findViewById(R.id.testSingle_id);

        toolText.setText(ActionDetailsActivity.actionId);
        subtoolText.setText(ActionDetailsActivity.actionTitle);
        toolText.setOnClickListener(v -> {
            if(getActivity() !=null) getActivity().finish();
        });
        mostRecentStatus = ActionDetailsActivity.statusEnums;
        setupTopDetailsInfo(mostRecentStatus);

        RecyclerView variablesRecyclerView = view.findViewById(R.id.action_variables_recyclerView);
        actionDetailsViewModel = new ViewModelProvider(this).get(ActionDetailsViewModel.class);
        actionDetailsViewModel.loadActionDetailsObs().observe(getViewLifecycleOwner(), model-> {
            if(model !=null) {
                operatorsText.setText(model.getOperators());
                if(model.getStreamlinedStepsModel() !=null)
                    stepsText.setText(model.getStreamlinedStepsModel().getFullUSSDCodeStep());
                UIHelper.makeEachTextLinks(model.getParsers(), parsersText, this);
                transacText.setText(model.getTransactionsNo());
                successText.setText(model.getSuccessNo());
                pendingText.setText(model.getPendingNo());
                failureText.setText(model.getFailedNo());

                variablesRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(view.getContext()));
                variablesRecyclerView.setAdapter(new VariableRecyclerAdapter(
                        ActionDetailsActivity.actionId,
                        model.getStreamlinedStepsModel(),
                        this,
                        Utils.getInitialVariableData(getContext(), ActionDetailsActivity.actionId).second
                ));
            }
        });

        TextView recentTransText = view.findViewById(R.id.recentTransa_id);
        TextView viewAllText = view.findViewById(R.id.viewAll_id);
        viewAllText.setOnClickListener(v -> {
            if(getActivity() !=null) {
                Intent i = new Intent(getContext(), MainActivity.class);
                i.putExtra("navigate", ActionDetailsActivity.actionId);
                startActivity(i);
                getActivity().finishAffinity();
            }

        });
        RecyclerView transacRecyclerView = view.findViewById(R.id.action_transac_recyclerView);
        transacRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));

        actionDetailsViewModel.loadActionTransactionsObs().observe(getViewLifecycleOwner(), transactions-> {
            switch (transactions.getEnums()) {
                case LOADING: recentTransText.setText(getResources().getString(R.string.loadingText));
                    break;
                case EMPTY: recentTransText.setText(getResources().getString(R.string.zero_transactions));
                    break;
                case HAS_DATA:
                    recentTransText.setText(getResources().getString(R.string.recent_transactions));
                    viewAllText.setVisibility(View.VISIBLE);
                    if(transactions.getTransactionModelsList().size() > 0) {
                        if(mostRecentStatus != transactions.getTransactionModelsList().get(0).getStatusEnums()){
                            mostRecentStatus = transactions.getTransactionModelsList().get(0).getStatusEnums();
                            setupTopDetailsInfo(mostRecentStatus);
                        }
                    }
                    transacRecyclerView.setAdapter(new TransactionRecyclerAdapter(transactions.getTransactionModelsList(),
                            this,
                            getResources().getColor(R.color.colorYellow),
                            getResources().getColor(R.color.colorRed),
                            getResources().getColor(R.color.colorGreen)));
                    break;
            }
        });

        testSingleActiontext.setOnClickListener(v->{

            Map<String, String> actionExtra = Utils.getInitialVariableData(getContext(),  ActionDetailsActivity.actionId).second;

            HoverParameters.Builder builder = new HoverParameters.Builder(getContext());
            builder.request(ActionDetailsActivity.actionId);
            builder.setEnvironment(Apis.getTestEnvMode());

            assert  actionExtra !=null;
            boolean hasValidVariables = true;
            for(String key : actionExtra.keySet()) {
                if(key == null || key.replace(" ","").isEmpty()) {
                    hasValidVariables = false;
                    break;
                }
                else builder.extra(key, actionExtra.get(key));
            }

            if(hasValidVariables) {
                try{
                    Intent i = builder.buildIntent();
                    startActivityForResult(i, TEST_SINGLE);
                }catch (Exception e){
                    UIHelper.showHoverToastV2(getContext(), getResources().getString(R.string.one_or_more_empty_variable));
                }
            }
            else {
                UIHelper.showHoverToastV2(getContext(), getResources().getString(R.string.one_or_more_empty_variable));
            }
        });

        return  view;
    }


    private void setupTopDetailsInfo(StatusEnums statusEnums) {
        switch (statusEnums) {
            case PENDING:
                UIHelper.changeStatusBarColor(getActivity(), getResources().getColor(R.color.colorYellow));
                topLayout.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                descContent.setVisibility(View.VISIBLE);
                descLink.setVisibility(View.VISIBLE);

                descTitle.setText(getResources().getString(R.string.pendingStatus_title));
                descTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_black_24dp, 0,0,0);
                descTitle.setCompoundDrawablePadding(32);
                descContent.setText(getResources().getString(R.string.pendingStatus_desc));
                UIHelper.setTextUnderline(descLink,getResources().getString(R.string.pendingStatus_linkText));
                descLink.setOnClickListener(v -> {
                    Intent i = new Intent(getActivity(), WebViewActivity.class);
                    i.putExtra(WebViewActivity.TITLE, "Pending transaction");
                    i.putExtra(WebViewActivity.URL, getResources().getString(R.string.pendingStatus_url));
                    startActivity(i);
                });
                break;
            case UNSUCCESSFUL:
                UIHelper.changeStatusBarColor(getActivity(), getResources().getColor(R.color.colorRed));
                topLayout.setBackgroundColor(getResources().getColor(R.color.colorRed));
                descContent.setVisibility(View.VISIBLE);
                descLink.setVisibility(View.VISIBLE);

                descTitle.setText(getResources().getString(R.string.failedStatus_title));
                descTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_black_24dp, 0,0,0);
                descTitle.setCompoundDrawablePadding(32);
                descContent.setText(getResources().getString(R.string.failedStatus_desc));
                UIHelper.setTextUnderline(descLink,getResources().getString(R.string.failedStatus_linkText));
                descLink.setOnClickListener(v -> {
                    Intent i = new Intent(getActivity(), WebViewActivity.class);
                    i.putExtra(WebViewActivity.TITLE, "Failed transaction");
                    i.putExtra(WebViewActivity.URL, getResources().getString(R.string.failedStatus_url));
                    startActivity(i);
                });

                break;
            case SUCCESS:
                UIHelper.changeStatusBarColor(getActivity(), getResources().getColor(R.color.colorGreen));
                topLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                descContent.setVisibility(View.VISIBLE);
                descLink.setVisibility(View.VISIBLE);

                descTitle.setText(getResources().getString(R.string.successStatus_title));
                descTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0,0,0);
                descTitle.setCompoundDrawablePadding(32);
                descContent.setVisibility(View.GONE);
                descLink.setVisibility(View.GONE);

                break;
            default:
                toolText.setTextColor(Color.WHITE);
                toolText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_back_white_24dp, 0,0,0);
                subtoolText.setTextColor(Color.WHITE);
                topLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                descTitle.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        actionDetailsViewModel.getDetails(ActionDetailsActivity.actionId);
        actionDetailsViewModel.getActionTrans(ActionDetailsActivity.actionId);

    }

    @Override
    public void customClickListener(Object... data) {
        Intent i = new Intent(getActivity(), TransactionDetailsActivity.class);
        i.putExtra(Apis.TRANS_ID, (String) data[0]);
        i.putExtra(Apis.TRANS_DATE, (String) data[1]);
        i.putExtra(Apis.TRANS_STATUS, (StatusEnums) data[2]);
        startActivity(i);
    }

    @Override
    public void onClickParser(String str) {
        Intent intent = new Intent(getActivity(), ParsersActivity.class);
        intent.putExtra(ParsersActivity.PARSER_EXTRA, str);
        startActivity(intent);
    }

    @Override
    public void onEditStringChanged(String label, String newValue) {
        timer.cancel();
        timer = new Timer();long DELAY = 800;
        timer.schedule(new TimerTask() {@Override public void run() {
            if (getContext() !=null) {
                Utils.saveActionVariable(getContext(), label, newValue, ActionDetailsActivity.actionId);
            }
        }}, DELAY);
    }
}