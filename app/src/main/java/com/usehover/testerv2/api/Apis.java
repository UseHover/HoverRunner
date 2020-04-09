package com.usehover.testerv2.api;

import com.usehover.testerv2.models.LoginModel;

public class Apis {
	public LoginModel doLoginWorkManager(String email, String password) {
		return new LoginModel(1, "demoToken");
	}
}
