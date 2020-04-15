package com.usehover.testerv2.ui.transactionDetails;

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

import com.usehover.testerv2.R;
import com.usehover.testerv2.adapters.TransactionDetailsRecyclerAdapter;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.ClickTypeEnum;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.enums.TransactionDetailsDataType;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.ui.action_details.ActionDetailsActivity;
import com.usehover.testerv2.ui.parsers.ParsersActivity;
import com.usehover.testerv2.ui.webview.WebViewActivity;
import com.usehover.testerv2.utils.UIHelper;

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
                UIHelper.changeStatusBarColor(getActivity(), getResources().getColor(R.color.colorYellow));
                topLayout.setBackgroundColor(getResources().getColor(R.color.colorYellow));
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
                UIHelper.changeStatusBarColor(getActivity(), getResources().getColor(R.color.colorRed));
                topLayout.setBackgroundColor(getResources().getColor(R.color.colorRed));
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
                UIHelper.changeStatusBarColor(getActivity(), getResources().getColor(R.color.colorGreen));
                topLayout.setBackgroundColor(getResources().getColor(R.color.colorGreen));
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

        TransactionDetailsViewModel transactionDetailsViewModel = new ViewModelProvider(this).get(TransactionDetailsViewModel.class);
        transactionDetailsViewModel.loadAboutInfoModelsObs().observe(getViewLifecycleOwner(), model->{
            if(model !=null) {
                aboutInfoRecyclerView.setAdapter(new TransactionDetailsRecyclerAdapter(model,
                        this,
                        R.color.colorRed,
                        R.color.colorYellow,
                        R.color.colorGreen));
            }

        });

        transactionDetailsViewModel.loadDeviceModelsObs().observe(getViewLifecycleOwner(), model-> {
            if(model !=null)
                aboutInfoRecyclerView.setAdapter(new TransactionDetailsRecyclerAdapter(model, this));
        });

        transactionDetailsViewModel.loadDebugInfoModelsObs().observe(getViewLifecycleOwner(), model-> {
            if(model !=null)
                aboutInfoRecyclerView.setAdapter(new TransactionDetailsRecyclerAdapter(model, this));
        });

        transactionDetailsViewModel.getAboutInfoModels(TransactionDetailsActivity.transactionId);
        transactionDetailsViewModel.getDebugInfoModels(TransactionDetailsActivity.transactionId);
        transactionDetailsViewModel.getDeviceModels(TransactionDetailsActivity.transactionId);

        return view;
    }

    @Override
    public void customClickListener(Object... data) {
        if (data[0] == ClickTypeEnum.CLICK_ACTION) {
            Intent i = new Intent(getActivity(), ActionDetailsActivity.class);
            i.putExtra(Apis.ACTION_ID, (String) data[1]);
            i.putExtra(Apis.ACTION_TITLE, (String) data[2]);
            i.putExtra(Apis.ACTION_STATUS, (StatusEnums) data[3]);
            startActivity(i);
        }
        else {
            Intent i2 = new Intent(getActivity(), ParsersActivity.class);
            i2.putExtra(ParsersActivity.PARSER_EXTRA, (String )data[1]);
            startActivity(i2);
        }
    }
}
