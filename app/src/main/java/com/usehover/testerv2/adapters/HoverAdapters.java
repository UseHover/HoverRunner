package com.usehover.testerv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.enums.ActionEnums;
import com.usehover.testerv2.interfaces.HomeActionsOnClickListener;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.utils.UIHelper;
import com.usehover.testerv2.utils.ViewsRelated;

import java.util.List;
public class HoverAdapters {
     public static class HomeActionRecyclerAdapter extends RecyclerView.Adapter<ViewsRelated.ActionListItemView> {

        private List<ActionsModel> actionsModel;
        private boolean showStatus;
        private HomeActionsOnClickListener homeActionsOnClickListener;

        public HomeActionRecyclerAdapter(List<ActionsModel> actionsModel, boolean showStatus, HomeActionsOnClickListener homeActionsOnClickListener) {
            this.actionsModel = actionsModel;
            this.showStatus = showStatus;
            this.homeActionsOnClickListener = homeActionsOnClickListener;
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
                if (model.getActionEnum() != ActionEnums.SUCCESS)
                    holder.iconImage.setImageResource(ViewsRelated.getActionIconDrawable(model.getActionEnum()));
            }
            holder.itemView.setOnClickListener(v -> homeActionsOnClickListener.onHomeActionClickListener(model.getActionId()));

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
}