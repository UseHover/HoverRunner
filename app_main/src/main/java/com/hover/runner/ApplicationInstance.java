package com.hover.runner;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.core.util.Pair;

import com.hover.runner.models.ActionsModel;
import com.hover.runner.models.TransactionModels;
import com.hover.runner.utils.fonts.FontReplacer;
import com.hover.runner.utils.fonts.Replacer;

import java.util.ArrayList;
import java.util.List;


public class ApplicationInstance extends Application {
	@SuppressLint("StaticFieldLeak")
	private static Context context;

	//For ActionFilters
	private static ArrayList<String> countriesFilter;
	private static ArrayList<String> networksFilter;
	private static ArrayList<String> categoryFilter;
	private static Pair<Long, Long> dateRange;
	private static String actionSearchText;
	private static boolean statusSuccess;
	private static boolean statusPending;
	private static boolean statusFailed;
	private static boolean statusNoTrans;
	private static boolean withParsers;
	private static boolean onlyWithSimPresent;

	//For transactionFilters
	private static ArrayList<String> transactionCountriesFilter;
	private static ArrayList<String> transactionNetworksFilter;
	private static ArrayList<String> transactionActionsSelectedFilter;
	private static Pair<Long, Long> transactionDateRange;
	private static String transactionSearchText;
	private static boolean transactionStatusSuccess;
	private static boolean transactionStatusPending;
	private static boolean transactionStatusFailed;

	//Action filter result;
	private static List<ActionsModel> resultFilter_Actions;
	private static List<ActionsModel> resultFilter_Actions_LOAD;
	private static List<TransactionModels> resultFilter_Transactions;
	private static List<TransactionModels> resultFilter_Transactions_LOAD;

	private static int COLOR_RED, COLOR_YELLOW, COLOR_GREEN;

	@Override
	public void onCreate() {
		super.onCreate();

		Replacer replacer = FontReplacer.Build(getApplicationContext());
		replacer.setDefaultFont("Gibson-Regular.otf");
		replacer.setBoldFont("Gibson-Bold.otf");
		replacer.setItalicFont("Gibson-SemiBoldItalic.otf");
		replacer.setThinFont("Gibson-Light.otf");
		replacer.applyFont();
		context = this;

		transactionActionsSelectedFilter = new ArrayList<>();
		transactionNetworksFilter = new ArrayList<>();
		transactionCountriesFilter = new ArrayList<>();
		transactionSearchText = "";
		transactionStatusSuccess = true;
		transactionStatusPending = true;
		transactionStatusFailed = true;

		countriesFilter = new ArrayList<>();
		networksFilter = new ArrayList<>();
		categoryFilter = new ArrayList<>();
		actionSearchText = "";
		statusFailed = true;
		statusNoTrans = true;
		statusPending = true;
		statusSuccess = true;
		withParsers = false;
		onlyWithSimPresent = false;

		resultFilter_Actions = new ArrayList<>();
		resultFilter_Actions_LOAD = new ArrayList<>();
		resultFilter_Transactions = new ArrayList<>();
		resultFilter_Transactions_LOAD = new ArrayList<>();

		COLOR_RED = getResources().getColor(R.color.colorRed);
				COLOR_YELLOW = getResources().getColor(R.color.colorYellow);
				COLOR_GREEN = getResources().getColor(R.color.colorGreen);
	}

	public static boolean isStatusSuccess() {
		return statusSuccess;
	}

	public static void setStatusSuccess(boolean statusSuccess) {
		ApplicationInstance.statusSuccess = statusSuccess;
	}

	public static boolean isStatusPending() {
		return statusPending;
	}

	public static void setStatusPending(boolean statusPending) {
		ApplicationInstance.statusPending = statusPending;
	}

	public static boolean isStatusFailed() {
		return statusFailed;
	}

	public static void setStatusFailed(boolean statusFailed) {
		ApplicationInstance.statusFailed = statusFailed;
	}

	public static boolean isStatusNoTrans() {
		return statusNoTrans;
	}

	public static void setStatusNoTrans(boolean statusNoTrans) {
		ApplicationInstance.statusNoTrans = statusNoTrans;
	}

	public static boolean isWithParsers() {
		return withParsers;
	}

	public static void setWithParsers(boolean withParsers) {
		ApplicationInstance.withParsers = withParsers;
	}

	public static boolean isOnlyWithSimPresent() {
		return onlyWithSimPresent;
	}

	public static void setOnlyWithSimPresent(boolean onlyWithSimPresent) {
		ApplicationInstance.onlyWithSimPresent = onlyWithSimPresent;
	}

	public static Pair<Long, Long> getDateRange() {
		return dateRange;
	}

	public static void setDateRange(Pair<Long, Long> dateRange) {
		ApplicationInstance.dateRange = dateRange;
	}

	public static String getActionSearchText() {
		return actionSearchText;
	}

	public static void setActionSearchText(String actionSearchText) {
		ApplicationInstance.actionSearchText = actionSearchText;
	}

	public static ArrayList<String> getCountriesFilter() {
		return countriesFilter;
	}

	public static void setCountriesFilter(ArrayList<String> countriesFilter) {
		ApplicationInstance.countriesFilter = countriesFilter;
	}

	public static ArrayList<String> getNetworksFilter() {
		return networksFilter;
	}

	public static void setNetworksFilter(ArrayList<String> networksFilter) {
		ApplicationInstance.networksFilter = networksFilter;
	}

	public static ArrayList<String> getCategoryFilter() {
		return categoryFilter;
	}

	public static void setCategoryFilter(ArrayList<String> categoryFilter) {
		ApplicationInstance.categoryFilter = categoryFilter;
	}

	public static ArrayList<String> getTransactionCountriesFilter() {
		return transactionCountriesFilter;
	}

	public static void setTransactionCountriesFilter(ArrayList<String> transactionCountriesFilter) {
		ApplicationInstance.transactionCountriesFilter = transactionCountriesFilter;
	}

	public static ArrayList<String> getTransactionNetworksFilter() {
		return transactionNetworksFilter;
	}

	public static void setTransactionNetworksFilter(ArrayList<String> transactionNetworksFilter) {
		ApplicationInstance.transactionNetworksFilter = transactionNetworksFilter;
	}

	public static ArrayList<String> getTransactionActionsSelectedFilter() {
		return transactionActionsSelectedFilter;
	}

	public static void setTransactionActionsSelectedFilter(ArrayList<String> transactionActionsSelectedFilter) {
		ApplicationInstance.transactionActionsSelectedFilter = transactionActionsSelectedFilter;
	}

	public static Pair<Long, Long> getTransactionDateRange() {
		return transactionDateRange;
	}

	public static void setTransactionDateRange(Pair<Long, Long> transactionDateRange) {
		ApplicationInstance.transactionDateRange = transactionDateRange;
	}

	public static String getTransactionSearchText() {
		return transactionSearchText;
	}

	public static void setTransactionSearchText(String transactionSearchText) {
		ApplicationInstance.transactionSearchText = transactionSearchText;
	}

	public static boolean isTransactionStatusSuccess() {
		return transactionStatusSuccess;
	}

	public static void setTransactionStatusSuccess(boolean transactionStatusSuccess) {
		ApplicationInstance.transactionStatusSuccess = transactionStatusSuccess;
	}

	public static boolean isTransactionStatusPending() {
		return transactionStatusPending;
	}

	public static void setTransactionStatusPending(boolean transactionStatusPending) {
		ApplicationInstance.transactionStatusPending = transactionStatusPending;
	}

	public static boolean isTransactionStatusFailed() {
		return transactionStatusFailed;
	}

	public static void setTransactionStatusFailed(boolean transactionStatusFailed) {
		ApplicationInstance.transactionStatusFailed = transactionStatusFailed;
	}

	public static List<ActionsModel> getResultFilter_Actions() {
		return resultFilter_Actions;
	}

	public static void setResultFilter_Actions(List<ActionsModel> resultFilter_Actions) {
		ApplicationInstance.resultFilter_Actions = resultFilter_Actions;
	}

	public static List<ActionsModel> getResultFilter_Actions_LOAD() {
		return resultFilter_Actions_LOAD;
	}

	public static void setResultFilter_Actions_LOAD(List<ActionsModel> resultFilter_Actions_LOAD) {
		ApplicationInstance.resultFilter_Actions_LOAD = resultFilter_Actions_LOAD;
	}

	public static List<TransactionModels> getResultFilter_Transactions() {
		return resultFilter_Transactions;
	}

	public static void setResultFilter_Transactions(List<TransactionModels> resultFilter_Transactions) {
		ApplicationInstance.resultFilter_Transactions = resultFilter_Transactions;
	}

	public static List<TransactionModels> getResultFilter_Transactions_LOAD() {
		return resultFilter_Transactions_LOAD;
	}

	public static void setResultFilter_Transactions_LOAD(List<TransactionModels> resultFilter_Transactions_LOAD) {
		ApplicationInstance.resultFilter_Transactions_LOAD = resultFilter_Transactions_LOAD;
	}

	public static int getColorRed() {
		return COLOR_RED;
	}

	public static int getColorYellow() {
		return COLOR_YELLOW;
	}

	public static int getColorGreen() {
		return COLOR_GREEN;
	}

	public static Context getContext() {
		return context;
	}
}
