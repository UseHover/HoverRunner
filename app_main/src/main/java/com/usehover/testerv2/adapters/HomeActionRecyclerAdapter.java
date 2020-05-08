package com.usehover.testerv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.utils.UIHelper;

import java.util.List;

public  class HomeActionRecyclerAdapter extends RecyclerView.Adapter<HomeActionRecyclerAdapter.ActionListItemView> {

    private List<ActionsModel> actionsModel;
    private boolean showStatus;
    private CustomOnClickListener customOnClickListener;
    private int colorPending, colorFailed, colorSuccess;

    public HomeActionRecyclerAdapter(List<ActionsModel> actionsModel, boolean showStatus, CustomOnClickListener customOnClickListener, int colorPending, int colorSuccess, int colorFailed) {
        this.actionsModel = actionsModel;
        this.showStatus = showStatus;
        this.customOnClickListener = customOnClickListener;
        this.colorPending = colorPending;
        this.colorFailed = colorFailed;
        this.colorSuccess = colorSuccess;
    }


    @NonNull
    @Override
    public ActionListItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_list_items, parent, false);
        return new ActionListItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionListItemView holder, int position) {
        ActionsModel model = actionsModel.get(position);
        UIHelper.setTextUnderline(holder.actionIdText, model.getActionId());
        if(model.getActionEnum() == StatusEnums.PENDING)
            holder.actionIdText.setTextColor(colorPending);
        else if(model.getActionEnum() == StatusEnums.UNSUCCESSFUL)
            holder.actionIdText.setTextColor(colorFailed);
        else if(model.getActionEnum() == StatusEnums.SUCCESS)
            holder.actionIdText.setTextColor(colorSuccess);
        holder.actionTitleText.setText(model.getActionTitle());


        if (showStatus) {
            if (model.getActionEnum() != StatusEnums.NOT_YET_RUN)
                holder.iconImage.setImageResource(UIHelper.getActionIconDrawable(model.getActionEnum()));
        }
        holder.itemView.setOnClickListener(v -> customOnClickListener.customClickListener(
                model.getActionId(),
                model.getActionTitle(),
                model.getActionEnum()
        ));

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
}