package com.usehover.testerv2.ui.action_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.ActionDetailsModels;
import com.usehover.testerv2.models.FullActionResult;

public class ActionDetailsLiveModel extends ViewModel {

    private MutableLiveData<ActionDetailsModels> actionDetailsModel;

    public ActionDetailsLiveModel() {
        actionDetailsModel =  new MutableLiveData<>();
    }

    LiveData<ActionDetailsModels> loadActionDetailsObs() {return actionDetailsModel;}
    void getDetails(String actionId) {actionDetailsModel.postValue(new Apis().doGetSpecificActionDetailsById(actionId));}

}
