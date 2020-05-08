package com.usehover.runner.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.runner.R;
import com.usehover.runner.api.Apis;
import com.usehover.runner.models.LoadSimModel;

public class SettingsViewModel extends ViewModel {

	private MutableLiveData<Integer> modeType;
	private MutableLiveData<LoadSimModel> loadSimModel;

	public SettingsViewModel() {
		modeType = new MutableLiveData<>();
		loadSimModel = new MutableLiveData<>();

		modeType.setValue(new Apis().getCurrentTestMode());
		loadSimModel.setValue(new LoadSimModel("Loading...", "Loading..."));

	}

	LiveData<Integer> loadCurrentModeObs() { return modeType; }
	LiveData<LoadSimModel> loadSimsObs() {return loadSimModel; }
	void getSims() {
		loadSimModel.postValue(new Apis().getSimsOnDevice());
	}
	void updateMode(int pos) {
		switch (pos) {
			case R.id.mode_normal: new Apis().updateTestMode(Apis.PROD_ENV);
				break;
			case R.id.mode_debug: new Apis().updateTestMode(Apis.DEBUG_ENV);
				break;
			case R.id.mode_noSim: new Apis().updateTestMode(Apis.TEST_ENV);
				break;
		}
	}

}