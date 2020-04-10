package com.usehover.testerv2.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.testerv2.api.Apis;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.models.LoginModel;

public class LoginViewModel extends ViewModel {
	private MutableLiveData<LoginModel> modelResult;

	public LoginViewModel() {
		modelResult = new MutableLiveData<>();
		modelResult.setValue(new LoginModel(HomeEnums.DEFAULT, null));
	}

	LiveData<LoginModel> getModelResult() { return modelResult; }

	void doLogin(String email, String password) {
		LoginModel loginModel = new Apis().doLoginWorkManager(email, password);
		modelResult.postValue(loginModel);
	}
}
