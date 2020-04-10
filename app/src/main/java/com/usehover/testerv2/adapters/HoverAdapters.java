package com.usehover.testerv2.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.TransactionModels;
import com.usehover.testerv2.utils.UIHelper;

import java.util.List;
public class HoverAdapters {
     public static class HomeActionRecyclerAdapter extends RecyclerView.Adapter<ViewsRelated.ActionListItemView> {

        private List<ActionsModel> actionsModel;
        private boolean showStatus;
        private CustomOnClickListener customOnClickListener;

        public HomeActionRecyclerAdapter(List<ActionsModel> actionsModel, boolean showStatus, CustomOnClickListener customOnClickListener) {
            this.actionsModel = actionsModel;
            this.showStatus = showStatus;
            this.customOnClickListener = customOnClickListener;
        }


        @NonNull
        @Override
        public ViewsRelated.ActionListItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_list_items, parent, false);
            return new ViewsRelated.ActionListItemView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewsRelated.ActionListItemView holder, int position) {
            ActionsModel model = actionsModel.get(position);
            UIHelper.setTextUnderline(holder.actionIdText, model.getActionId());
            holder.actionTitleText.setText(model.getActionTitle());


            if (showStatus) {
                if (model.getActionEnum() != StatusEnums.SUCCESS)
                    holder.iconImage.setImageResource(ViewsRelated.getActionIconDrawable(model.getActionEnum()));
            }
            holder.itemView.setOnClickListener(v -> customOnClickListener.customClickListener(model.getActionId()));

        }
         @Override
         public long getItemId(int position) {
             return position;
         }

         @Override
         public int getItemViewType(int position) {
             return position;
         }

        @Override
        public int getItemCount() {
            if (actionsModel == null) return 0;
            return actionsModel.size();
        }
    }


    public static class TransactionRecyclerAdapter extends RecyclerView.Adapter<ViewsRelated.TransactionListItemView> {

         private List<TransactionModels> transactionModelsList;
         private CustomOnClickListener customOnClickListener;
         private int colorPending, colorFailed;

        public TransactionRecyclerAdapter(List<TransactionModels> transactionModelsList, CustomOnClickListener customOnClickListener, int colorPending, int colorFailed) {
            this.transactionModelsList = transactionModelsList;
            this.customOnClickListener = customOnClickListener;
            this.colorPending = colorPending;
            this.colorFailed = colorFailed;
        }

        @NonNull
        @Override
        public ViewsRelated.TransactionListItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_list_items, parent, false);
            return new ViewsRelated.TransactionListItemView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewsRelated.TransactionListItemView holder, int position) {
            TransactionModels transactionModels = transactionModelsList.get(position);
            holder.date.setText(transactionModels.getDate());
            holder.content.setText(transactionModels.getCaption());

            if(transactionModels.getStatusEnums() != StatusEnums.SUCCESS) {
                holder.date.setCompoundDrawablesWithIntrinsicBounds(0,0,ViewsRelated.getActionIconDrawable(transactionModels.getStatusEnums()), 0);
                holder.date.setCompoundDrawablePadding(8);
                holder.date.setTextColor(transactionModels.getStatusEnums() == StatusEnums.PENDING ? colorPending : colorFailed );
            }

            holder.itemView.setOnClickListener(v -> customOnClickListener.customClickListener(transactionModels.getTransaction_id()));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            if (transactionModelsList == null) return 0;
            return transactionModelsList.size();
        }
    }
}