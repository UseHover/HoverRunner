package com.usehover.testerv2.ui.transactions.filter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.FilterDataFullModel;
import com.usehover.testerv2.models.FullTransactionResult;
import com.usehover.testerv2.models.TransactionModels;

import java.util.List;

public class TransactionFilterViewModel extends ViewModel {
    private MutableLiveData<FullTransactionResult> filteredTransactions;
    private MutableLiveData<FilterDataFullModel> initialFullData;

    public TransactionFilterViewModel() {
        initialFullData = new MutableLiveData<>();
        filteredTransactions =  new MutableLiveData<>();

        FilterDataFullModel filterDataFullModel = new FilterDataFullModel();
        filterDataFullModel.setActionEnum(StatusEnums.LOADING);
        initialFullData.setValue(filterDataFullModel);
        filteredTransactions.setValue(new FullTransactionResult(StatusEnums.LOADING, null));
    }

    LiveData<FilterDataFullModel> loadAllDataObs() { return  initialFullData;}
    LiveData<FullTransactionResult> loadTransactionsObs() {return filteredTransactions;}


    void getFullDataFirst() { initialFullData.postValue(new Apis().doGetDataForActionFilter());}
    void getOrReloadFilterTransactions(List<ActionsModel> actionsModels, List<TransactionModels> transactionModels) {

        filteredTransactions.postValue(new FullTransactionResult(StatusEnums.LOADING, null));

        FullTransactionResult actionResult =new Apis().filterThroughTransactions(actionsModels, transactionModels);
        filteredTransactions.postValue(actionResult);
    }
}
