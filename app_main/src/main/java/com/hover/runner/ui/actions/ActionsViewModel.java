package com.hover.runner.ui.actions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.api.Apis;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.models.FullActionResult;
import com.hover.runner.enums.HomeEnums;
import com.hover.runner.states.ActionState;

public class ActionsViewModel extends ViewModel {

    private MutableLiveData<HomeEnums> filterStatus;
    private MutableLiveData<FullActionResult> homeActions;

    public ActionsViewModel() {
        filterStatus = new MutableLiveData<>();
        homeActions =  new MutableLiveData<>();

        filterStatus.setValue(HomeEnums.FILTER_OFF);
        homeActions.setValue(new FullActionResult(StatusEnums.LOADING, null));
    }

    LiveData<HomeEnums> getText() { return filterStatus; }
    LiveData<FullActionResult> loadActionsObs() {return homeActions;}

    void setFilterOn() {
        filterStatus.postValue(HomeEnums.FILTER_ON);
    }
    void getAllActions() {
        if(new ActionState().isActionsInDefaultState()) {
            filterStatus.postValue(HomeEnums.FILTER_OFF);
            homeActions.postValue(new Apis().doGetAllActionsWorkManager(false));
        }
        else {
            filterStatus.postValue(HomeEnums.FILTER_ON);
            homeActions.postValue(new FullActionResult(StatusEnums.HAS_DATA, ActionState.getResultFilter_Actions_LOAD()));
        }
    }
}