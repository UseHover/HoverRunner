package com.usehover.testerv2.api;

import com.usehover.testerv2.database.DatabaseCallsToHover;
import com.usehover.testerv2.enums.ModesEnum;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.FullActionResult;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.models.FullTransactionResult;
import com.usehover.testerv2.models.LoadSimModel;
import com.usehover.testerv2.models.LoginModel;
import com.usehover.testerv2.utils.UIHelper;

import java.util.Random;

public class Apis {
	public final static int PROD_ENV = 0, DEBUG_ENV = 1, TEST_ENV = 2;
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

	public LoadSimModel getSimsOnDevice() {
		return new LoadSimModel("MTN NIGERIA", "SAFARICOM KE");
	}
	public int getCurrentTestMode() {
		return new Random().nextInt(3);
	}
	public void updateTestMode(int mode) {

	}

	public void refreshAppData() {

	}
}
