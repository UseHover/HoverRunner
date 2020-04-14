package com.usehover.testerv2.ui.parsers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.models.ParsersInfoModel;

public class ParsersViewModel extends ViewModel {
    private MutableLiveData<ParsersInfoModel> parserModel;

    public ParsersViewModel() {
        parserModel = new MutableLiveData<>();
        parserModel.setValue(null);
    }

    LiveData<ParsersInfoModel> loadParserInfoObs() { return  parserModel;}
    void getParsersInfo(String parserId) {parserModel.postValue(new Apis().getParsersInfoById(parserId));}
}
