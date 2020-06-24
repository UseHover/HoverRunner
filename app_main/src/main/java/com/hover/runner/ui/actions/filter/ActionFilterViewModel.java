package com.hover.runner.ui.actions.filter;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hover.runner.api.Apis;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.models.FilterDataFullModel;
import com.hover.runner.models.FullActionResult;
import com.hover.runner.models.TransactionModels;

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
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FullActionResult actionResult =new Apis().filterThroughActions(actionsModels, transactionModels);
            filteredActions.postValue(actionResult);
        }, 300);

    }

}
