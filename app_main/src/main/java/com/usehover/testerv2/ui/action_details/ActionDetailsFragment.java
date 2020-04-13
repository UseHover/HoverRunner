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
import androidx.lifecycle.ViewModelProviders;

import com.usehover.testerv2.R;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.interfaces.ParserClickListener;
import com.usehover.testerv2.ui.actions.ActionsViewModel;
import com.usehover.testerv2.ui.webview.WebViewActivity;
import com.usehover.testerv2.utils.UIHelper;

public class ActionDetailsFragment extends Fragment implements ParserClickListener, CustomOnClickListener {

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
        toolText.setOnClickListener(v -> getActivity().finish());

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


        ActionDetailsLiveModel actionDetailsLiveModel = ViewModelProviders.of(this).get(ActionDetailsLiveModel.class);
        actionDetailsLiveModel.loadActionDetailsObs().observe(getViewLifecycleOwner(), model-> {
            if(model !=null) {
                operatorsText.setText(model.getOperators());
                stepsText.setText(model.getSteps());
                UIHelper.makeEachTextLinks(model.getParsers(), parsersText, this);
                transacText.setText(model.getTransactionsNo());
                successText.setText(model.getSuccessNo());
                pendingText.setText(model.getPendingNo());
                failureText.setText(model.getFailedNo());
            }
        });
        actionDetailsLiveModel.getDetails(ActionDetailsActivity.actionId);

        return  view;
    }

    @Override
    public void customClickListener(Object... data) {

    }

    @Override
    public void onClickParser(String str) {
        UIHelper.showHoverToast(getContext(), getActivity().getCurrentFocus(), str);
    }
}
