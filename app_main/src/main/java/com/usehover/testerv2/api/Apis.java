package com.usehover.testerv2.api;

import androidx.core.util.Pair;

import com.hover.sdk.api.Hover;
import com.hover.sdk.sims.SimInfo;
import com.usehover.testerv2.ApplicationInstance;
import com.usehover.testerv2.database.DatabaseCallsToHover;
import com.usehover.testerv2.enums.PassageEnum;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.enums.TransactionDetailsDataType;
import com.usehover.testerv2.models.ActionDetailsModels;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.FilterActionsFullModel;
import com.usehover.testerv2.models.FullActionResult;
import com.usehover.testerv2.enums.HomeEnums;
import com.usehover.testerv2.models.FullTransactionResult;
import com.usehover.testerv2.models.LoadSimModel;
import com.usehover.testerv2.models.LoginModel;
import com.usehover.testerv2.models.ParsersInfoModel;
import com.usehover.testerv2.models.SingleFilterInfoModel;
import com.usehover.testerv2.models.TransactionDetailsInfoModels;
import com.usehover.testerv2.models.TransactionDetailsMessagesModel;
import com.usehover.testerv2.models.TransactionModels;
import com.usehover.testerv2.utils.UIHelper;
import com.usehover.testerv2.utils.Utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	public FullActionResult doGetAllActionsWorkManager(boolean withMetaInfo) {
		List<ActionsModel> actionsModelList = new DatabaseCallsToHover().getAllActionsFromHover(withMetaInfo);
		return new FullActionResult(actionsModelList.size() > 0 ?StatusEnums.HAS_DATA : StatusEnums.EMPTY, actionsModelList);
	}

	public FilterActionsFullModel doGetDataForActionFilter() {
		FilterActionsFullModel filterActionsFullModel = new FilterActionsFullModel();

		List<ActionsModel> actionsModelList = new DatabaseCallsToHover().getAllActionsFromHover(true);
		List<TransactionModels> transactionModelsList = new DatabaseCallsToHover().getAllTransactionsFromHover(null);
		ArrayList<String> countryRawList = new ArrayList<>();
		ArrayList<Pair<String,String>> networkRawList = new ArrayList<>();
		ArrayList<String> networkNameTemp = new ArrayList<>();
		ArrayList<String> categoryRawList = new ArrayList<>();


		if(actionsModelList.size() > 0) {
			for(ActionsModel actionsModel: actionsModelList) {
				//Get all countries from all available actions
				if(!countryRawList.contains(actionsModel.getCountry())) countryRawList.add(actionsModel.getCountry());

				//Get all network list from actions
				String networks = actionsModel.getNetwork_name().replace(" or ", ", ");
				String[] networkList = networks.split(", ");
				for(String networkName : networkList) {
					if(!networkNameTemp.contains(networkName)) {
						networkNameTemp.add(networkName);
						networkRawList.add(new Pair<>(networkName, actionsModel.getCountry()));
					}
				}

			}
		}

		if(transactionModelsList.size() > 0) {
			for(TransactionModels transactionModels : transactionModelsList) {
				if(transactionModels.getCategory() !=null) {
					if(!transactionModels.getCategory().isEmpty()) {
						if(!categoryRawList.contains(transactionModels.getCategory())) categoryRawList.add(transactionModels.getCategory());
					}
				}
			}
		}

		filterActionsFullModel.setAllCategories(categoryRawList);
		filterActionsFullModel.setAllCountries(countryRawList);
		filterActionsFullModel.setAllNetworks(networkRawList);
		filterActionsFullModel.setActionsModelList(actionsModelList);
		filterActionsFullModel.setTransactionModelsList(transactionModelsList);
		filterActionsFullModel.setActionEnum(actionsModelList.size() > 0 ? StatusEnums.HAS_DATA : StatusEnums.EMPTY);
		return  filterActionsFullModel;
	}

	public ArrayList<SingleFilterInfoModel> getCountriesForActionFilter(ArrayList<String> countries) {
		ArrayList<SingleFilterInfoModel> filterInfoModels = new ArrayList<>();
		ArrayList<String> mySelectedCountries = ApplicationInstance.getCountriesFilter();
		for(String country : countries) {
			SingleFilterInfoModel singleFilterInfoModel = new SingleFilterInfoModel(country, false);
			if(mySelectedCountries.contains(country)) {
				singleFilterInfoModel.setCheck(true);
				mySelectedCountries.remove(country);
			}
			filterInfoModels.add(singleFilterInfoModel);
		}
		return filterInfoModels;
	}

	public ArrayList<SingleFilterInfoModel> getCategoriesForActionFilter(ArrayList<String> categories) {
		ArrayList<SingleFilterInfoModel> filterInfoModels = new ArrayList<>();
		ArrayList<String> mySelectedCategories = ApplicationInstance.getCategoryFilter();
		for(String category : categories) {
			SingleFilterInfoModel singleFilterInfoModel = new SingleFilterInfoModel(category, false);
			if(mySelectedCategories.contains(category)) {
				singleFilterInfoModel.setCheck(true);
				mySelectedCategories.remove(category);
			}
			filterInfoModels.add(singleFilterInfoModel);
		}
		return filterInfoModels;
	}

	public ArrayList<SingleFilterInfoModel> getNetworksForActionFilter(ArrayList<Pair<String, String>> networkPairList) {
		ArrayList<SingleFilterInfoModel> filterInfoModels = new ArrayList<>();
		ArrayList<String> mySelectedNetworks = ApplicationInstance.getNetworksFilter();

		for(Pair networkPair: networkPairList) {
			assert networkPair.first != null;
			assert networkPair.second != null;
			SingleFilterInfoModel singleFilterInfoModel = new SingleFilterInfoModel(networkPair.first.toString(), networkPair.second.toString(), false);
			if(mySelectedNetworks.contains(networkPair.first.toString())) {
				singleFilterInfoModel.setCheck(true);
				mySelectedNetworks.remove(networkPair.first.toString());
			}
			filterInfoModels.add(singleFilterInfoModel);
		}
		return filterInfoModels;
	}

	public String getSelectedCountriesAsText() {
		StringBuilder text = new StringBuilder();
		int maxSize = 5;
		if(ApplicationInstance.getCountriesFilter().size() < maxSize) maxSize = ApplicationInstance.getCountriesFilter().size();
		for(int i=0; i< maxSize; i++) {
			if(i==0)
				text = new StringBuilder(ApplicationInstance.getCountriesFilter().get(0));
			else
				text.append(", ").append(ApplicationInstance.getCountriesFilter().get(i));
		}
		return text.toString();
	}

	public String getSelectedNetworksAsText() {
		StringBuilder text = new StringBuilder();
		int maxSize = 5;
		if(ApplicationInstance.getNetworksFilter().size() < maxSize) maxSize = ApplicationInstance.getNetworksFilter().size();
		for(int i=0; i< maxSize; i++) {
			if(i==0)
				text = new StringBuilder(ApplicationInstance.getNetworksFilter().get(0));
			else
				text.append(", ").append(ApplicationInstance.getNetworksFilter().get(i));
		}
		return text.toString();
	}

	public String getSelectedCategoriesAsText() {
		StringBuilder text = new StringBuilder();
		int maxSize = 5;
		if(ApplicationInstance.getCategoryFilter().size() < maxSize) maxSize = ApplicationInstance.getCategoryFilter().size();
		for(int i=0; i< maxSize; i++) {
			if(i==0)
				text = new StringBuilder(ApplicationInstance.getCategoryFilter().get(0));
			else
				text.append(", ").append(ApplicationInstance.getCategoryFilter().get(i));
		}
		return text.toString();
	}





	public void resetActionFilterDataset() {
		ApplicationInstance.setOnlyWithSimPresent(false);
		ApplicationInstance.setWithParsers(false);
		ApplicationInstance.setStatusPending(true);
		ApplicationInstance.setStatusFailed(true);
		ApplicationInstance.setStatusNoTrans(true);
		ApplicationInstance.setStatusSuccess(true);
		ApplicationInstance.setActionSearchText("");
		ApplicationInstance.setDateRange(null);
		ApplicationInstance.setCountriesFilter(new ArrayList<>());
		ApplicationInstance.setCategoryFilter(new ArrayList<>());
		ApplicationInstance.setNetworksFilter(new ArrayList<>());
	}
	public FullTransactionResult doGetAllTransactionsWorkManager() {
		List<TransactionModels> transactionModelsList = new DatabaseCallsToHover().getAllTransactionsFromHover(null);
		return new FullTransactionResult(transactionModelsList.size() > 0 ?StatusEnums.HAS_DATA : StatusEnums.EMPTY,transactionModelsList);
	}

	public ActionDetailsModels doGetSpecificActionDetailsById(String actionId){
	return new DatabaseCallsToHover().getActionDetailsById(actionId);
	}

	public FullTransactionResult doGetTransactionsByActionIdWorkManager(String actionId) {
		List<TransactionModels> transactionModelsList = new DatabaseCallsToHover().getTransactionByActionIdFromHover(actionId);
		return new FullTransactionResult(transactionModelsList.size() > 0 ?StatusEnums.HAS_DATA : StatusEnums.EMPTY, transactionModelsList);
	}

	public ParsersInfoModel getParsersInfoById(String parserId) {
		return new DatabaseCallsToHover().getParserInfoByIdFromHover(parserId);
	}

	public FullTransactionResult getTransactionsByParserId(String parserId) {
		List<TransactionModels> transactionModelsList = new DatabaseCallsToHover().getTransactionByParserIdFromHover(parserId);
		return new FullTransactionResult(transactionModelsList.size() > 0 ?StatusEnums.HAS_DATA : StatusEnums.EMPTY, transactionModelsList);
	}

	public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsAboutById(String transactionId) {
		return new DatabaseCallsToHover().getTransactionDetailsAbout(transactionId);
	}
	public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsDebugInfoById(String transactionId) {
		return new DatabaseCallsToHover().getTransactionsDetailsDebug(transactionId);
	}
	public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsDeviceById(String transactionId) {
		return new DatabaseCallsToHover().getTransactionDetailsDevice(transactionId);
	}

	public ArrayList<TransactionDetailsMessagesModel> getMessagesOfTransactionById(String transactionId) {
		String[][] result = new DatabaseCallsToHover().getTransactionMessagesByIdFromHover(transactionId);
		String[] enteredValues = result[0];
		String[] ussdMessages = result[1];
		int largestSize = Math.max(enteredValues.length, ussdMessages.length);
		ArrayList<TransactionDetailsMessagesModel> messagesModels = new ArrayList<>(largestSize);

		//Put in a try and catch to prevent crashing when USSD session reports incorrectly.
		try{
			for(int i=0; i < largestSize; i++) {
				messagesModels.add(new TransactionDetailsMessagesModel(
						enteredValues[i] != null ? enteredValues[i] : "",
						ussdMessages[i] != null  ? ussdMessages[i]  : ""));
			}
		} catch (Exception e) {
			for(int i=0; i < largestSize-1; i++) {
				messagesModels.add(new TransactionDetailsMessagesModel(
						enteredValues[i] != null ? enteredValues[i] : "",
						ussdMessages[i] != null  ? ussdMessages[i]  : ""));
			}
		}

		return messagesModels;
	}



	public LoadSimModel getSimsOnDevice() {
		List<SimInfo> sims = Hover.getPresentSims(ApplicationInstance.getContext());
		String sim1 = "None"; String sim2 = "None";
		if(sims.size() > 0) {
			 sim1 = sims.get(0).getNetworkOperatorName();
		}
		if(sims.size() > 1) {
			sim2 = sims.get(1).getNetworkOperatorName();
		}

		return new LoadSimModel(sim1, sim2);
	}
	public int getCurrentTestMode() { return Utils.getIntFromSharedPref(ApplicationInstance.getContext(), Utils.TESTER_ENV); }
	public void updateTestMode(int mode) { Utils.saveInt(Utils.TESTER_ENV, mode, ApplicationInstance.getContext()); }
	public static int getTestEnvMode() { return  Utils.getIntFromSharedPref(ApplicationInstance.getContext(), Utils.TESTER_ENV);}


	public PassageEnum allowIntoMainActivity() {
		return PassageEnum.REJECT;
	}
}
