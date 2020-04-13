package com.usehover.testerv2.ui.action_details;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.usehover.testerv2.R;
import com.usehover.testerv2.ui.webview.WebViewActivity;
import com.usehover.testerv2.utils.UIHelper;

public class ActionDetailsFragment extends Fragment {


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
                descTitle.setCompoundDrawablePadding(16);
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
                descTitle.setCompoundDrawablePadding(16);
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
                descTitle.setCompoundDrawablePadding(16);
                descContent.setVisibility(View.GONE);
                descLink.setVisibility(View.GONE);
                break;
            default:
                UIHelper.changeStatusBarColor(getActivity(), getResources().getColor(R.color.colorSecondaryGrey));
                descTitle.setPadding(42,0,0,0);
        }


        return  view;
    }

}
