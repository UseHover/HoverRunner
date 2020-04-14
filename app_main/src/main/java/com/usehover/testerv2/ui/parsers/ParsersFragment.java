package com.usehover.testerv2.ui.parsers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.adapters.HoverAdapters;
import com.usehover.testerv2.adapters.ViewsRelated;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.ui.action_details.ActionDetailsActivity;
import com.usehover.testerv2.utils.UIHelper;


public class ParsersFragment extends Fragment implements CustomOnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parsers_fragment, container, false);

        TextView typeText = view.findViewById(R.id.parser_type_content);
        TextView actionText = view.findViewById(R.id.parser_action_content);
        TextView actionIdText = view.findViewById(R.id.parser_actionId_content);
        TextView categoryText = view.findViewById(R.id.parser_category_content);
        TextView statusText = view.findViewById(R.id.parser_status_content);
        TextView createdText = view.findViewById(R.id.parser_createdAt_content);
        TextView senderText = view.findViewById(R.id.parser_sender_content);
        TextView regexText = view.findViewById(R.id.parser_regex_content);
        TextView toolText = view.findViewById(R.id.parserDetails_toolbarText);

        toolText.setText(ParsersActivity.parserId);
        toolText.setOnClickListener(v -> {
            if(getActivity() !=null) getActivity().finish();
        });



        ParsersViewModel parsersViewModel = new ViewModelProvider(this).get(ParsersViewModel.class);
        parsersViewModel.loadParserInfoObs().observe(getViewLifecycleOwner(), parsersInfoModel -> {
            if(parsersInfoModel !=null) {
                typeText.setText(parsersInfoModel.getParser_type());
                UIHelper.setTextUnderline(actionText, parsersInfoModel.getParser_action());
                UIHelper.setTextUnderline(actionIdText,parsersInfoModel.getParser_actionID());
                categoryText.setText(parsersInfoModel.getParser_category());
                createdText.setText(parsersInfoModel.getParser_created());
                senderText.setText(parsersInfoModel.getParser_sender());
                regexText.setText(parsersInfoModel.getParser_regex());

                switch (parsersInfoModel.getStatusEnums()) {
                    case NOT_YET_RUN:
                        statusText.setText(getResources().getString(R.string.not_yet_run));
                        break;
                    case UNSUCCESSFUL:
                        statusText.setText(getResources().getString(R.string.failed_label));
                        statusText.setTextColor(getResources().getColor(R.color.colorRed));
                        break;
                    case PENDING:
                        statusText.setText(getResources().getString(R.string.pending_label));
                        statusText.setTextColor(getResources().getColor(R.color.colorYellow));
                        break;
                    case SUCCESS:
                        statusText.setText(getResources().getString(R.string.success_label));
                        statusText.setTextColor(getResources().getColor(R.color.colorGreen));
                        break;
                }
                actionIdText.setOnClickListener(v -> startActionActivity(
                        parsersInfoModel.getParser_actionID(),
                        parsersInfoModel.getParser_action(),
                        parsersInfoModel.getStatusEnums()));

                actionText.setOnClickListener(v -> startActionActivity(
                        parsersInfoModel.getParser_actionID(),
                        parsersInfoModel.getParser_action(),
                        parsersInfoModel.getStatusEnums()));

            }
        });


        TextView recentTransText = view.findViewById(R.id.recentTransa_id);
        RecyclerView parserTransactionRecyclerView = view.findViewById(R.id.parser_transac_recyclerView);
        parserTransactionRecyclerView.setLayoutManager(ViewsRelated.setMainLinearManagers(getContext()));

        parsersViewModel.loadParsersTransactionsObs().observe(getViewLifecycleOwner(), transactions-> {
            switch (transactions.getEnums()) {
                case LOADING:
                        recentTransText.setText(getResources().getString(R.string.loadingText));
                    break;
                case EMPTY:
                        recentTransText.setText(getResources().getString(R.string.zero_transactions));
                    break;
                case HAS_DATA:
                        recentTransText.setText(getResources().getString(R.string.recent_transactions));
                        parserTransactionRecyclerView.setAdapter(new HoverAdapters.TransactionRecyclerAdapter(transactions.getTransactionModelsList(),
                            this,
                            getResources().getColor(R.color.colorYellow),
                            getResources().getColor(R.color.colorRed),
                            getResources().getColor(R.color.colorGreen)));
                    break;
            }
        });

        parsersViewModel.getParsersInfo(ParsersActivity.parserId);
        parsersViewModel.getParserTransactions(ParsersActivity.parserId);


        return view;
    }

    private void startActionActivity(String actionId, String actionTitle, StatusEnums statusEnums) {
        Intent i = new Intent(getActivity(), ActionDetailsActivity.class);
        i.putExtra(Apis.ACTION_ID, actionId);
        i.putExtra(Apis.ACTION_TITLE, actionTitle);
        i.putExtra(Apis.ACTION_STATUS, statusEnums);
        startActivity(i);
    }

    @Override
    public void customClickListener(Object... data) {

    }
}
