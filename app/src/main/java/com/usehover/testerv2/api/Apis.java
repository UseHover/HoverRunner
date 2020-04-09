package com.usehover.testerv2.api;

import com.usehover.testerv2.models.LoginModel;
import com.usehover.testerv2.utils.UIHelper;

public class Apis {

	private static final String INVALID_EMAIL = "Invalid email format";
	private static final String INVALID_PASSWORD = "Invalid password format";

	public LoginModel doLoginWorkManager(String email, String password) {
		if(!UIHelper.validateEmail(email)) return new LoginModel(HoverEnums.LOGIN_ERROR, INVALID_EMAIL);
		if(!UIHelper.validatePassword(password)) return new LoginModel(HoverEnums.LOGIN_ERROR, INVALID_PASSWORD);

		return new LoginModel(HoverEnums.LOGIN_SUCCESS, "Login successful");
	}
}
