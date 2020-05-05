package com.usehover.testerv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.models.FilterDataFullModel;
import com.usehover.testerv2.models.SingleFilterInfoModel;
import com.usehover.testerv2.models.WithSubtitleFilterInfoModel;

import java.util.ArrayList;

public class WithSubtitleRecyclerAdapter extends  RecyclerView.Adapter<WithSubtitleRecyclerAdapter.WithSubtitleViewHolder>{

    private ArrayList<WithSubtitleFilterInfoModel> entries;
    private CustomOnClickListener clickListener;

    public WithSubtitleRecyclerAdapter(ArrayList<WithSubtitleFilterInfoModel> entries, CustomOnClickListener clickListener) {
        this.entries = entries;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public WithSubtitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.withsubtitle_option_items, parent, false);
        return new WithSubtitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithSubtitleViewHolder holder, int position) {
        holder.checkBox.setText(entries.get(position).getTitle());
        holder.checkBox.setChecked(entries.get(position).isCheck());
        holder.subtitle.setText(entries.get(position).getSubtitle());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            clickListener.customClickListener(entries.get(position).getTitle(), isChecked);
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(entries == null) return 0;
        return entries.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class WithSubtitleViewHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox checkBox;
        TextView subtitle;
        WithSubtitleViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_filter_option);
            subtitle = itemView.findViewById(R.id.option_subtitle);
        }
    }
}
