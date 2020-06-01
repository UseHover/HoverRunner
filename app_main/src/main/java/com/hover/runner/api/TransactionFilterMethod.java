package com.hover.runner.api;


import com.hover.runner.ApplicationInstance;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.models.TransactionModels;
import com.hover.runner.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class TransactionFilterMethod {
    private List<ActionsModel> actionsModelList;
    private List<TransactionModels> transactionModelList;
    private List<TransactionModels> filteredTransactionList;
    private boolean filterListHasBeenVisited;

   TransactionFilterMethod(List<ActionsModel> ams, List<TransactionModels> tml) {
        this.actionsModelList = new ArrayList<>(ams);
        this.transactionModelList = new ArrayList<>(tml);
        this.filteredTransactionList = new ArrayList<>();
        this.filterListHasBeenVisited = false;
    }

    private List<TransactionModels> filteredTrans(List<TransactionModels> f0, List<TransactionModels> f1, boolean visited) {
        //Where f0 is for filtered actions, and f1 for totalActions
        //If f0 size == 0 it means the previous stages weren't part of the filtering params.
        // Therefore use the total action list to filter current stage.
        if(f0.size() == 0 && !visited) return f1;
        return f0;
    }
    private void removeTransactionItem(Iterator<TransactionModels> md) {
        try{
            md.remove();
        }catch (Exception ignored){};
    }
    private void removeActionItem(Iterator<ActionsModel> md) {
        try{
            md.remove();
        }catch (Exception ignored){};
    }

    List<TransactionModels> startFilterTransaction() {

        //Filter overview algo explained-> USE FOR LOOP ONCE, and remove items dose'nt comply.
        //Search through keyword if it exists in parameter
        //Search through dates if it exists in parameter.
        //Search through status if its not in default value.

        //Then load actions, and search through countries if it exists in parameter.
        //Search through selected network if it exists in parameter
        // If a list of action is selected then override the actions in countries and network, and load selected actions.

        //Then if list action based filter is selected, pick transactions that has selected action ids.
        //Else just load filtered transactions.


        if(ApplicationInstance.getTransactionSearchText() !=null || ApplicationInstance.getTransactionDateRange() != null ||
                !ApplicationInstance.isTransactionStatusFailed() || !ApplicationInstance.isTransactionStatusSuccess() ||
                !ApplicationInstance.isTransactionStatusPending()) {

            filterThroughTextsearchDateRangeAndRanStatus();
            filteredTransactionList = transactionModelList;
            filterListHasBeenVisited = true;
        }


        if(filterListHasBeenVisited && filteredTransactionList.size() == 0) return filteredTransactionList;
        if(ApplicationInstance.getTransactionActionsSelectedFilter().size() > 0) {
            filterThroughTransactionsBySelectedActions();
            List<TransactionModels> toFilterWithTransaction = filteredTrans(filteredTransactionList, transactionModelList, filterListHasBeenVisited);
            return getQualifiedTransactionList(actionsModelList, toFilterWithTransaction);
        }
        else if(ApplicationInstance.getTransactionCountriesFilter().size() > 0 || ApplicationInstance.getTransactionNetworksFilter().size() > 0) {

            filterThroughTransactionsByCountriesAndNetworks();
            List<TransactionModels> toFilterWithTransaction = filteredTrans(filteredTransactionList, transactionModelList, filterListHasBeenVisited);
            return getQualifiedTransactionList(actionsModelList, toFilterWithTransaction);
        }

        else {
            if(filterListHasBeenVisited) {
                ApplicationInstance.setResultFilter_Transactions(filteredTransactionList);
                return filteredTransactionList;
            }
            else return transactionModelList;
        }

    }

    private List<TransactionModels>  getQualifiedTransactionList(List<ActionsModel> amList, List<TransactionModels> tmList) {

        ArrayList<String> actionIdList = new ArrayList<>(amList.size());
        for(ActionsModel model: amList) {
            actionIdList.add(model.getActionId());
        }
        for(Iterator<TransactionModels> mdt= tmList.iterator(); mdt.hasNext(); ) {
            TransactionModels models = mdt.next();
            if(!actionIdList.contains(models.getActionId())) {
                removeTransactionItem(mdt);
            }
        }
        ApplicationInstance.setResultFilter_Transactions(tmList);
        return tmList;
    }

    private void filterThroughTextsearchDateRangeAndRanStatus() {
        for(Iterator<TransactionModels> md= transactionModelList.iterator(); md.hasNext();) {
            TransactionModels model = md.next();

            // STAGE 1: FILTER THROUGH KEYWORDS.
            if(ApplicationInstance.getTransactionSearchText() !=null) {
                if(!ApplicationInstance.getTransactionSearchText().isEmpty()) {
                    String searchValue = ApplicationInstance.getTransactionSearchText();
                    if (!model.getTransaction_id().contains(searchValue) || !model.getCaption().contains(searchValue)) {
                        removeTransactionItem(md);
                    }
                }
            }

            // STAGE 2: FILTER THROUGH DATE RANGE
            if(ApplicationInstance.getTransactionDateRange() != null) {
                long startDate = (long) Utils.nonNullDateRange(ApplicationInstance.getTransactionDateRange().first);
                long endDate = (long) Utils.nonNullDateRange(ApplicationInstance.getTransactionDateRange().second);
                if (model.getDateTimeStamp() < startDate || model.getDateTimeStamp() > endDate) {
                    removeTransactionItem(md);
                }
            }
            // STAGE 3: FILTER THROUGH IF, FAILED IS NOT CHECKED.
            if(!ApplicationInstance.isTransactionStatusFailed()) {
                if(model.getStatusEnums() == StatusEnums.UNSUCCESSFUL) {
                    removeTransactionItem(md);
                }
            }

            // STAGE 4: FILTER THROUGH IF, SUCCESS IS NOT CHECKED.
            if(!ApplicationInstance.isTransactionStatusSuccess()) {
                if(model.getStatusEnums() == StatusEnums.SUCCESS) {
                    removeTransactionItem(md);
                }
            }

            // STAGE 5: FILTER THROUGH IF, PENDING IS NOT CHECKED.
            if(!ApplicationInstance.isTransactionStatusPending()) {
                if(model.getStatusEnums() == StatusEnums.PENDING) {
                    removeTransactionItem(md);
                }
            }
        }
    }

    private void filterThroughTransactionsBySelectedActions() {
        for(Iterator<ActionsModel> md= actionsModelList.iterator(); md.hasNext();) {
            ActionsModel model = md.next();
            if(!ApplicationInstance.getTransactionActionsSelectedFilter().contains(model.getActionId())) {
                removeActionItem(md);
            }
        }
    }

    private void filterThroughTransactionsByCountriesAndNetworks() {
        for(Iterator<ActionsModel> md= actionsModelList.iterator(); md.hasNext(); ) {
            ActionsModel model = md.next();
            if(ApplicationInstance.getTransactionCountriesFilter().size() > 0) {
                StringBuilder concatenatedSelectedCountries = new StringBuilder();
                for(String countryCode : ApplicationInstance.getTransactionCountriesFilter()) {
                    concatenatedSelectedCountries = concatenatedSelectedCountries.append(concatenatedSelectedCountries).append(countryCode);
                }
                String allSelectedCountries = concatenatedSelectedCountries.toString();
                if(!allSelectedCountries.contains(model.getCountry())) {
                    removeActionItem(md);
                }
            }

            if(ApplicationInstance.getTransactionNetworksFilter().size() > 0) {
                String[] networkNames = new Apis().convertNetworkNamesToStringArray(model.getNetwork_name());
                boolean toRemove = true;
                for(String network: networkNames) {
                    if (ApplicationInstance.getTransactionNetworksFilter().contains(network)) {
                        toRemove = false;
                        break;
                    }
                }

                if(toRemove) {
                    removeActionItem(md);
                }
            }
        }
    }
}
