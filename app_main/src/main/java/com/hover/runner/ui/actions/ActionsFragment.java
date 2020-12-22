package com.hover.runner.ui.actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hover.runner.enums.GlobalNav;
import com.hover.sdk.actions.HoverAction;
import com.hover.sdk.api.Hover;
import com.hover.sdk.api.HoverParameters;
import com.hover.runner.ApplicationInstance;
import com.hover.runner.R;
import com.hover.runner.adapters.HomeActionRecyclerAdapter;
import com.hover.runner.api.Apis;
import com.hover.runner.enums.PassageEnum;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.interfaces.CustomOnClickListener;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.ui.action_details.ActionDetailsActivity;
import com.hover.runner.ui.actions.filter.ActionFilterActivity;
import com.hover.runner.ui.actions.uncompletedVariables.UncompletedVariableActivity;
import com.hover.runner.utils.network.NetworkUtil;
import com.hover.runner.utils.UIHelper;
import com.hover.runner.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionsFragment extends Fragment implements CustomOnClickListener, Hover.DownloadListener {


    private ActionsViewModel actionsViewModel;
    private static final int FILTER_RESULT = 300;
    private static final int TEST_ALL_RESULT = 301;
    private static final int FILL_IN_UNCOMPLETED_VARIABLES = 302;
    private TextView filterText;
    private LinearLayout emptyInfoLayout;
    private ProgressBar progressBar;
    private RecyclerView homeActionsRecyclerView;
    private List<ActionsModel> rawRunnableModelList = new ArrayList<>();
    private ArrayList<ActionsModel> withCompletedVariableActionList = new ArrayList<>();
    private int actionRunCounter = 0;
    private RelativeLayout emptyStateView;
    private SwipeRefreshLayout pullToRefresh;
    private TextView emptyTitle, emptySubtitle;
    private HomeActionRecyclerAdapter homeActionRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actionsViewModel = new ViewModelProvider(this).get(ActionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_actions, container, false);

        filterText  = root.findViewById(R.id.actionFilter_id);
        progressBar = root.findViewById(R.id.progress_state_1);
        emptyInfoLayout = root.findViewById(R.id.empty_info_layout);
        emptyStateView = root.findViewById(R.id.layoutForEmptyStateId);
        pullToRefresh = root.findViewById(R.id.pullToRefresh);

        emptyTitle = root.findViewById(R.id.empty_title_text);
        emptySubtitle = root.findViewById(R.id.empty_subtitle_text);

        homeActionsRecyclerView = root.findViewById(R.id.recyclerViewId);
        homeActionsRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));
        homeActionsRecyclerView.setHasFixedSize(true);

        UIHelper.setTextUnderline(filterText, getResources().getString(R.string.filter_text));


        //CALL THE FILTER FUNCTION
        filterText.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ActionFilterActivity.class);
            startActivityForResult(i, FILTER_RESULT);
        });

        root.findViewById(R.id.testAllActions_id).setOnClickListener(v -> {
            //Ensure action is not refreshing and running at same time to prevent errors.
            if(!pullToRefresh.isRefreshing()) {
                pullToRefresh.setRefreshing(true);

                ArrayList<ActionsModel> withUncompletedVariablesActionList = new ArrayList<>();
                withCompletedVariableActionList = new ArrayList<>();

                for (ActionsModel runnableModel : rawRunnableModelList) {
                    int variableSize = Utils.getStreamlinedStepsStepsFromRaw(runnableModel.getRootCode(), runnableModel.getSteps()).getStepVariableLabel().size();
                    switch (Utils.actionHasAllVariablesFilled(getContext(), runnableModel.getActionId(), variableSize)) {
                        case GOOD:
                            withCompletedVariableActionList.add(runnableModel);
                            break;
                        case BAD:
                            String jsonArrayToString = runnableModel.getSteps().toString();
                            runnableModel.setJsonArrayToString(jsonArrayToString);
                            withUncompletedVariablesActionList.add(runnableModel);
                            break;
                        case SKIPPED:
                            if(!ApplicationInstance.isAllowSkippedActionsToRun()) {
                                String jsonArrayToString2 = runnableModel.getSteps().toString();
                                runnableModel.setJsonArrayToString(jsonArrayToString2);
                                withUncompletedVariablesActionList.add(runnableModel);
                            }
                          break;
                    }
                }

                pullToRefresh.setRefreshing(false);
                if (withUncompletedVariablesActionList.size() > 0) {
                    Intent i = new Intent(getActivity(), UncompletedVariableActivity.class);
                    i.putParcelableArrayListExtra("data", withUncompletedVariablesActionList);
                    startActivityForResult(i, FILL_IN_UNCOMPLETED_VARIABLES);
                } else {
                    if (withCompletedVariableActionList.size() > 0) runAction(true);
                    else
                        UIHelper.flashMessage(getContext(), getResources().getString(R.string.noRunnableAction));
                }
            }

        });

        setupViews();

        pullToRefresh.setOnRefreshListener(() -> {
            if(new NetworkUtil(getContext()).isNetworkAvailable() == PassageEnum.ACCEPT) {
                Hover.updateActionConfigs(this, (getContext() != null) ? getContext() : ApplicationInstance.getContext());
            }
            else {
                pullToRefresh.setRefreshing(false);
                UIHelper.flashMessage(getContext(), getActivity()!=null ? getActivity().getCurrentFocus() : null,
                        ApplicationInstance.getContext().getString(R.string.NO_NETWORK));
            }

        });

        return root;
    }

    private void runAction(boolean firstTime) {
        ActionsModel action = withCompletedVariableActionList.get(actionRunCounter);
        Map<String, String> actionExtra = Utils.getInitialVariableData(getContext(), action.getActionId()).second;

        HoverParameters.Builder builder = new HoverParameters.Builder(getActivity());
        builder.request(action.getActionId());
        builder.setEnvironment(Apis.getCurrentEnv());
        builder.style(R.style.myHoverTheme);
//        builder.initialProcessingMessage(getResources().getString(R.string.transaction_coming_up));
//        builder.finalMsgDisplayTime(0);

        if(actionRunCounter != withCompletedVariableActionList.size()-1) {
            builder.finalMsgDisplayTime(0);
        }
        else builder.finalMsgDisplayTime(30000);

        assert  actionExtra !=null;
        for(String key : actionExtra.keySet()) {
            builder.extra(key, actionExtra.get(key));
        }
        if(firstTime) actionRunCounter = actionRunCounter + 1;
        Intent i = builder.buildIntent();
        startActivityForResult(i, TEST_ALL_RESULT);

        if(firstTime) ApplicationInstance.setAllowSkippedActionsToRun(false);
    }


    private void setupViews() {
        actionsViewModel.getText().observe(getViewLifecycleOwner(), filterStatus-> {
            switch (filterStatus) {
                case FILTER_OFF:
                    filterText.setTextColor(getResources().getColor(R.color.colorHoverWhite));
                    filterText.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                    break;

                case FILTER_ON:
                    filterText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    filterText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_purple_24dp, 0,0,0);
                    filterText.setCompoundDrawablePadding(8);
                    break;
            }
        });

        actionsViewModel.loadActionsObs().observe(getViewLifecycleOwner(), fullActionResult -> {
            switch (fullActionResult.getActionEnum()){
                case LOADING:
                    if (homeActionsRecyclerView.getVisibility() == View.VISIBLE)
                        homeActionsRecyclerView.setVisibility(View.GONE);
                    if (emptyInfoLayout.getVisibility() == View.VISIBLE) {
                        emptyInfoLayout.setVisibility(View.GONE);
                        emptyStateView.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    break;

                case EMPTY:
                    if(homeActionsRecyclerView.getVisibility() == View.VISIBLE)
                        homeActionsRecyclerView.setVisibility(View.GONE);
                    if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
                    emptyInfoLayout.setVisibility(View.VISIBLE);
                    emptyStateView.setVisibility(View.VISIBLE);

                    emptyTitle.setText(getResources().getString(R.string.no_actions_yet));
                    emptySubtitle.setText(getResources().getString(R.string.no_actions_desc));

                    break;

                case HAS_DATA:
                    if(emptyInfoLayout.getVisibility() == View.VISIBLE) {
                        emptyInfoLayout.setVisibility(View.GONE);
                        emptyStateView.setVisibility(View.GONE);
                    }
                    if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
                    if(homeActionsRecyclerView.getVisibility() != View.VISIBLE) homeActionsRecyclerView.setVisibility(View.VISIBLE);
                    rawRunnableModelList = fullActionResult.getActionsModelList();
                    homeActionRecyclerAdapter = new HomeActionRecyclerAdapter(fullActionResult.getActionsModelList(), true, this);
                    homeActionsRecyclerView.setAdapter(homeActionRecyclerAdapter);
                    break;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        actionsViewModel.getAllActions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEST_ALL_RESULT) {
            if(actionRunCounter  < withCompletedVariableActionList.size()) {
                runAction(false);
                actionRunCounter = actionRunCounter + 1;
            }
            else if(actionRunCounter == withCompletedVariableActionList.size()) {
                //Important to set runCounter back to zero when completed.
                actionRunCounter = 0;
                //actionsViewModel.getAllActions();
            }
        }
    }



    @Override
    public void customClickListener(Object... data) {
        assert data!=null;
        Intent i = new Intent(getActivity(), ActionDetailsActivity.class);
        View titleView = homeActionRecyclerAdapter.getTitleView();
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), titleView, "action_title");
        i.putExtra(Apis.ACTION_ID, (String) data[0]);
        i.putExtra(Apis.ACTION_TITLE, (String) data[1]);
        i.putExtra(Apis.ACTION_STATUS, (StatusEnums) data[2]);
        startActivity(i, activityOptionsCompat.toBundle());
    }

    @Override
    public void onError(String message) {
        pullToRefresh.setRefreshing(false);
        UIHelper.flashMessage(getContext(), message);
    }

    @Override
    public void onSuccess(ArrayList<HoverAction> actions) {
        pullToRefresh.setRefreshing(false);
        actionsViewModel.getAllActions();
        UIHelper.flashMessage(getContext(), getResources().getString(R.string.refreshed_successfully));
    }
}
