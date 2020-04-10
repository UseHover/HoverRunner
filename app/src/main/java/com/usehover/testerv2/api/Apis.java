package com.usehover.testerv2.api;

import com.usehover.testerv2.database.DatabaseCallsToHover;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.FullActionResult;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.models.FullTransactionResult;
import com.usehover.testerv2.models.LoginModel;
import com.usehover.testerv2.utils.UIHelper;

public class Apis {

	private static final String INVALID_EMAIL = "Invalid email format";
	private static final String INVALID_PASSWORD = "Invalid password format";

	public LoginModel doLoginWorkManager(String email, String password) {
		if(!UIHelper.validateEmail(email)) return new LoginModel(HomeEnums.ERROR, INVALID_EMAIL);
		if(!UIHelper.validatePassword(password)) return new LoginModel(HomeEnums.ERROR, INVALID_PASSWORD);

		return new LoginModel(HomeEnums.SUCCESS, "Login successful");
	}

	public FullActionResult doGetAllActionsWorkManager() {
		return new FullActionResult(StatusEnums.HAS_DATA, new DatabaseCallsToHover().getAllActionsFromHover());
	}

	public FullTransactionResult doGetAllTransactionsWorkManager() {
		return new FullTransactionResult(StatusEnums.HAS_DATA, new DatabaseCallsToHover().getAllTransactionsFromHover());
	}
}
