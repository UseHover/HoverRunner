package com.usehover.testerv2.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.enums.StatusEnums;

public class ViewsRelated {
    private static final int INITIAL_ITEMS_FETCH = 30;

    public static LinearLayoutManager setMainLinearManagers(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setInitialPrefetchItemCount(INITIAL_ITEMS_FETCH);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        return  linearLayoutManager;
    }

     static int getActionIconDrawable(StatusEnums enums) {
        if (enums == StatusEnums.PENDING)
            return R.drawable.ic_warning_yellow_24dp;
        else if (enums == StatusEnums.UNSUCCESSFUL)
            return R.drawable.ic_error_red_24dp;
        else return R.drawable.ic_check_circle_green_24dp;
    }

    static class ActionListItemView extends RecyclerView.ViewHolder {
         TextView actionIdText, actionTitleText;
         ImageView iconImage;
         ActionListItemView(@NonNull View itemView) {
            super(itemView);
            actionIdText = itemView.findViewById(R.id.actionIdText_Id);
            actionTitleText = itemView.findViewById(R.id.actionTitle_Id);
            iconImage = itemView.findViewById(R.id.actionIconStatus);
        }
    }

    static class TransactionListItemView extends  RecyclerView.ViewHolder {
        TextView date, content;
        TransactionListItemView(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.transaction_date_id);
            content = itemView.findViewById(R.id.transaction_content_id);
        }
    }


}
