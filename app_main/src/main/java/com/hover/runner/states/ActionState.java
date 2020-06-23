package com.hover.runner.states;

import androidx.core.util.Pair;

import com.hover.runner.models.ActionsModel;

import java.util.ArrayList;
import java.util.List;

public class ActionState {
    private static ArrayList<String>  countriesFilter = new ArrayList<>();
    private static ArrayList<String>  networksFilter = new ArrayList<>();
    private static ArrayList<String>  categoryFilter = new ArrayList<>();
    private static Pair<Long, Long> dateRange;
    private static String actionSearchText = "";
    private static boolean statusFailed = true;
    private static boolean statusNoTrans = true;
    private static boolean statusPending = true;
    private static boolean statusSuccess = true;
    private static boolean withParsers = false;
    private static boolean onlyWithSimPresent = false;
    private static List<ActionsModel> resultFilter_Actions_LOAD = new ArrayList<>();

    public static List<ActionsModel> getResultFilter_Actions_LOAD() {
        return resultFilter_Actions_LOAD;
    }

    public static void setResultFilter_Actions_LOAD(List<ActionsModel> resultFilter_Actions_LOAD) {
        ActionState.resultFilter_Actions_LOAD = resultFilter_Actions_LOAD;
    }

    public static ArrayList<String> getCountriesFilter() {
        return countriesFilter;
    }

    public static void setCountriesFilter(ArrayList<String> countriesFilter) {
        ActionState.countriesFilter = countriesFilter;
    }

    public static ArrayList<String> getNetworksFilter() {
        return networksFilter;
    }

    public static void setNetworksFilter(ArrayList<String> networksFilter) {
        ActionState.networksFilter = networksFilter;
    }

    public static ArrayList<String> getCategoryFilter() {
        return categoryFilter;
    }

    public static void setCategoryFilter(ArrayList<String> categoryFilter) {
        ActionState.categoryFilter = categoryFilter;
    }

    public static Pair<Long, Long> getDateRange() {
        return dateRange;
    }

    public static void setDateRange(Pair<Long, Long> dateRange) {
        ActionState.dateRange = dateRange;
    }

    public static String getActionSearchText() {
        return actionSearchText;
    }

    public static void setActionSearchText(String actionSearchText) {
        ActionState.actionSearchText = actionSearchText;
    }

    public static boolean isStatusFailed() {
        return statusFailed;
    }

    public static void setStatusFailed(boolean statusFailed) {
        ActionState.statusFailed = statusFailed;
    }

    public static boolean isStatusNoTrans() {
        return statusNoTrans;
    }

    public static void setStatusNoTrans(boolean statusNoTrans) {
        ActionState.statusNoTrans = statusNoTrans;
    }

    public static boolean isStatusPending() {
        return statusPending;
    }

    public static void setStatusPending(boolean statusPending) {
        ActionState.statusPending = statusPending;
    }

    public static boolean isStatusSuccess() {
        return statusSuccess;
    }

    public static void setStatusSuccess(boolean statusSuccess) {
        ActionState.statusSuccess = statusSuccess;
    }

    public static boolean isWithParsers() {
        return withParsers;
    }

    public static void setWithParsers(boolean withParsers) {
        ActionState.withParsers = withParsers;
    }

    public static boolean isOnlyWithSimPresent() {
        return onlyWithSimPresent;
    }

    public static void setOnlyWithSimPresent(boolean onlyWithSimPresent) {
        ActionState.onlyWithSimPresent = onlyWithSimPresent;
    }
}
