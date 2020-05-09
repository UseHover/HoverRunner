package com.hover.runner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hover.runner.R;
import com.hover.runner.enums.ClickTypeEnum;
import com.hover.runner.interfaces.CustomOnClickListener;
import com.hover.runner.models.TransactionDetailsInfoModels;
import com.hover.runner.utils.UIHelper;

import java.util.ArrayList;


public class TransactionDetailsRecyclerAdapter extends RecyclerView.Adapter<TransactionDetailsRecyclerAdapter.TDViewHolder> {

    private ArrayList<TransactionDetailsInfoModels> modelsArrayList;
    private CustomOnClickListener customOnClickListener;
    private String actionId = ""; private String actionName="";

    private int colorRed, colorYellow, colorGreen;

    public TransactionDetailsRecyclerAdapter(ArrayList<TransactionDetailsInfoModels> modelsArrayList, CustomOnClickListener customOnClickListener) {
        this.modelsArrayList = modelsArrayList;
        this.customOnClickListener = customOnClickListener;
    }

    public TransactionDetailsRecyclerAdapter(ArrayList<TransactionDetailsInfoModels> modelsArrayList, CustomOnClickListener customOnClickListener, int colorRed, int colorYellow, int colorGreen) {
        this.modelsArrayList = modelsArrayList;
        this.customOnClickListener = customOnClickListener;
        this.colorRed = colorRed;
        this.colorYellow = colorYellow;
        this.colorGreen = colorGreen;

        for(TransactionDetailsInfoModels models : modelsArrayList) {
            if(models.getLabel().equals("ActionID")) {
                actionId = models.getValue();
            }
            if(models.getLabel().equals("Action")) {
                actionName = models.getValue();
            }
        }
    }

    @NonNull
    @Override
    public TDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transac_details_list_items, parent, false);
        return new TDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TDViewHolder holder, int position) {
        TransactionDetailsInfoModels infoModels = modelsArrayList.get(position);

        holder.label.setText(infoModels.getLabel());

        if(infoModels.getLabel().equals("Status")) {
            switch (infoModels.getStatusEnums()) {
                case UNSUCCESSFUL:
                    holder.value.setTextColor(colorRed);
                    break;
                case PENDING:
                    holder.value.setTextColor(colorYellow);
                    break;
                case SUCCESS:
                    holder.value.setTextColor(colorGreen);
                    break;
            }
        }
        if(infoModels.isClickable()) {
            UIHelper.setTextUnderline(holder.value, infoModels.getValue());
            if(infoModels.getLabel().contains("Action"))
                holder.value.setOnClickListener(v -> customOnClickListener.customClickListener(ClickTypeEnum.CLICK_ACTION, actionId, actionName, infoModels.getStatusEnums() ));
            else holder.value.setOnClickListener(v -> customOnClickListener.customClickListener(ClickTypeEnum.CLICK_PARSER,  infoModels.getValue() ));
        }
        else holder.value.setText(infoModels.getValue());

    }

    @Override
    public int getItemCount() {
        if(modelsArrayList == null) return 0;
        return modelsArrayList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class TDViewHolder extends RecyclerView.ViewHolder {
        TextView label, value;
        TDViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.transac_det_label);
            value = itemView.findViewById(R.id.transac_det_value);

        }
    }
}
