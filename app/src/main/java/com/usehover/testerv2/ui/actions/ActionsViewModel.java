package com.usehover.testerv2.ui.actions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActionsViewModel extends ViewModel {

private MutableLiveData<String> mText;

public ActionsViewModel() {
	mText = new MutableLiveData<>();
	mText.setValue("This is home fragment");
}

LiveData<String> getText() {
	return mText;
}
}