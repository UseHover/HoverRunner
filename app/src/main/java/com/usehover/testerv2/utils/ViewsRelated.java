package com.usehover.testerv2.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.enums.ActionEnums;

public class ViewsRelated {
    private static final int INITIAL_ITEMS_FETCH = 20;

    public static LinearLayoutManager setMainLinearManagers(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setInitialPrefetchItemCount(INITIAL_ITEMS_FETCH);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        return  linearLayoutManager;
    }

    public static int getActionIconDrawable(ActionEnums enums) {
        if (enums == ActionEnums.PENDING)
            return R.drawable.ic_warning_yellow_24dp;
        return R.drawable.ic_error_red_24dp;
    }

    public static class ActionListItemView extends RecyclerView.ViewHolder {
        public TextView actionIdText, actionTitleText;
        public ImageView iconImage;
        public ActionListItemView(@NonNull View itemView) {
            super(itemView);
            actionIdText = itemView.findViewById(R.id.actionIdText_Id);
            actionTitleText = itemView.findViewById(R.id.actionTitle_Id);
            iconImage = itemView.findViewById(R.id.actionIconStatus);
        }
    }
}
