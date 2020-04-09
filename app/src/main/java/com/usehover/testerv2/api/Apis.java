package com.usehover.testerv2.api;

import com.usehover.testerv2.database.DatabaseCallsToHover;
import com.usehover.testerv2.enums.ActionEnums;
import com.usehover.testerv2.enums.FullActionResult;
import com.usehover.testerv2.enums.HoverEnums;
import com.usehover.testerv2.models.LoginModel;
import com.usehover.testerv2.utils.UIHelper;

public class Apis {

	private static final String INVALID_EMAIL = "Invalid email format";
	private static final String INVALID_PASSWORD = "Invalid password format";

	public LoginModel doLoginWorkManager(String email, String password) {
		if(!UIHelper.validateEmail(email)) return new LoginModel(HoverEnums.ERROR, INVALID_EMAIL);
		if(!UIHelper.validatePassword(password)) return new LoginModel(HoverEnums.ERROR, INVALID_PASSWORD);

		return new LoginModel(HoverEnums.SUCCESS, "Login successful");
	}

	public FullActionResult doGetAllActionsWorkManager() {
		return new FullActionResult(ActionEnums.HAS_DATA, new DatabaseCallsToHover().getAllActionsFromHover());
	}
}
