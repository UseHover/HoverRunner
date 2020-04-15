package com.usehover.testerv2.api;

import com.usehover.testerv2.database.DatabaseCallsToHover;
import com.usehover.testerv2.enums.PassageEnum;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.enums.TransactionDetailsDataType;
import com.usehover.testerv2.models.ActionDetailsModels;
import com.usehover.testerv2.models.FullActionResult;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.models.FullTransactionResult;
import com.usehover.testerv2.models.LoadSimModel;
import com.usehover.testerv2.models.LoginModel;
import com.usehover.testerv2.models.ParsersInfoModel;
import com.usehover.testerv2.models.TransactionDetailsInfoModels;
import com.usehover.testerv2.models.TransactionDetailsMessagesModel;
import com.usehover.testerv2.utils.UIHelper;
import com.usehover.testerv2.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public class Apis {
	public final static int PROD_ENV = 0, DEBUG_ENV = 1, TEST_ENV = 2;
	private static final String INVALID_EMAIL = "Invalid email format, please enter correct email and try again";
	private static final String INVALID_PASSWORD = "Invalid password format. Ensure password has a minimum of 5 letters with no space";
	public static final String NO_NETWORK = "Internet connection not found";

	public static final String ACTION_ID = "action_id";
	public static final String ACTION_TITLE = "action_title";
	public static final String ACTION_STATUS = "actionStatus";

	public static final String TRANS_ID = "trans_id";
	public static final String TRANS_DATE = "trans_date";
	public static final String TRANS_STATUS = "trans_status";


	public LoginModel doLoginWorkManager(String email, String password) {
		if(!Utils.validateEmail(email)) return new LoginModel(HomeEnums.ERROR_EMAIL, INVALID_EMAIL);
		if(!Utils.validatePassword(password)) return new LoginModel(HomeEnums.ERROR_PASSWORD, INVALID_PASSWORD);

		return new LoginModel(HomeEnums.SUCCESS, "Login successful");
	}

	public FullActionResult doGetAllActionsWorkManager() {
		return new FullActionResult(StatusEnums.HAS_DATA, new DatabaseCallsToHover().getAllActionsFromHover());
	}

	public FullTransactionResult doGetAllTransactionsWorkManager() {
		return new FullTransactionResult(StatusEnums.HAS_DATA, new DatabaseCallsToHover().getAllTransactionsFromHover());
	}

	public ActionDetailsModels doGetSpecificActionDetailsById(String actionId){
	return new DatabaseCallsToHover().getActionDetailsById(actionId);
	}

	public FullTransactionResult doGetTransactionsByActionIdWorkManager(String actionId) {
		return new FullTransactionResult(StatusEnums.HAS_DATA, new DatabaseCallsToHover().getTransactionByActionIdFromHover(actionId));
	}

	public ParsersInfoModel getParsersInfoById(String parserId) {
		return new DatabaseCallsToHover().getParserInfoByIdFromHover(parserId);
	}

	public FullTransactionResult getTransactionsByParserId(String parserId) {
		return new FullTransactionResult(StatusEnums.HAS_DATA, new DatabaseCallsToHover().getTransactionByParserIdFromHover(parserId));
	}

	public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsInfoById(TransactionDetailsDataType type, String transactionId) {
		return new DatabaseCallsToHover().getTransactionDetailsByIdFromHover(type, transactionId);
	}
	public ArrayList<TransactionDetailsMessagesModel> getMessagesOfTransactionById(String transactionId) {
		String[][] result = new DatabaseCallsToHover().getTransactionMessagesByIdFromHover(transactionId);
		String[] enteredValues = result[0];
		String[] ussdMessages = result[1];
		int largestSize = Math.max(enteredValues.length, ussdMessages.length);
		ArrayList<TransactionDetailsMessagesModel> messagesModels = new ArrayList<>(largestSize);
		for(int i=0; i < largestSize; i++) {
			messagesModels.add(new TransactionDetailsMessagesModel(
					enteredValues[i] != null ? enteredValues[i] : "",
					ussdMessages[i] != null  ? ussdMessages[i]  : ""));
		}
		return messagesModels;
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

	public PassageEnum allowIntoMainActivity() {
		return PassageEnum.REJECT;
	}
}
