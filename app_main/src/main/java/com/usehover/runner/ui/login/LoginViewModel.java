package com.usehover.runner.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usehover.runner.api.Apis;
import com.usehover.runner.enums.HomeEnums;
import com.usehover.runner.models.LoginModel;

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
