package com.usehover.runner.ui.action_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.runner.api.Apis;
import com.usehover.runner.enums.StatusEnums;
import com.usehover.runner.models.ActionDetailsModels;
import com.usehover.runner.models.FullTransactionResult;

public class ActionDetailsViewModel extends ViewModel {

    private MutableLiveData<ActionDetailsModels> actionDetailsModel;
    private MutableLiveData<FullTransactionResult> actionTransactions;

    public ActionDetailsViewModel() {
        actionDetailsModel =  new MutableLiveData<>();
        actionTransactions = new MutableLiveData<>();
        actionTransactions.setValue(new FullTransactionResult(StatusEnums.LOADING, null));
    }

    LiveData<ActionDetailsModels> loadActionDetailsObs() {return actionDetailsModel;}
    LiveData<FullTransactionResult> loadActionTransactionsObs() {return actionTransactions; }
    void getDetails(String actionId) {actionDetailsModel.postValue(new Apis().doGetSpecificActionDetailsById(actionId));}
    void getActionTrans(String actionId) {actionTransactions.postValue(new Apis().doGetTransactionsByActionIdWorkManager(actionId));}

}
