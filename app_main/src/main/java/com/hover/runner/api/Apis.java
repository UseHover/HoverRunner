package com.hover.runner.api;

import androidx.core.util.Pair;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.R;
import com.hover.runner.states.ActionState;
import com.hover.runner.states.TransactionState;
import com.hover.runner.settings.SettingsHelper;
import com.hover.sdk.api.Hover;
import com.hover.sdk.sims.SimInfo;
import com.hover.runner.database.ConvertRawDatabaseDataToModels;
import com.hover.runner.enums.PassageEnum;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.models.ActionDetailsModels;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.models.FilterDataFullModel;
import com.hover.runner.models.FullActionResult;
import com.hover.runner.enums.HomeEnums;
import com.hover.runner.models.FullTransactionResult;
import com.hover.runner.models.LoadSimModel;
import com.hover.runner.models.LoginModel;
import com.hover.runner.models.ParsersInfoModel;
import com.hover.runner.models.SingleFilterInfoModel;
import com.hover.runner.models.TransactionDetailsInfoModels;
import com.hover.runner.models.TransactionDetailsMessagesModel;
import com.hover.runner.models.TransactionModels;
import com.hover.runner.models.WithSubtitleFilterInfoModel;
import com.hover.runner.utils.Utils;
import com.hover.runner.utils.network.LoginAsyncCaller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Apis {
	public final static int PROD_ENV = 0, DEBUG_ENV = 1, TEST_ENV = 2;
	private static final int MAX_SIZE_FHV = 2;

	public static final String ACTION_ID = "action_id";
	public static final String ACTION_TITLE = "action_title";
	public static final String ACTION_STATUS = "actionStatus";

	public static final String TRANS_ID = "trans_id";
	public static final String TRANS_DATE = "trans_date";
	public static final String TRANS_STATUS = "trans_status";


	public LoginModel doLoginWorkManager(String email, String password) {
		if(!Utils.validateEmail(email)) return new LoginModel(HomeEnums.ERROR_EMAIL, ApplicationInstance.getContext().getString(R.string.INVALID_EMAIL));
		if(!Utils.validatePassword(password)) return new LoginModel(HomeEnums.ERROR_PASSWORD,  ApplicationInstance.getContext().getString(R.string.INVALID_PASSWORD));

		try {
			return new LoginAsyncCaller().execute(email, password).get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String loginAuthFailMessage = "Authentication failed. Wrong email or password";
		return new LoginModel(HomeEnums.ERROR, loginAuthFailMessage);
	}

	public FullActionResult doGetAllActionsWorkManager(boolean withMetaInfo) {
		List<ActionsModel> actionsModelList = new ConvertRawDatabaseDataToModels().getAllActionsFromHover(withMetaInfo);
		return new FullActionResult(actionsModelList.size() > 0 ?StatusEnums.HAS_DATA : StatusEnums.EMPTY, actionsModelList);
	}

	String[] convertNetworkNamesToStringArray(String networkName) {
		String networks = networkName.replace(" or ", ", ");
		return networks.split(", ");
	}
	public FilterDataFullModel doGetDataForActionFilter() {
		FilterDataFullModel filterDataFullModel = new FilterDataFullModel();

		List<ActionsModel> actionsModelList = new ConvertRawDatabaseDataToModels().getAllActionsFromHover(true);
		List<TransactionModels> transactionModelsList = new ConvertRawDatabaseDataToModels().getAllTransactionsFromHover(null);
		ArrayList<String> countryRawList = new ArrayList<>();
		ArrayList<Pair<String,String>> networkRawList = new ArrayList<>();
		ArrayList<String> networkNameTemp = new ArrayList<>();
		ArrayList<String> categoryRawList = new ArrayList<>();


		if(actionsModelList.size() > 0) {
			for(ActionsModel actionsModel: actionsModelList) {
				//Get all countries from all available actions
				if(!countryRawList.contains(actionsModel.getCountry())) countryRawList.add(actionsModel.getCountry());

				//Get all network list from actions
				String[] networkList = convertNetworkNamesToStringArray(actionsModel.getNetwork_name());
				for(String networkName : networkList) {
					if(!networkNameTemp.contains(networkName)) {
						networkNameTemp.add(networkName);
						networkRawList.add(new Pair<>(networkName, actionsModel.getCountry()));
					}
				}

			}
		}

		if (transactionModelsList.size() > 0) {
			for (TransactionModels transactionModels : transactionModelsList) {
				if (transactionModels.getCategory() !=null) {
					if (!transactionModels.getCategory().isEmpty()) {
						if (!categoryRawList.contains(transactionModels.getCategory())) categoryRawList.add(transactionModels.getCategory());
					}
				}
			}
		}

		filterDataFullModel.setAllCategories(categoryRawList);
		filterDataFullModel.setAllCountries(countryRawList);
		filterDataFullModel.setAllNetworks(networkRawList);
		filterDataFullModel.setActionsModelList(actionsModelList);
		filterDataFullModel.setTransactionModelsList(transactionModelsList);
		filterDataFullModel.setActionEnum(actionsModelList.size() > 0 ? StatusEnums.HAS_DATA : StatusEnums.EMPTY);
		return filterDataFullModel;
	}


	public ArrayList<SingleFilterInfoModel> getCategoriesForActionFilter(ArrayList<String> categories) {
		ArrayList<SingleFilterInfoModel> filterInfoModels = new ArrayList<>();
		ArrayList<String> mySelectedCategories = ActionState.getCategoryFilter();
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


	public ArrayList<SingleFilterInfoModel> getCountriesForActionFilter(ArrayList<String> countries) {
		ArrayList<SingleFilterInfoModel> filterInfoModels = new ArrayList<>();
		ArrayList<String> mySelectedCountries = ActionState.getCountriesFilter();
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

	public ArrayList<SingleFilterInfoModel> getNetworksForActionFilter(ArrayList<Pair<String, String>> networkPairList) {
		ArrayList<SingleFilterInfoModel> filterInfoModels = new ArrayList<>();
		ArrayList<String> mySelectedNetworks = ActionState.getNetworksFilter();

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


	public ArrayList<SingleFilterInfoModel> getCountriesForTransactionFilter(ArrayList<String> countries) {
		ArrayList<SingleFilterInfoModel> filterInfoModels = new ArrayList<>();
		ArrayList<String> mySelectedCountries = TransactionState.getTransactionCountriesFilter();
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

	public ArrayList<SingleFilterInfoModel> getNetworksForTransactionFilter(ArrayList<Pair<String, String>> networkPairList) {
		ArrayList<SingleFilterInfoModel> filterInfoModels = new ArrayList<>();
		ArrayList<String> mySelectedNetworks = TransactionState.getTransactionNetworksFilter();

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

	public ArrayList<WithSubtitleFilterInfoModel> getTransactionSelectedActionsFilter(List<ActionsModel> actionModelList) {
		ArrayList<WithSubtitleFilterInfoModel> filterInfoModels = new ArrayList<>();
		ArrayList<String> mySelectedActions = TransactionState.getTransactionActionsSelectedFilter();

		for(ActionsModel actionPair: actionModelList) {
			WithSubtitleFilterInfoModel withSubtitleFilterInfoModel = new WithSubtitleFilterInfoModel(actionPair.getActionId(), actionPair.getActionTitle(), false);
			if(mySelectedActions.contains(withSubtitleFilterInfoModel.getTitle())) {
				withSubtitleFilterInfoModel.setCheck(true);
				mySelectedActions.remove(withSubtitleFilterInfoModel.getTitle());
			}
			filterInfoModels.add(withSubtitleFilterInfoModel);
		}
		return filterInfoModels;
	}

	public String getSelectedCountriesAsText() {
		StringBuilder text = new StringBuilder();
		int maxSize = MAX_SIZE_FHV;
		if(ActionState.getCountriesFilter().size() < maxSize) maxSize = ActionState.getCountriesFilter().size();
		for(int i=0; i< maxSize; i++) {
			if(i==0)
				text = new StringBuilder( countryNameFromCountryCode(ActionState.getCountriesFilter().get(0)));
			else
				text.append(", ").append(countryNameFromCountryCode(ActionState.getCountriesFilter().get(i)));
		}
		return text.toString()+addSuffixToEntryValue(ActionState.getCountriesFilter());
	}

	private String countryNameFromCountryCode(String code) {
		return  new Locale("",code).getDisplayCountry();
	}
	public String getSelectedNetworksAsText() {
		StringBuilder text = new StringBuilder();
		int maxSize = MAX_SIZE_FHV;
		if(ActionState.getNetworksFilter().size() < maxSize) maxSize = ActionState.getNetworksFilter().size();
		for(int i=0; i< maxSize; i++) {
			if(i==0)
				text = new StringBuilder(ActionState.getNetworksFilter().get(0));
			else
				text.append(", ").append(ActionState.getNetworksFilter().get(i));
		}
		return text.toString()+addSuffixToEntryValue(ActionState.getNetworksFilter());
	}

	public String getSelectedCategoriesAsText() {
		StringBuilder text = new StringBuilder();
		int maxSize = MAX_SIZE_FHV;
		if(ActionState.getCategoryFilter().size() < maxSize) maxSize = ActionState.getCategoryFilter().size();
		for(int i=0; i< maxSize; i++) {
			if(i==0)
				text = new StringBuilder(ActionState.getCategoryFilter().get(0));
			else
				text.append(", ").append(ActionState.getCategoryFilter().get(i));
		}
		return text.toString()+addSuffixToEntryValue(ActionState.getCategoryFilter());
	}

	public String getSelectedTransactionCountriesAsText() {
		StringBuilder text = new StringBuilder();
		int maxSize = MAX_SIZE_FHV;
		if(TransactionState.getTransactionCountriesFilter().size() < maxSize) maxSize = TransactionState.getTransactionCountriesFilter().size();
		for(int i=0; i< maxSize; i++) {
			if(i==0)
				text = new StringBuilder(countryNameFromCountryCode(TransactionState.getTransactionCountriesFilter().get(0)));
			else
				text.append(", ").append(countryNameFromCountryCode(TransactionState.getTransactionCountriesFilter().get(i)));
		}


		return text.toString()+addSuffixToEntryValue(TransactionState.getTransactionCountriesFilter());
	}

	public String getSelectedTransactionNetworksAsText() {
		StringBuilder text = new StringBuilder();
		int maxSize = MAX_SIZE_FHV;
		if(TransactionState.getTransactionNetworksFilter().size() < maxSize) maxSize = TransactionState.getTransactionNetworksFilter().size();
		for(int i=0; i< maxSize; i++) {
			if(i==0)
				text = new StringBuilder(TransactionState.getTransactionNetworksFilter().get(0));
			else
				text.append(", ").append(TransactionState.getTransactionNetworksFilter().get(i));
		}

		return text.toString()+addSuffixToEntryValue(TransactionState.getTransactionNetworksFilter());
	}

	public String getSelectedActionsForTransactionAsText() {
		StringBuilder text = new StringBuilder();
		int maxSize = MAX_SIZE_FHV;
		if(TransactionState.getTransactionActionsSelectedFilter().size() < maxSize) maxSize = TransactionState.getTransactionActionsSelectedFilter().size();
		for(int i=0; i< maxSize; i++) {
			if(i==0)
				text = new StringBuilder(TransactionState.getTransactionActionsSelectedFilter().get(0));
			else
				text.append(", ").append(TransactionState.getTransactionActionsSelectedFilter().get(i));
		}
		return text.toString()+addSuffixToEntryValue(TransactionState.getTransactionActionsSelectedFilter());
	}

	private String addSuffixToEntryValue(ArrayList<?> list) {
		String suffix = "";
		if(list.size()>MAX_SIZE_FHV) {
			int remaining = list.size() - MAX_SIZE_FHV;
			String others = remaining == 1 ? "other": "others";
			suffix = String.format(Locale.ENGLISH, " and %d %s", remaining, others);
		}
		return suffix;
	}
	public void resetActionFilterDataset() {
		ActionState.setOnlyWithSimPresent(false);
		ActionState.setWithParsers(false);
		ActionState.setStatusPending(true);
		ActionState.setStatusFailed(true);
		ActionState.setStatusNoTrans(true);
		ActionState.setStatusSuccess(true);
		ActionState.setActionSearchText("");
		ActionState.setDateRange(null);
		ActionState.setCountriesFilter(new ArrayList<>());
		ActionState.setCategoryFilter(new ArrayList<>());
		ActionState.setNetworksFilter(new ArrayList<>());
	}

	public static boolean actionFilterIsInNormalState() {
		return !ActionState.isOnlyWithSimPresent() && !ActionState.isWithParsers() && ActionState.isStatusPending() &&
				ActionState.isStatusFailed() && ActionState.isStatusSuccess() && ActionState.isStatusNoTrans() &&
				ActionState.getActionSearchText().isEmpty() && ActionState.getDateRange() == null &&
				ActionState.getCategoryFilter().isEmpty() && ActionState.getCountriesFilter().isEmpty() && ActionState.getNetworksFilter().isEmpty();
	}

	public static boolean transactionFilterIsInNormalState() {
		return TransactionState.getTransactionActionsSelectedFilter().isEmpty() && TransactionState.getTransactionNetworksFilter().isEmpty() &&
				TransactionState.getTransactionCountriesFilter().isEmpty() && TransactionState.getTransactionDateRange() == null &&
				TransactionState.getTransactionSearchText().isEmpty() && TransactionState.isTransactionStatusFailed() &&
				TransactionState.isTransactionStatusPending() && TransactionState.isTransactionStatusSuccess();
	}

	public void resetTransactionFilterDataset() {
		TransactionState.setTransactionNetworksFilter(new ArrayList<>());
		TransactionState.setTransactionCountriesFilter(new ArrayList<>());
		TransactionState.setTransactionActionsSelectedFilter(new ArrayList<>());
		TransactionState.setTransactionDateRange(null);
		TransactionState.setTransactionSearchText("");
		TransactionState.setTransactionStatusFailed(true);
		TransactionState.setTransactionStatusPending(true);
		TransactionState.setTransactionStatusSuccess(true);
	}
	public FullTransactionResult doGetAllTransactionsWorkManager() {
		List<TransactionModels> transactionModelsList = new ConvertRawDatabaseDataToModels().getAllTransactionsFromHover(null);
		return new FullTransactionResult(transactionModelsList.size() > 0 ?StatusEnums.HAS_DATA : StatusEnums.EMPTY,transactionModelsList);
	}

	public ActionDetailsModels doGetSpecificActionDetailsById(String actionId){
		return new ConvertRawDatabaseDataToModels().getActionDetailsById(actionId);
	}

	public FullTransactionResult doGetTransactionsByActionIdWorkManager(String actionId) {
		List<TransactionModels> transactionModelsList = new ConvertRawDatabaseDataToModels().getTransactionsByActionIdFromHover(actionId);
		return new FullTransactionResult(transactionModelsList.size() > 0 ?StatusEnums.HAS_DATA : StatusEnums.EMPTY, transactionModelsList);
	}

	public ParsersInfoModel getParsersInfoById(int parserId) {
		return new ConvertRawDatabaseDataToModels().getParserInfoByIdFromHover(parserId);
	}

	public FullTransactionResult getTransactionsByParserId(int parserId) {
		List<TransactionModels> transactionModelsList = new ConvertRawDatabaseDataToModels().getTransactionsByParserIdFromHover(parserId);
		return new FullTransactionResult(transactionModelsList.size() > 0 ?StatusEnums.HAS_DATA : StatusEnums.EMPTY, transactionModelsList);
	}

	public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsAboutById(String transactionId) {
		return new ConvertRawDatabaseDataToModels().getTransactionDetailsAbout(transactionId);
	}
	public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsDebugInfoById(String transactionId) {
		return new ConvertRawDatabaseDataToModels().getTransactionsDetailsDebug(transactionId);
	}
	public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsDeviceById(String transactionId) {
		return new ConvertRawDatabaseDataToModels().getTransactionDetailsDevice(transactionId);
	}

	public ArrayList<TransactionDetailsMessagesModel> getMessagesOfTransactionById(String transactionId) {
		String[][] result = new ConvertRawDatabaseDataToModels().getTransactionMessagesByIdFromHover(transactionId);
		String[] enteredValues = result[0];
		String[] ussdMessages = result[1];
		int largestSize = Math.max(enteredValues.length, ussdMessages.length);
		ArrayList<TransactionDetailsMessagesModel> messagesModels = new ArrayList<>();

		//Put in a try and catch to prevent crashing when USSD session reports incorrectly.
		try{
			for(int i=0; i < largestSize; i++) {
				messagesModels.add(new TransactionDetailsMessagesModel(
						enteredValues[i] != null ? enteredValues[i] : "",
						ussdMessages[i] != null  ? ussdMessages[i]  : ""));
			}
		} catch (Exception e) {

			//PUTTING IN ANOTHER TRY AND CATCH TO AVOID ERROR WHEN ON NO-SIM MODE
			try{
				for(int i=0; i < largestSize-1; i++) {
					messagesModels.add(new TransactionDetailsMessagesModel(
							enteredValues[i] != null ? enteredValues[i] : "",
							ussdMessages[i] != null  ? ussdMessages[i]  : ""));
				}
			} catch (Exception ex){
				//USE THIS FOR NO-SIM MESSAGE MODE;
				messagesModels.add(new TransactionDetailsMessagesModel("*ROOT_CODE#", "Test Responses"));
			}


		}

		return messagesModels;
	}



	public LoadSimModel getSimsOnDevice() {
		List<SimInfo> sims = Hover.getPresentSims(ApplicationInstance.getContext());
		String sim1 = "None"; String sim2 = "None";
		if(sims.size() == 0) {
			Hover.updateSimInfo(ApplicationInstance.getContext());
			sims = Hover.getPresentSims(ApplicationInstance.getContext());
		}

		if(sims.size() > 0) {
			sim1 = sims.get(0).getNetworkOperatorName();
		}
		if(sims.size() > 1) {
			sim2 = sims.get(1).getNetworkOperatorName();
		}

		return new LoadSimModel(sim1, sim2);
	}
	public static int getCurrentEnv() { return Utils.getSavedInt(SettingsHelper.ENV, ApplicationInstance.getContext()); }
	public static void setEnv(int mode) { Utils.saveInt(SettingsHelper.ENV, mode, ApplicationInstance.getContext()); }


	public PassageEnum allowIntoMainActivity() {
		return SettingsHelper.getApiKey(ApplicationInstance.getContext()).isEmpty()? PassageEnum.REJECT : PassageEnum.ACCEPT;
	}


	public FullActionResult filterThroughActions(List<ActionsModel> actionsModels, List<TransactionModels> transactionModels) {
		List<ActionsModel> list = new ActonFilterMethod(actionsModels, transactionModels).startFilterAction();
		if(list.size() > 0) return new FullActionResult(StatusEnums.HAS_DATA, list);
		else return new FullActionResult(StatusEnums.EMPTY, list);
	}

	public FullTransactionResult filterThroughTransactions(List<ActionsModel> actionsModels, List<TransactionModels> transactionModels) {
		List<TransactionModels> list = new TransactionFilterMethod(actionsModels, transactionModels).startFilterTransaction();
		if (list.size() > 0) return new FullTransactionResult(StatusEnums.HAS_DATA, list);
		else return new FullTransactionResult(StatusEnums.EMPTY, list);
	}
}
