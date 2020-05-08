package com.usehover.testerv2.ui.actions.filter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.FilterDataFullModel;
import com.usehover.testerv2.models.FullActionResult;
import com.usehover.testerv2.models.TransactionModels;

import java.util.List;

public class ActionFilterViewModel extends ViewModel {
    private MutableLiveData<FullActionResult> filteredActions;
    private MutableLiveData<FilterDataFullModel> initialFullData;

    public ActionFilterViewModel() {
        initialFullData = new MutableLiveData<>();
        filteredActions =  new MutableLiveData<>();

        FilterDataFullModel filterDataFullModel = new FilterDataFullModel();
        filterDataFullModel.setActionEnum(StatusEnums.LOADING);
        initialFullData.setValue(filterDataFullModel);
        filteredActions.setValue(new FullActionResult(StatusEnums.LOADING, null));
    }

    LiveData<FilterDataFullModel> loadAllDataObs() { return  initialFullData;}
    LiveData<FullActionResult> loadActionsObs() {return filteredActions;}


    void getFullDataFirst() { initialFullData.postValue(new Apis().doGetDataForActionFilter());}
    void getOrReloadFilterActions(List<ActionsModel> actionsModels, List<TransactionModels> transactionModels) {
        filteredActions.postValue(new FullActionResult(StatusEnums.LOADING, null));

        FullActionResult actionResult =new Apis().filterThroughActions(actionsModels, transactionModels);
        filteredActions.postValue(actionResult);
    }

}
