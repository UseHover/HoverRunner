package com.usehover.testerv2;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.core.util.Pair;

import com.usehover.testerv2.utils.fonts.FontReplacer;
import com.usehover.testerv2.utils.fonts.Replacer;

import java.util.ArrayList;


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

	public static Context getContext() {
		return context;
	}
}
