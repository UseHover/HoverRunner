package com.usehover.testerv2.ui.transactions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransactionViewModel extends ViewModel {

private MutableLiveData<String> mText;

public TransactionViewModel() {
	mText = new MutableLiveData<>();
	mText.setValue("This is dashboard fragment");
}

public LiveData<String> getText() {
	return mText;
}
}