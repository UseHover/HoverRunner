package com.usehover.testerv2.ui.transactionDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.TransactionDetailsDataType;
import com.usehover.testerv2.models.TransactionDetailsInfoModels;

import java.util.ArrayList;

public class TransactionDetailsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TransactionDetailsInfoModels>> aboutInfoModels;
    private MutableLiveData<ArrayList<TransactionDetailsInfoModels>> deviceModels;
    private MutableLiveData<ArrayList<TransactionDetailsInfoModels>> debugInfoModels;

    public TransactionDetailsViewModel() {
        aboutInfoModels = new MutableLiveData<>();
        deviceModels = new MutableLiveData<>();
        debugInfoModels = new MutableLiveData<>();

        aboutInfoModels.setValue(null);
        deviceModels.setValue(null);
        debugInfoModels.setValue(null);
    }

    LiveData<ArrayList<TransactionDetailsInfoModels>> loadAboutInfoModelsObs() {return aboutInfoModels; }
    LiveData<ArrayList<TransactionDetailsInfoModels>> loadDeviceModelsObs() {return deviceModels; }
    LiveData<ArrayList<TransactionDetailsInfoModels>> loadDebugInfoModelsObs() {return debugInfoModels; }

    void getAboutInfoModels() {aboutInfoModels.postValue
            (new Apis().getTransactionDetailsInfoById(TransactionDetailsDataType.ABOUT));}

    void getDeviceModels() {aboutInfoModels.postValue
            (new Apis().getTransactionDetailsInfoById(TransactionDetailsDataType.DEVICE));}

    void getDebugInfoModels() {aboutInfoModels.postValue
            (new Apis().getTransactionDetailsInfoById(TransactionDetailsDataType.DEBUG_INFO));}
}
