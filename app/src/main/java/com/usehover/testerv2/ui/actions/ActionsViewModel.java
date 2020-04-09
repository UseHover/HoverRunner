package com.usehover.testerv2.ui.actions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.ActionEnums;
import com.usehover.testerv2.enums.FullActionResult;
import com.usehover.testerv2.enums.HoverEnums;
import com.usehover.testerv2.models.ActionsModel;

import java.util.List;

public class ActionsViewModel extends ViewModel {

    private MutableLiveData<HoverEnums> filterStatus;
    private MutableLiveData<FullActionResult> homeActions;

    public ActionsViewModel() {
        filterStatus = new MutableLiveData<>();
        homeActions =  new MutableLiveData<>();
        filterStatus.setValue(HoverEnums.FILTER_OFF);
        homeActions.setValue(new FullActionResult(ActionEnums.LOADING, null));
    }

    LiveData<HoverEnums> getText() {
        return filterStatus;
    }
    LiveData<FullActionResult> getActionsList() {return homeActions;}

    void setFilterOn() {
        filterStatus.postValue(HoverEnums.FILTER_ON);
    }
    void getAllActions() { homeActions.postValue(new Apis().doGetAllActionsWorkManager()); }
}