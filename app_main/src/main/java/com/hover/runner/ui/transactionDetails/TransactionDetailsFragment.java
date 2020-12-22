package com.hover.runner.ui.transactionDetails;

import android.content.Intent;
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

import com.hover.runner.ApplicationInstance;
import com.hover.runner.R;
import com.hover.runner.adapters.TransactionDetailsRecyclerAdapter;
import com.hover.runner.adapters.TransactionMessagesRecyclerAdapter;
import com.hover.runner.api.Apis;
import com.hover.runner.enums.ClickTypeEnum;
import com.hover.runner.enums.GlobalNav;
import com.hover.runner.interfaces.CustomOnClickListener;
import com.hover.runner.ui.action_details.ActionDetailsActivity;
import com.hover.runner.ui.parsers.ParsersActivity;
import com.hover.runner.ui.webview.WebViewActivity;
import com.hover.runner.utils.UIHelper;

public class TransactionDetailsFragment extends Fragment implements CustomOnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_details_fragment, container, false);

        TextView toolText = view.findViewById(R.id.transactionDetails_toolbarText);
        TextView subtoolText = view.findViewById(R.id.transactionDetails_subtoolText);
        TextView descTitle = view.findViewById(R.id.transactionDetails_statusTitle);

        TextView descContent = view.findViewById(R.id.transactionDetails_statusDesc);
        TextView descLink = view.findViewById(R.id.transactionDetails_statusLink);
        LinearLayout topLayout = view.findViewById(R.id.transactionDetailsTopLayoutId);

        toolText.setText(TransactionDetailsActivity.transactionDate);
        subtoolText.setText(TransactionDetailsActivity.transactionId);
        toolText.setOnClickListener(v -> {
            if(getActivity() !=null) getActivity().finish();
        });

        switch (TransactionDetailsActivity.transactionStatusEnums) {
            case PENDING:
                UIHelper.changeStatusBarColor(getActivity(), ApplicationInstance.getColorYellow());
                topLayout.setBackgroundColor(ApplicationInstance.getColorYellow());
                descContent.setVisibility(View.VISIBLE);
                descLink.setVisibility(View.VISIBLE);

                descTitle.setText(getResources().getString(R.string.transaction_det_pending));
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
                UIHelper.changeStatusBarColor(getActivity(), ApplicationInstance.getColorRed());
                topLayout.setBackgroundColor(ApplicationInstance.getColorRed());
                descContent.setVisibility(View.VISIBLE);
                descLink.setVisibility(View.VISIBLE);

                descTitle.setText(getResources().getString(R.string.transaction_det_failed));
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
                UIHelper.changeStatusBarColor(getActivity(), ApplicationInstance.getColorGreen());
                topLayout.setBackgroundColor(ApplicationInstance.getColorGreen());
                descContent.setVisibility(View.VISIBLE);
                descLink.setVisibility(View.VISIBLE);

                descTitle.setText(getResources().getString(R.string.transaction_det_success));
                descTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0,0,0);
                descTitle.setCompoundDrawablePadding(32);
                descContent.setVisibility(View.GONE);
                descLink.setVisibility(View.GONE);

                break;
        }

        RecyclerView aboutInfoRecyclerView = view.findViewById(R.id.transac_aboutInfo_recyclerView);
        RecyclerView deviceRecyclerView = view.findViewById(R.id.transac_devices_recyclerView);
        RecyclerView debugInfoRecyclerView = view.findViewById(R.id.transac_debugInfo_recyclerView);
        RecyclerView messagesRecyclerView = view.findViewById(R.id.transac_messages_recyclerView);

        aboutInfoRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));
        deviceRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));
        debugInfoRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));
        messagesRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));

        aboutInfoRecyclerView.setHasFixedSize(true);
        debugInfoRecyclerView.setHasFixedSize(true);
        debugInfoRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setHasFixedSize(true);

        TransactionDetailsViewModel transactionDetailsViewModel = new ViewModelProvider(this).get(TransactionDetailsViewModel.class);
        transactionDetailsViewModel.loadAboutInfoModelsObs().observe(getViewLifecycleOwner(), model->{
            if(model !=null) {
                aboutInfoRecyclerView.setAdapter(new TransactionDetailsRecyclerAdapter(model, this, true));
            }
        });

        transactionDetailsViewModel.loadDeviceModelsObs().observe(getViewLifecycleOwner(), model2-> {
            if(model2 !=null) deviceRecyclerView.setAdapter(new TransactionDetailsRecyclerAdapter(model2, this));
        });

        transactionDetailsViewModel.loadDebugInfoModelsObs().observe(getViewLifecycleOwner(), model3-> {
            if(model3 !=null) debugInfoRecyclerView.setAdapter(new TransactionDetailsRecyclerAdapter(model3, this));
        });

        transactionDetailsViewModel.loadMessagesModelObs().observe(getViewLifecycleOwner(), model->{
            if(model !=null) messagesRecyclerView.setAdapter(new TransactionMessagesRecyclerAdapter(model));
        });

        transactionDetailsViewModel.getAboutInfoModels(TransactionDetailsActivity.transactionId);
        transactionDetailsViewModel.getDebugInfoModels(TransactionDetailsActivity.transactionId);
        transactionDetailsViewModel.getDeviceModels(TransactionDetailsActivity.transactionId);
        transactionDetailsViewModel.getMessagesModels(TransactionDetailsActivity.transactionId);

        setupGlobalNav(view);
        return view;
    }

    private void setupGlobalNav(View view) {
        view.findViewById(R.id.global_nav_Actions).setOnClickListener(v ->UIHelper.doGlobalNavigation(GlobalNav.NAV_ACTION, getActivity()));
        view.findViewById(R.id.global_nav_Transactions).setOnClickListener(v -> UIHelper.doGlobalNavigation(GlobalNav.NAV_TRANSACTION, getActivity()));
        view.findViewById(R.id.global_nav_Settings).setOnClickListener(v->UIHelper.doGlobalNavigation(GlobalNav.NAV_SETTINGS, getActivity()));
    }
    @Override
    public void customClickListener(Object... data) {
        if (data[0] == ClickTypeEnum.CLICK_ACTION) {
            Intent i = new Intent(getActivity(), ActionDetailsActivity.class);
            i.putExtra(Apis.ACTION_ID, (String) data[1]);
            i.putExtra(Apis.ACTION_TITLE, (String) data[2]);
            i.putExtra(Apis.ACTION_STATUS, TransactionDetailsActivity.transactionStatusEnums);
            startActivity(i);
        }
        else {
            Intent i2 = new Intent(getActivity(), ParsersActivity.class);
            i2.putExtra(ParsersActivity.PARSER_EXTRA, Integer.valueOf((String) data[1]));
            startActivity(i2);
        }
    }
}
