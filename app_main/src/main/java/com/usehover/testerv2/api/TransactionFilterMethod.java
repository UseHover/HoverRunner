package com.usehover.testerv2.api;


import com.usehover.testerv2.ApplicationInstance;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.TransactionModels;
import com.usehover.testerv2.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class TransactionFilterMethod {
    private List<TransactionModels> filteredTrans(List<TransactionModels> f0, List<TransactionModels> f1, boolean visited) {
        //Where f0 is for filtered actions, and f1 for totalActions
        //If f0 size == 0 it means the previous stages weren't part of the filtering params.
        // Therefore use the total action list to filter current stage.
        if(f0.size() == 0 && !visited) return f1;
        return f0;
    }

    List<TransactionModels> startFilterTransaction(List<ActionsModel> ams, List<TransactionModels> tml) {
        //Search searches by id, or text from the session
        List<ActionsModel> actionsModelList = new ArrayList<>(ams);
        List<TransactionModels> transactionModelList = new ArrayList<>(tml);
        List<TransactionModels> filteredTransactionList = new ArrayList<>();
        boolean filterListHasBeenVisited = false;

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

            for(Iterator<TransactionModels> md= transactionModelList.iterator(); md.hasNext();) {
                TransactionModels model = md.next();

                // STAGE 1: FILTER THROUGH KEYWORDS.
                if(ApplicationInstance.getTransactionSearchText() !=null) {
                    if(!ApplicationInstance.getTransactionSearchText().isEmpty()) {
                        String searchValue = ApplicationInstance.getTransactionSearchText();
                        if (!model.getTransaction_id().contains(searchValue) || !model.getCaption().contains(searchValue)) {
                            md.remove();
                        }
                    }
                }

                // STAGE 2: FILTER THROUGH DATE RANGE
                if(ApplicationInstance.getTransactionDateRange() != null) {
                    long startDate = (long) Utils.nonNullDateRange(ApplicationInstance.getTransactionDateRange().first);
                    long endDate = (long) Utils.nonNullDateRange(ApplicationInstance.getTransactionDateRange().second);
                    if (model.getDateTimeStamp() < startDate || model.getDateTimeStamp() > endDate) {
                        md.remove();
                    }
                }
                // STAGE 3: FILTER THROUGH IF, FAILED IS NOT CHECKED.
                if(!ApplicationInstance.isTransactionStatusFailed()) {
                    if(model.getStatusEnums() == StatusEnums.UNSUCCESSFUL) {
                        md.remove();
                    }
                }

                // STAGE 4: FILTER THROUGH IF, SUCCESS IS NOT CHECKED.
                if(!ApplicationInstance.isTransactionStatusSuccess()) {
                    if(model.getStatusEnums() == StatusEnums.SUCCESS) {
                        md.remove();
                    }
                }

                // STAGE 5: FILTER THROUGH IF, PENDING IS NOT CHECKED.
                if(!ApplicationInstance.isTransactionStatusPending()) {
                    if(model.getStatusEnums() == StatusEnums.PENDING) {
                        md.remove();
                    }
                }
            }
            filteredTransactionList = transactionModelList;
            filterListHasBeenVisited = true;
        }


        if(filterListHasBeenVisited && filteredTransactionList.size() == 0) return filteredTransactionList;
        if(ApplicationInstance.getTransactionActionsSelectedFilter().size() > 0) {
            for(Iterator<ActionsModel> md= actionsModelList.iterator(); md.hasNext();) {
                ActionsModel model = md.next();
                if(!ApplicationInstance.getTransactionActionsSelectedFilter().contains(model.getActionId())) {
                    md.remove();
                }
            }
            List<TransactionModels> toFilterWithTransaction = filteredTrans(filteredTransactionList, transactionModelList, filterListHasBeenVisited);
            return getQualifiedTransactionList(actionsModelList, toFilterWithTransaction);
        }
        else if(ApplicationInstance.getTransactionCountriesFilter().size() > 0 || ApplicationInstance.getTransactionNetworksFilter().size() > 0) {

            for(Iterator<ActionsModel> md= actionsModelList.iterator(); md.hasNext(); ) {
                ActionsModel model = md.next();
                if(ApplicationInstance.getTransactionCountriesFilter().size() > 0) {
                    StringBuilder concatenatedSelectedCountries = new StringBuilder();
                    for(String countryCode : ApplicationInstance.getTransactionCountriesFilter()) {
                        concatenatedSelectedCountries = concatenatedSelectedCountries.append(concatenatedSelectedCountries).append(countryCode);
                    }
                    String allSelectedCountries = concatenatedSelectedCountries.toString();
                    if(!allSelectedCountries.contains(model.getCountry())) {
                        md.remove();
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
                        md.remove();
                    }
                }
            }
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
                mdt.remove();
            }
        }
        ApplicationInstance.setResultFilter_Transactions(tmList);
        return tmList;
    }
}
