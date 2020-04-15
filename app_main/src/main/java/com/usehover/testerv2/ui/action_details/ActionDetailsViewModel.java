package com.usehover.testerv2.ui.action_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.ActionDetailsModels;
import com.usehover.testerv2.models.FullTransactionResult;

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
