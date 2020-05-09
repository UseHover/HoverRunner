package com.usehover.testerv2.ui.parsers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.FullTransactionResult;
import com.usehover.testerv2.models.ParsersInfoModel;

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
    void getParsersInfo(int parserId) {parserModel.postValue(new Apis().getParsersInfoById(parserId));}

    LiveData<FullTransactionResult> loadParsersTransactionsObs() {return parserTransactions; }
    void getParserTransactions(int parserId) {parserTransactions.postValue(new Apis().getTransactionsByParserId(parserId));}
}
