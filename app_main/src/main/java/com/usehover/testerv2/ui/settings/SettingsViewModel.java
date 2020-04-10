package com.usehover.testerv2.ui.settings;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.R;
import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.ModesEnum;
import com.usehover.testerv2.models.LoadSimModel;

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