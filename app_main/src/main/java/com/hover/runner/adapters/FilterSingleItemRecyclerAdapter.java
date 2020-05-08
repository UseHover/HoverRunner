package com.hover.runner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.hover.runner.R;
import com.hover.runner.interfaces.CustomOnClickListener;
import com.hover.runner.models.SingleFilterInfoModel;

import java.util.ArrayList;

public class FilterSingleItemRecyclerAdapter extends RecyclerView.Adapter<FilterSingleItemRecyclerAdapter.FilterSingleViewHolder> {
    private ArrayList<SingleFilterInfoModel> entries;
    private CustomOnClickListener clickListener;
    private boolean makeFaint = false;

    public FilterSingleItemRecyclerAdapter(ArrayList<SingleFilterInfoModel> countries, CustomOnClickListener clickListener) {
        this.entries = countries;
        this.clickListener = clickListener;
    }

    public FilterSingleItemRecyclerAdapter(ArrayList<SingleFilterInfoModel> entries, CustomOnClickListener clickListener, boolean makeFaint) {
        this.entries = entries;
        this.clickListener = clickListener;
        this.makeFaint = makeFaint;
    }

    @NonNull
    @Override
    public FilterSingleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(makeFaint ? R.layout.single_filter_options_items_v2 : R.layout.single_filter_options_items, parent, false);
        return new FilterSingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterSingleViewHolder holder, int position) {
        holder.checkBox.setText(entries.get(position).getTitle());
        holder.checkBox.setChecked(entries.get(position).isCheck());

        //Capitalize country code or value that's exactly 2 letter size.
        if(entries.get(position).getTitle().length() == 2) holder.checkBox.setAllCaps(true);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            clickListener.customClickListener(entries.get(position).getTitle(), isChecked);
        });
    }

    @Override
    public int getItemCount() {
        if(entries == null) return 0;
        return entries.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class FilterSingleViewHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox checkBox;
        FilterSingleViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_filter_option);
        }
    }
}
