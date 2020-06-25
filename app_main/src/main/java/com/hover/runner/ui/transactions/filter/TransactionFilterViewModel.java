package com.hover.runner.ui.transactions.filter;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hover.runner.api.Apis;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.models.FilterDataFullModel;
import com.hover.runner.models.FullTransactionResult;
import com.hover.runner.models.TransactionModels;

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

        new Handler().postDelayed(() -> {
            FullTransactionResult actionResult =new Apis().filterThroughTransactions(actionsModels, transactionModels);
            filteredTransactions.postValue(actionResult);
        }, 300);


    }
}
