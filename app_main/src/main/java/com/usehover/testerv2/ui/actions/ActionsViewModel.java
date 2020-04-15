package com.usehover.testerv2.ui.actions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.FullActionResult;
import com.usehover.testerv2.enums.HomeEnums;

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
    void getAllActions() { homeActions.postValue(new Apis().doGetAllActionsWorkManager()); }
}