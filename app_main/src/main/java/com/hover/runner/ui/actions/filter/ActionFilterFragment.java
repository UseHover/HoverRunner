package com.hover.runner.ui.actions.filter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.hover.runner.R;
import com.hover.runner.api.Apis;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.models.FilterDataFullModel;
import com.hover.runner.states.ActionState;
import com.hover.runner.ui.filter_pages.FilterByCategoriesActivity;
import com.hover.runner.ui.filter_pages.FilterByCountriesActivity;
import com.hover.runner.ui.filter_pages.FilterByNetworksActivity;
import com.hover.runner.utils.UIHelper;
import com.hover.runner.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ActionFilterFragment extends Fragment {

    private ProgressBar loadingProgressBar;
    private FilterDataFullModel filterDataFullModel;
    private TextView resetText, countryEntry, networkEntry, categoryEntry, datePickerView;
    private EditText searchActionEdit;
    private AppCompatCheckBox status_success, status_pending, status_fail, status_noTrans, withParser, onlyWithSimPresent;
    private Timer timer = new Timer();
    private boolean resetActivated = false;
    private ActionFilterViewModel actionFilterViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.action_filter_fragment, container, false);


        TextView toolText = view.findViewById(R.id.actionFilterBackId);
        LinearLayout entryFilterView = view.findViewById(R.id.entry_filter_view);
        TextView showActionsText = view.findViewById(R.id.showActions_id);
        loadingProgressBar = view.findViewById(R.id.filter_progressBar);
        resetText = view.findViewById(R.id.reset_id);
        countryEntry = view.findViewById(R.id.countryEntryId);
        networkEntry = view.findViewById(R.id.networkEntryId);
        categoryEntry = view.findViewById(R.id.categoryEntryId);
        searchActionEdit = view.findViewById(R.id.searchEditId);
        status_success = view.findViewById(R.id.checkbox_success);
        status_pending = view.findViewById(R.id.checkbox_pending);
        status_fail = view.findViewById(R.id.checkbox_fail);
        status_noTrans = view.findViewById(R.id.checkbox_no_transaction);
        withParser = view.findViewById(R.id.checkbox_parsers);
        onlyWithSimPresent = view.findViewById(R.id.checkbox_sim);
        datePickerView = view.findViewById(R.id.dateRangeEditId);

        ;

        toolText.setOnClickListener(v -> { if(getActivity() !=null) {
            prepareForPreviousActivity(true);
            getActivity().finish();
        } });
        loadingProgressBar.setVisibility(View.VISIBLE);
        loadingProgressBar.setIndeterminate(true);
        UIHelper.setTextUnderline(resetText, "Reset");


        actionFilterViewModel = new ViewModelProvider(this).get(ActionFilterViewModel.class);
        actionFilterViewModel.loadAllDataObs().observe(getViewLifecycleOwner(), result-> {
            if(result.getActionEnum() == StatusEnums.HAS_DATA) {
                loadingProgressBar.setVisibility(View.GONE);
                filterDataFullModel = result;
                entryFilterView.setVisibility(View.VISIBLE);
                filterThroughActions();
            }
            else if(result.getActionEnum() == StatusEnums.EMPTY) {
                filterDataFullModel = null;
                UIHelper.flashMessage(getContext(), getResources().getString(R.string.no_actions_yet));
                if(getActivity()!=null)getActivity().finish();
            }
        });


        showActionsText.setOnClickListener(v -> {
            //We are setting two list of actions, so that if user clicks cancel, it shows the previously filtered actions
            //When users clicks this button, it then adds the latest filtered actions into this bucket.
            prepareForPreviousActivity(false);
            if(getActivity() !=null)getActivity().finish();
        });
        actionFilterViewModel.loadActionsObs().observe(getViewLifecycleOwner(), filterResult-> {
            if(filterResult.getActionEnum() == StatusEnums.HAS_DATA) {
                showActionsText.setClickable(true);
                showActionsText.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                String suffixAction ="actions";
                if(filterResult.getActionsModelList().size() == 1) suffixAction = "action";
                showActionsText.setText(String.format(Locale.getDefault(), "Show %d %s", filterResult.getActionsModelList().size(), suffixAction));
            }
            else if(filterResult.getActionEnum() == StatusEnums.LOADING) {
                showActionsText.setClickable(false);
                showActionsText.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                showActionsText.setText(getResources().getString(R.string.loadingText));
            }
            else {
                //It means empty
                showActionsText.setClickable(false);
                showActionsText.setBackgroundColor(getResources().getColor(R.color.colorMainGrey));
                showActionsText.setText(getResources().getString(R.string.no_actions_filter_result));
            }

        });


        searchActionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ActionState.setActionSearchText(s.toString());
                timer.cancel();
                timer = new Timer();
                long DELAY = 1500;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        activateReset();
                        filterThroughActions();
                    }
                }, DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(ActionState.getResultFilter_Actions_LOAD().size() > 0) {
            activateReset();
        }
        resetText.setOnClickListener(v->{
            if(resetActivated) {
                new Apis().resetActionFilterDataset();
                reloadAllMajorViews();
                ActionState.setResultFilter_Actions(new ArrayList<>());
                ActionState.setResultFilter_Actions_LOAD(new ArrayList<>());
                deactivateReset();

                UIHelper.flashMessage(getContext(), getResources().getString(R.string.reset_successful));
                new Handler().postDelayed(this::deactivateReset, 800);
            }
        });


        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Pair<Long, Long>> picker = builder.setTitleText(getResources().getString(R.string.selected_range)).build();

        datePickerView.setOnClickListener(v -> {
            if(hasLoaded()) picker.show(getParentFragmentManager(), picker.toString());
        });
        picker.addOnPositiveButtonClickListener(selection -> {
            ActionState.setDateRange(selection);
            setOrReloadDateRange();
            filterThroughActions();

        });

        setUpViewClicks();
        setupCheckboxes();
        actionFilterViewModel.getFullDataFirst();

        return view;
    }

    private void prepareForPreviousActivity(boolean isBackButtonPressed) {
        if(Apis.actionFilterIsInNormalState()) {

            if (!isBackButtonPressed) {
                ActionState.setResultFilter_Actions_LOAD(filterDataFullModel.getActionsModelList());
            }  //ActionState.setResultFilter_Actions_LOAD(new ArrayList<>());

            ActionState.setResultFilter_Actions(new ArrayList<>());
        }
        else {
            ActionState.setResultFilter_Actions_LOAD(ActionState.getResultFilter_Actions());
            ActionState.setResultFilter_Actions(new ArrayList<>());
        }
    }

    private void filterThroughActions() {
        if(filterDataFullModel == null) onCreate(null);
        else {
            actionFilterViewModel.getOrReloadFilterActions(filterDataFullModel.getActionsModelList(), filterDataFullModel.getTransactionModelsList());
        }
    }

    private void deactivateReset() {
        resetText.setTextColor(getResources().getColor(R.color.colorMainGrey));
        resetActivated = false;
    }
    private void activateReset() {
        if(!Apis.actionFilterIsInNormalState()) {
            resetText.setTextColor(getResources().getColor(R.color.colorHoverWhite));
            resetActivated = true;
        }

    }
    private void setupCheckboxes() {
        status_success.setOnCheckedChangeListener((v, status)-> {
            ActionState.setStatusSuccess(status);
            activateReset();
            filterThroughActions();
        });

        status_noTrans.setOnCheckedChangeListener((v, status)-> {
            ActionState.setStatusNoTrans(status);
            activateReset();
            filterThroughActions();
        });

        status_fail.setOnCheckedChangeListener((v, status)-> {
            ActionState.setStatusFailed(status);
            activateReset();
            filterThroughActions();
        });

        status_pending.setOnCheckedChangeListener((v, status)-> {
            ActionState.setStatusPending(status);
            activateReset();
            filterThroughActions();
        });

        withParser.setOnCheckedChangeListener((v, status) -> {
            ActionState.setWithParsers(status);
            activateReset();
            filterThroughActions();
        });

        onlyWithSimPresent.setOnCheckedChangeListener((v, status)-> {
            ActionState.setOnlyWithSimPresent(status);
            activateReset();
            filterThroughActions();
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        reloadAllMajorViews();
    }

    private void reloadAllMajorViews() {
        setOrReloadSearchEdit();
        setOrReloadCountriesText();
        setOrReloadNetworkText();
        setOrReloadDateRange();
        setOrReloadCategoryText();
        setOrReloadCheckboxes();
    }


    private void setUpViewClicks() {
        countryEntry.setOnClickListener(v-> {
            Intent i = new Intent(getActivity(), FilterByCountriesActivity.class);
            i.putExtra("data", new Apis().getCountriesForActionFilter(filterDataFullModel.getAllCountries()));
            i.putExtra("filter_type", 0);
            startActivity(i);
        });

        networkEntry.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), FilterByNetworksActivity.class);
            i.putExtra("data", new Apis().getNetworksForActionFilter(filterDataFullModel.getAllNetworks()));
            i.putExtra("filter_type", 0);
            startActivity(i);
        });

        categoryEntry.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), FilterByCategoriesActivity.class);
            i.putExtra("data", new Apis().getCategoriesForActionFilter(filterDataFullModel.getAllCategories()));
            startActivity(i);
        });
    }

    private void setOrReloadSearchEdit() {
        if(ActionState.getActionSearchText() !=null) {
            if(!ActionState.getActionSearchText().isEmpty()) {
                searchActionEdit.setText(ActionState.getActionSearchText());
            }else searchActionEdit.setText("");
        } else searchActionEdit.setText("");
    }
    private void setOrReloadCountriesText() {
        if(ActionState.getCountriesFilter().size()>0) {
            countryEntry.setText(new Apis().getSelectedCountriesAsText());
            activateReset();
        }
        else countryEntry.setText("");
    }



    private void setOrReloadNetworkText() {
        if(ActionState.getNetworksFilter().size()>0) {
            networkEntry.setText(new Apis().getSelectedNetworksAsText());
            activateReset();
        }
        else networkEntry.setText("");
    }

    private void setOrReloadDateRange() {
        if(ActionState.getDateRange() != null) {
            Pair<Long, Long> dateRange = ActionState.getDateRange();
            datePickerView.setText(String.format(Locale.getDefault(), "%s - %s",
                    Utils.formatDateV2((long) Utils.nonNullDateRange(dateRange.first)),
                    Utils.formatDateV3((long) Utils.nonNullDateRange(dateRange.second))));
            activateReset();
        }
        else {
            datePickerView.setText(String.format(Locale.getDefault(), "From %s - %s",
                    "<account creation>",
                    Utils.formatDateV3((long) Utils.nonNullDateRange(new Date().getTime()))));
        }
    }

    private void setOrReloadCategoryText() {
        if(ActionState.getCategoryFilter().size() > 0) {
            categoryEntry.setText(new Apis().getSelectedCategoriesAsText());
            activateReset();
        }
        else categoryEntry.setText("");
    }

    private void setOrReloadCheckboxes() {
        status_success.setChecked(ActionState.isStatusSuccess());
        status_pending.setChecked(ActionState.isStatusPending());
        status_fail.setChecked(ActionState.isStatusFailed());
        status_noTrans.setChecked(ActionState.isStatusNoTrans());
        withParser.setChecked(ActionState.isWithParsers());
        onlyWithSimPresent.setChecked(ActionState.isOnlyWithSimPresent());
    }

    private boolean hasLoaded() {
        return loadingProgressBar.getVisibility() == View.GONE;
    }

}
