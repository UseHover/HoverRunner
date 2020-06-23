package com.hover.runner.states;

import androidx.core.util.Pair;

import com.hover.runner.models.TransactionModels;

import java.util.ArrayList;
import java.util.List;

public class TransactionState {

    private static ArrayList<String> transactionActionsSelectedFilter = new ArrayList<>();
    private static ArrayList<String> transactionNetworksFilter = new ArrayList<>();
    private static ArrayList<String> transactionCountriesFilter = new ArrayList<>();
    private static String transactionSearchText = "";
    private static boolean transactionStatusSuccess = true;
    private static boolean transactionStatusPending = true;
    private static boolean transactionStatusFailed = true;
    private static Pair<Long, Long> transactionDateRange;
    private static List<TransactionModels> resultFilter_Transactions = new ArrayList<>();
    private static List<TransactionModels> resultFilter_Transactions_LOAD = new ArrayList<>();

    public static List<TransactionModels> getResultFilter_Transactions() {
        return resultFilter_Transactions;
    }

    public static void setResultFilter_Transactions(List<TransactionModels> resultFilter_Transactions) {
        TransactionState.resultFilter_Transactions = resultFilter_Transactions;
    }

    public static List<TransactionModels> getResultFilter_Transactions_LOAD() {
        return resultFilter_Transactions_LOAD;
    }

    public static void setResultFilter_Transactions_LOAD(List<TransactionModels> resultFilter_Transactions_LOAD) {
        TransactionState.resultFilter_Transactions_LOAD = resultFilter_Transactions_LOAD;
    }

    public static ArrayList<String> getTransactionActionsSelectedFilter() {
        return transactionActionsSelectedFilter;
    }

    public static void setTransactionActionsSelectedFilter(ArrayList<String> transactionActionsSelectedFilter) {
        TransactionState.transactionActionsSelectedFilter = transactionActionsSelectedFilter;
    }

    public static ArrayList<String> getTransactionNetworksFilter() {
        return transactionNetworksFilter;
    }

    public static void setTransactionNetworksFilter(ArrayList<String> transactionNetworksFilter) {
        TransactionState.transactionNetworksFilter = transactionNetworksFilter;
    }

    public static ArrayList<String> getTransactionCountriesFilter() {
        return transactionCountriesFilter;
    }

    public static void setTransactionCountriesFilter(ArrayList<String> transactionCountriesFilter) {
        TransactionState.transactionCountriesFilter = transactionCountriesFilter;
    }

    public static String getTransactionSearchText() {
        return transactionSearchText;
    }

    public static void setTransactionSearchText(String transactionSearchText) {
        TransactionState.transactionSearchText = transactionSearchText;
    }

    public static boolean isTransactionStatusSuccess() {
        return transactionStatusSuccess;
    }

    public static void setTransactionStatusSuccess(boolean transactionStatusSuccess) {
        TransactionState.transactionStatusSuccess = transactionStatusSuccess;
    }

    public static boolean isTransactionStatusPending() {
        return transactionStatusPending;
    }

    public static void setTransactionStatusPending(boolean transactionStatusPending) {
        TransactionState.transactionStatusPending = transactionStatusPending;
    }

    public static boolean isTransactionStatusFailed() {
        return transactionStatusFailed;
    }

    public static void setTransactionStatusFailed(boolean transactionStatusFailed) {
        TransactionState.transactionStatusFailed = transactionStatusFailed;
    }

    public static Pair<Long, Long> getTransactionDateRange() {
        return transactionDateRange;
    }

    public static void setTransactionDateRange(Pair<Long, Long> transactionDateRange) {
        TransactionState.transactionDateRange = transactionDateRange;
    }
}
