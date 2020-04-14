package com.usehover.testerv2.ui.action_details;


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

import com.usehover.testerv2.MainActivity;
import com.usehover.testerv2.R;
import com.usehover.testerv2.adapters.HoverAdapters;
import com.usehover.testerv2.adapters.VariableAdapter;
import com.usehover.testerv2.adapters.ViewsRelated;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.interfaces.ParserClickListener;
import com.usehover.testerv2.interfaces.VariableEditinterface;
import com.usehover.testerv2.ui.parsers.ParsersActivity;
import com.usehover.testerv2.ui.webview.WebViewActivity;
import com.usehover.testerv2.utils.UIHelper;
import com.usehover.testerv2.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class ActionDetailsFragment extends Fragment implements ParserClickListener, CustomOnClickListener, VariableEditinterface {

    private Timer timer= new Timer();
    private ActionDetailsViewModel actionDetailsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.action_details_fragment, container, false);

        TextView toolText = view.findViewById(R.id.actionDetails_toolbarText);
        TextView subtoolText = view.findViewById(R.id.actionDetails_subtoolText);
        TextView descTitle = view.findViewById(R.id.actionDetails_statusTitle);

        TextView descContent = view.findViewById(R.id.actionDetails_statusDesc);
        TextView descLink = view.findViewById(R.id.actionDetails_statusLink);
        LinearLayout topLayout = view.findViewById(R.id.actionDetailsTopLayoutId);

        TextView operatorsText = view.findViewById(R.id.operators_content);
        TextView stepsText = view.findViewById(R.id.steps_content);
        TextView parsersText = view.findViewById(R.id.parsers_content);
        TextView transacText = view.findViewById(R.id.transactionNo_content);
        TextView successText = view.findViewById(R.id.successCount_content);
        TextView pendingText = view.findViewById(R.id.pendingCount_content);
        TextView failureText = view.findViewById(R.id.failedCount_content);

        toolText.setText(ActionDetailsActivity.actionId);
        subtoolText.setText(ActionDetailsActivity.actionTitle);
        toolText.setOnClickListener(v -> {
            if(getActivity() !=null) getActivity().finish();
        });

        switch (ActionDetailsActivity.statusEnums) {
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
                    i.putExtra(WebViewActivity.TITLE, "Pending Transaction");
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
                    i.putExtra(WebViewActivity.TITLE, "Failed Transaction");
                    i.putExtra(WebViewActivity.URL, getResources().getString(R.string.failedStatus_url));
                    startActivity(i);
                });

                break;
            case SUCCESS:
                UIHelper.changeStatusBarColor(getActivity(), getResources().getColor(R.color.colorGreen));
                topLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                descContent.setVisibility(View.VISIBLE);
                descLink.setVisibility(View.VISIBLE);

                descTitle.setText(getResources().getString(R.string.pendingStatus_title));
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

        RecyclerView variablesRecyclerView = view.findViewById(R.id.action_variables_recyclerView);
        actionDetailsViewModel = new ViewModelProvider(this).get(ActionDetailsViewModel.class);
        actionDetailsViewModel.loadActionDetailsObs().observe(getViewLifecycleOwner(), model-> {
            if(model !=null) {
                operatorsText.setText(model.getOperators());
                if(model.getStreamlinedStepsModel() !=null) stepsText.setText(model.getStreamlinedStepsModel().getFullUSSDCodeStep());
                UIHelper.makeEachTextLinks(model.getParsers(), parsersText, this);
                transacText.setText(model.getTransactionsNo());
                successText.setText(model.getSuccessNo());
                pendingText.setText(model.getPendingNo());
                failureText.setText(model.getFailedNo());

                variablesRecyclerView.setLayoutManager(ViewsRelated.setMainLinearManagers(view.getContext()));
                variablesRecyclerView.setAdapter(new VariableAdapter(
                        ActionDetailsActivity.actionId,
                        model.getStreamlinedStepsModel(),
                        this,
                        Utils.getInitialVariableData(getContext(), ActionDetailsActivity.actionId)
                ));
            }
        });

        TextView recentTransText = view.findViewById(R.id.recentTransa_id);
        TextView viewAllText = view.findViewById(R.id.viewAll_id);
        viewAllText.setOnClickListener(v -> {
            if(getActivity() !=null) {
                Intent i = new Intent(getContext(), MainActivity.class);
                i.putExtra("navigate", true);
                startActivity(i);
                getActivity().finishAffinity();
            }

        });
        RecyclerView transacRecyclerView = view.findViewById(R.id.action_transac_recyclerView);
        transacRecyclerView.setLayoutManager(ViewsRelated.setMainLinearManagers(getContext()));

        actionDetailsViewModel.loadActionTransactionsObs().observe(getViewLifecycleOwner(), transactions-> {
            switch (transactions.getEnums()) {
                case LOADING: recentTransText.setText(getResources().getString(R.string.loadingText));
                    break;
                case EMPTY: recentTransText.setText(getResources().getString(R.string.zero_transactions));
                    break;
                case HAS_DATA:
                    recentTransText.setText(getResources().getString(R.string.recent_transactions));
                    viewAllText.setVisibility(View.VISIBLE);
                    transacRecyclerView.setAdapter(new HoverAdapters.TransactionRecyclerAdapter(transactions.getTransactionModelsList(),
                            this,
                            getResources().getColor(R.color.colorYellow),
                            getResources().getColor(R.color.colorRed),
                            getResources().getColor(R.color.colorGreen)));
                    break;
            }
        });


        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        actionDetailsViewModel.getDetails(ActionDetailsActivity.actionId);
        actionDetailsViewModel.getActionTrans(ActionDetailsActivity.actionId);

    }

    @Override
    public void customClickListener(Object... data) {

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
        timer = new Timer();long DELAY = 2000;
        timer.schedule(new TimerTask() {@Override public void run() {
            if(getContext() !=null) Utils.saveActionVariable(getContext(), label, newValue); }}, DELAY);
    }
}
