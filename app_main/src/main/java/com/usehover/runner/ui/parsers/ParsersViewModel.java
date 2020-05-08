package com.usehover.runner.ui.parsers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.runner.api.Apis;
import com.usehover.runner.enums.StatusEnums;
import com.usehover.runner.models.FullTransactionResult;
import com.usehover.runner.models.ParsersInfoModel;

public class ParsersViewModel extends ViewModel {
    private MutableLiveData<ParsersInfoModel> parserModel;
    private MutableLiveData<FullTransactionResult> parserTransactions;

    public ParsersViewModel() {
        parserModel = new MutableLiveData<>();
        parserTransactions = new MutableLiveData<>();
        parserModel.setValue(null);
        parserTransactions.setValue(new FullTransactionResult(StatusEnums.LOADING, null));
    }

    LiveData<ParsersInfoModel> loadParserInfoObs() { return  parserModel;}
    void getParsersInfo(String parserId) {parserModel.postValue(new Apis().getParsersInfoById(parserId));}

    LiveData<FullTransactionResult> loadParsersTransactionsObs() {return parserTransactions; }
    void getParserTransactions(String parserId) {parserTransactions.postValue(new Apis().getTransactionsByParserId(parserId));}
}
