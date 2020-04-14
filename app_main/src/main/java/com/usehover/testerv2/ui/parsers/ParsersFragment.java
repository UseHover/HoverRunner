package com.usehover.testerv2.ui.parsers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.adapters.HoverAdapters;
import com.usehover.testerv2.adapters.ViewsRelated;
import com.usehover.testerv2.interfaces.CustomOnClickListener;


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

        ParsersViewModel parsersViewModel = ViewModelProviders.of(this).get(ParsersViewModel.class);
        parsersViewModel.loadParserInfoObs().observe(getViewLifecycleOwner(), parsersInfoModel -> {
            if(parsersInfoModel !=null) {
                typeText.setText(parsersInfoModel.getParser_type());
                actionText.setText(parsersInfoModel.getParser_action());
                actionIdText.setText(parsersInfoModel.getParser_actionID());
                categoryText.setText(parsersInfoModel.getParser_category());
                statusText.setText(parsersInfoModel.getParser_status());
                createdText.setText(parsersInfoModel.getParser_created());
                senderText.setText(parsersInfoModel.getParser_sender());
                regexText.setText(parsersInfoModel.getParser_regex());
            }
        });


        TextView recentTransText = view.findViewById(R.id.recentTransa_id);
        RecyclerView parserTransactionRecyclerView = view.findViewById(R.id.parser_transac_recyclerView);
        parserTransactionRecyclerView.setLayoutManager(ViewsRelated.setMainLinearManagers(getContext()));

        parsersViewModel.loadParsersTransactionsObs().observe(getViewLifecycleOwner(), transactions-> {
            switch (transactions.getEnums()) {
                case LOADING: recentTransText.setText(getResources().getString(R.string.loadingText));
                    break;
                case EMPTY: recentTransText.setText(getResources().getString(R.string.zero_transactions));
                    break;
                case HAS_DATA:
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

    @Override
    public void customClickListener(Object... data) {

    }
}
