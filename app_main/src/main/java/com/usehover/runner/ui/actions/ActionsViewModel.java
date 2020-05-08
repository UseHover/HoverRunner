package com.usehover.runner.ui.actions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.runner.ApplicationInstance;
import com.usehover.runner.api.Apis;
import com.usehover.runner.enums.StatusEnums;
import com.usehover.runner.models.FullActionResult;
import com.usehover.runner.enums.HomeEnums;

public class ActionsViewModel extends ViewModel {

    private MutableLiveData<HomeEnums> filterStatus;
    private MutableLiveData<FullActionResult> homeActions;

    public ActionsViewModel() {
        filterStatus = new MutableLiveData<>();
        homeActions =  new MutableLiveData<>();

        filterStatus.setValue(HomeEnums.FILTER_OFF);
        homeActions.setValue(new FullActionResult(StatusEnums.LOADING, null));
    }

    LiveData<HomeEnums> getText() {
        return filterStatus;
    }
    LiveData<FullActionResult> loadActionsObs() {return homeActions;}

    void setFilterOn() {
        filterStatus.postValue(HomeEnums.FILTER_ON);
    }
    void getAllActions() {
        if(ApplicationInstance.getResultFilter_Actions_LOAD().size() == 0) {
            filterStatus.postValue(HomeEnums.FILTER_OFF);
            homeActions.postValue(new Apis().doGetAllActionsWorkManager(false));
        }
        else {
            filterStatus.postValue(HomeEnums.FILTER_ON);
            homeActions.postValue(new FullActionResult(StatusEnums.HAS_DATA, ApplicationInstance.getResultFilter_Actions_LOAD()));
        }
    }
}