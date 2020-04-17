package com.usehover.testerv2.ui.actions.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.usehover.testerv2.R;
import com.usehover.testerv2.utils.UIHelper;

public class ActionFilterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.action_filter_fragment, container, false);

        TextView toolText = view.findViewById(R.id.actionFilterBackId);
        TextView resetText = view.findViewById(R.id.reset_id);
        toolText.setOnClickListener(v -> getActivity().finish());

        TextView datePickerView = view.findViewById(R.id.dateRangeEditId);
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<?> picker = builder.setTitleText(getResources().getString(R.string.selected_range)).build();
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


    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                //focusedView = v;
            } else {
               // focusedView  = null;
            }
        }
    };
}
