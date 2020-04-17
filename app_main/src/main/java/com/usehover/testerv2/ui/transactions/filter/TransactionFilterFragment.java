package com.usehover.testerv2.ui.transactions.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.usehover.testerv2.R;
import com.usehover.testerv2.utils.UIHelper;

public class TransactionFilterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_filter_fragment, container, false);

        TextView toolText = view.findViewById(R.id.transactionFilterBackId);
        TextView resetText = view.findViewById(R.id.reset_id);
        toolText.setOnClickListener(v -> getActivity().finish());

        TextView datePickerView = view.findViewById(R.id.dateRangeEditId);
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker picker = builder.setTheme(R.style.DateTheme).setTitleText("Select Range").build();

        picker.addOnPositiveButtonClickListener(selection -> {
            Pair<Long, Long> datePairs = (Pair<Long, Long>) selection;
            UIHelper.showHoverToastV2(getContext(), String.valueOf(datePairs.first+" AND "+datePairs.second));

        });
        datePickerView.setOnClickListener(v -> {
            picker.show(getParentFragmentManager(), picker.toString());
        });

        view.findViewById(R.id.showActions_id).setOnClickListener(v -> getActivity().finish());

        return view;
    }
}
