package com.usehover.testerv2.ui.actions.filter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.FullActionResult;

public class ActionFilterViewModel extends ViewModel {
    private MutableLiveData<FullActionResult> homeActions;

    public ActionFilterViewModel() {
        homeActions =  new MutableLiveData<>();
        homeActions.setValue(new FullActionResult(StatusEnums.LOADING, null));
    }

    LiveData<FullActionResult> loadActionsObs() {return homeActions;}
    void getAllActions() { homeActions.postValue(new Apis().doGetAllActionsWorkManager(true)); }
}
