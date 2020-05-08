package com.usehover.runner.models;

import androidx.core.util.Pair;

import com.usehover.runner.enums.StatusEnums;

import java.util.ArrayList;
import java.util.List;

public class FilterDataFullModel {
    private StatusEnums actionEnum;
    private List<ActionsModel> actionsModelList;
    private List<TransactionModels> transactionModelsList;
    private ArrayList<String> allCountries;
    private ArrayList<Pair<String, String>> allNetworks;
    private ArrayList<String> allCategories;

    public StatusEnums getActionEnum() {
        return actionEnum;
    }

    public void setActionEnum(StatusEnums actionEnum) {
        this.actionEnum = actionEnum;
    }

    public List<ActionsModel> getActionsModelList() {
        return actionsModelList;
    }

    public void setActionsModelList(List<ActionsModel> actionsModelList) {
        this.actionsModelList = actionsModelList;
    }

    public List<TransactionModels> getTransactionModelsList() {
        return transactionModelsList;
    }

    public void setTransactionModelsList(List<TransactionModels> transactionModelsList) {
        this.transactionModelsList = transactionModelsList;
    }

    public ArrayList<String> getAllCountries() {
        return allCountries;
    }

    public void setAllCountries(ArrayList<String> allCountries) {
        this.allCountries = allCountries;
    }

    public ArrayList<Pair<String,String>> getAllNetworks() {
        return allNetworks;
    }

    public void setAllNetworks(ArrayList<Pair<String,String>> allNetworks) {
        this.allNetworks = allNetworks;
    }

    public ArrayList<String> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(ArrayList<String> allCategories) {
        this.allCategories = allCategories;
    }


}
