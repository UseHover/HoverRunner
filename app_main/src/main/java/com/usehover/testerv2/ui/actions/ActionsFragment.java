package com.usehover.testerv2.ui.actions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.usehover.testerv2.R;
import com.usehover.testerv2.adapters.HomeActionRecyclerAdapter;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.interfaces.CustomOnClickListener;
import com.usehover.testerv2.ui.action_details.ActionDetailsActivity;
import com.usehover.testerv2.ui.actions.filter.ActionFilterActivity;
import com.usehover.testerv2.utils.UIHelper;

public class ActionsFragment extends Fragment implements CustomOnClickListener {


    private ActionsViewModel actionsViewModel;
    private static final int FILTER_RESULT = 300;
    private TextView filterText, emptyStateText;
    private ProgressBar progressBar;
    private RecyclerView homeActionsRecyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actionsViewModel = new ViewModelProvider(this).get(ActionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_actions, container, false);
        filterText  = root.findViewById(R.id.actionFilter_id);
        progressBar = root.findViewById(R.id.progress_state_1);
        emptyStateText = root.findViewById(R.id.empty_text_1);

        homeActionsRecyclerView = root.findViewById(R.id.recyclerViewId);
        homeActionsRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));


        UIHelper.setTextUnderline(filterText, getResources().getString(R.string.filter_text));

        //CALL THE FILTER FUNCTION
        filterText.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ActionFilterActivity.class);
            startActivityForResult(i, FILTER_RESULT);
        });

        //GET ALL ACTIONS
        actionsViewModel.getAllActions();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        actionsViewModel.getText().observe(getViewLifecycleOwner(), filterStatus-> {
            switch (filterStatus) {
                case FILTER_OFF: filterText.setTextColor(getResources().getColor(R.color.colorHoverWhite));
                    break;
                case FILTER_ON: filterText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filterText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_purple_24dp, 0,0,0);
                    filterText.setCompoundDrawablePadding(8);
                    break;
            }
        });

        actionsViewModel.loadActionsObs().observe(getViewLifecycleOwner(), fullActionResult -> {
            switch (fullActionResult.getActionEnum()){
                case LOADING:
                    if(homeActionsRecyclerView.getVisibility() == View.VISIBLE)homeActionsRecyclerView.setVisibility(View.GONE);
                    if(emptyStateText.getVisibility() == View.VISIBLE) emptyStateText.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case EMPTY:
                    if(homeActionsRecyclerView.getVisibility() == View.VISIBLE)homeActionsRecyclerView.setVisibility(View.GONE);
                    if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
                    emptyStateText.setVisibility(View.VISIBLE);
                    break;
                case HAS_DATA:
                    if(emptyStateText.getVisibility() == View.VISIBLE) emptyStateText.setVisibility(View.GONE);
                    if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
                    if(homeActionsRecyclerView.getVisibility() != View.VISIBLE) homeActionsRecyclerView.setVisibility(View.VISIBLE);
                    homeActionsRecyclerView.setAdapter(new HomeActionRecyclerAdapter(fullActionResult.getActionsModelList(), true,
                            this,
                            getResources().getColor(R.color.colorYellow),
                            getResources().getColor(R.color.colorRed)));
                    break;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String newText = data.getStringExtra("idn");
                assert newText != null;
                if (newText.equals("enteredTextValue")) {
                    actionsViewModel.setFilterOn();
                }
            }
        }
    }


    @Override
    public void customClickListener(Object... data) {
        assert data!=null;
        Intent i = new Intent(getActivity(), ActionDetailsActivity.class);
        i.putExtra(Apis.ACTION_ID, (String) data[0]);
        i.putExtra(Apis.ACTION_TITLE, (String) data[1]);
        i.putExtra(Apis.ACTION_STATUS, (StatusEnums) data[2]);
        startActivity(i);
    }
}
