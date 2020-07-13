package com.hover.runner.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hover.runner.R;
import com.hover.runner.api.Apis;
import com.hover.runner.models.LoadSimModel;
import com.hover.runner.settings.SettingsHelper;

public class SettingsViewModel extends ViewModel {

	private MutableLiveData<Integer> modeType;
	private MutableLiveData<LoadSimModel> loadSimModel;

	public SettingsViewModel() {
		modeType = new MutableLiveData<>();
		loadSimModel = new MutableLiveData<>();

		modeType.setValue(SettingsHelper.getCurrentEnv());
		loadSimModel.setValue(new LoadSimModel("Loading...", "Loading..."));

	}

	LiveData<Integer> loadCurrentModeObs() { return modeType; }
	LiveData<LoadSimModel> loadSimsObs() {return loadSimModel; }
	void getSims() {
		loadSimModel.postValue(new Apis().getSimsOnDevice());
	}
}