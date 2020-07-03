package com.hover.runner.ui.actions.uncompletedVariables;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.R;
import com.hover.runner.adapters.VariableRecyclerAdapter;
import com.hover.runner.interfaces.VariableEditinterface;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.ui.action_details.ActionDetailsActivity;
import com.hover.runner.utils.UIHelper;
import com.hover.runner.utils.Utils;

import org.json.JSONArray;

import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class UncompletedVariableFragment extends Fragment implements  VariableEditinterface {
    private Timer timer= new Timer();
    private int currentViewPosition = 0;
    private TextView toolText,subtoolText,descTitle,descContent, nextSave;
    private RecyclerView variablesRecyclerView;
    private ActionsModel actionsModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uncompleted_variable_fragment_layout, container, false);
        toolText = view.findViewById(R.id.u_variable_toolbarText);
        subtoolText = view.findViewById(R.id.u_variable_subtoolText);
        descTitle = view.findViewById(R.id.u_variable_statusTitle);
        descContent = view.findViewById(R.id.u_variable_statusDesc);
        nextSave = view.findViewById(R.id.next_save_text_id);
        TextView skip = view.findViewById(R.id.skip_id);

        UIHelper.setTextUnderline(skip, getResources().getString(R.string.skip_text));
        variablesRecyclerView = view.findViewById(R.id.action_variables_recyclerView);


        toolText.setOnClickListener(v-> {
            if(getActivity() !=null)getActivity().finish();
        });
        nextSave.setOnClickListener(v-> {
            int unCompleted = UncompletedVariableActivity.uncompletedVariableActionList.size();
            if(actionsModel!=null && Utils.isActionHasCompletedVariables(actionsModel.getActionId(), getContext())) {
                if (currentViewPosition == unCompleted - 1) {
                    //If it's in the last pos and time to save
                    ApplicationInstance.setAllowSkippedActionsToRun(true);
                    if (getActivity() != null) getActivity().finish();
                } else {
                    //do next action
                    currentViewPosition = currentViewPosition + 1;
                    updateTopNoticeLayoutInfo();
                }
            }
            else {
                UIHelper.showHoverToastV2(getContext(), getContext().getResources().getString(R.string.uncompleted_variable_unfilled));
            }
        });

        skip.setOnClickListener(v->{
            Utils.saveSkippedVariable(getContext(), actionsModel.getActionId());
            int unCompleted = UncompletedVariableActivity.uncompletedVariableActionList.size();
            if(currentViewPosition == unCompleted - 1) {
                //If it's in the last pos, skip and close activity
                ApplicationInstance.setAllowSkippedActionsToRun(true);
                if(getActivity() !=null) getActivity().finish();
            }
            else {
                //skip and move to next action
                currentViewPosition = currentViewPosition +1;
                updateTopNoticeLayoutInfo();
            }

        });
        updateTopNoticeLayoutInfo();
        return view;
    }

    private void updateTopNoticeLayoutInfo() {
        actionsModel = UncompletedVariableActivity.uncompletedVariableActionList.get(currentViewPosition);
        int unCompleted = UncompletedVariableActivity.uncompletedVariableActionList.size();
        String totalUncompleted = String.valueOf(unCompleted);
        String suffixForDesc = "actions are missing information";
        if(unCompleted == 1) suffixForDesc = "action is missing information";

        toolText.setText(actionsModel.getActionId());
        subtoolText.setText(actionsModel.getActionTitle());
        descTitle.setText(String.format(Locale.ENGLISH, "%d %s",unCompleted, suffixForDesc));
        UIHelper.setTextUnderline(nextSave, (currentViewPosition == unCompleted - 1) ? "Save" : "Next");
        descContent.setText(String.format(Locale.getDefault(),"Action %d of %s", currentViewPosition+1, totalUncompleted));

        try{
            variablesRecyclerView.setLayoutManager(UIHelper.setMainLinearManagers(getContext()));
            VariableRecyclerAdapter variableRecyclerAdapter = new VariableRecyclerAdapter(
                    actionsModel.getActionId(),
                    Utils.getStreamlinedStepsStepsFromRaw(actionsModel.getRootCode(), new JSONArray(actionsModel.getJsonArrayToString())),
                    this,
                    Utils.getInitialVariableData(getContext(), actionsModel.getActionId()).second
            );
            variablesRecyclerView.setAdapter(variableRecyclerAdapter);
        }catch (Exception e) {
            UIHelper.showHoverToast(getContext(), getActivity().getCurrentFocus(), getResources().getString(R.string.bad_steps_config));
        }

    }

    @Override
    public void onEditStringChanged(String label, String newValue) {
        timer.cancel();
        timer = new Timer();long DELAY = 1000;
        timer.schedule(new TimerTask() {@Override public void run() {

            if(getContext() !=null){
                Log.d("Saving cache", "label: "+label+",  value: "+newValue+", and actionId is: "+actionsModel.getActionId());
                Utils.saveActionVariable(getContext(), label, newValue, actionsModel.getActionId());
            } }}, DELAY);
    }
}
