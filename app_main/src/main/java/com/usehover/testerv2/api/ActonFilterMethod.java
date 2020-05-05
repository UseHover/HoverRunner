package com.usehover.testerv2.api;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.hover.sdk.api.Hover;
import com.hover.sdk.sims.SimInfo;
import com.usehover.testerv2.ApplicationInstance;
import com.usehover.testerv2.database.DatabaseCallsToHover;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.TransactionModels;
import com.usehover.testerv2.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ActonFilterMethod {
    private List<ActionsModel> filteredActions(List<ActionsModel> f0, List<ActionsModel> f1, boolean visited) {
        //Where f0 is for filtered actions, and f1 for totalActions
        //If f0 size == 0 it means the previous stages weren't part of the filtering params.
        // Therefore use the total action list to filter current stage.
        if(f0.size() == 0 && !visited) return f1;
        return f0;
    }
    List<ActionsModel> startFilterAction(List<ActionsModel> ams, List<TransactionModels> tml) {
        List<ActionsModel> actionsModelList = new ArrayList<>(ams);
        List<TransactionModels> transactionModelList = new ArrayList<>(tml);
        List<ActionsModel> filteredActionList = new ArrayList<>();
        boolean filterListAsBeenVisited = false;

        //Filter overview algo explained->
        //If country list is greater than zero, search through list of countries :countriesFilter;
        //----> Since country list is stored as country-code(e.g NG) it means no two country code can exists.
        //----> To reduce the number of for loops. It will be safe to concatenate all selected country code, and search using if it contains.
        //If network list is greater than zero, search through list of networks: networksFilter;
        //Search if action parsers string length is greater than 4 or not: withParsers.
        //If action search text is not empty, search through action id, action name and rootcode actionSearchText;
        //If date is not equals to null, through greaterOrEquals to stratDate and lessOrEquals to end range of transaction: dateRange;
        //If category list is greater than zero, search through list of categories categoryFilter;
        //Search through transaction id that has boolean value statusSuccess, statusFailed, statusPending and statusNoTrans(If transaction id does not exist);
        //onlyWithSimPresent : get the HNI of the two(or one) sim(s) available and search for hnis that matches.


        //    Log.d("FILTER_THROUGH", "RESULT STARTED");
        // STAGE 1: FILTER THROUGH COUNTRIES IF IT'S INCLUDED IN THE FILTERING PARAMETERS.
        // TIME COMPLEXITY: O(n)
        if(ApplicationInstance.getCountriesFilter().size() > 0) {
            StringBuilder concatenatedSelectedCountries = new StringBuilder();
            for(String countryCode : ApplicationInstance.getCountriesFilter()) {
                concatenatedSelectedCountries = concatenatedSelectedCountries.append(concatenatedSelectedCountries).append(countryCode);
            }
            String allSelectedCountries = concatenatedSelectedCountries.toString();
            Log.d("FILTER THROUGH", "COUNTRIES CONCAT: "+allSelectedCountries);
            for(ActionsModel model: actionsModelList) {
                if(allSelectedCountries.contains(model.getCountry())) {
                    filteredActionList.add(model);
                }
            }
            filterListAsBeenVisited = true;
            //Log.d("FILTER_THROUGH", "PASSED STAGE 1 WITH SIZE: "+filteredActionList.size());
        }

        if(filterListAsBeenVisited && filteredActionList.size() == 0) return filteredActionList;
        // STAGE 2: FILTER THROUGH NETWORKS IF IT'S INCLUDED IN THE FILTERING PARAMETERS.
        // TIME COMPLEXITY: O(n²)
        if(ApplicationInstance.getNetworksFilter().size() > 0) {
            List<ActionsModel> newTempList = new ArrayList<>();
            for(ActionsModel model : filteredActions(filteredActionList, actionsModelList, filterListAsBeenVisited)) {
                String[] networkNames = new Apis().convertNetworkNamesToStringArray(model.getNetwork_name());
                for(String network: networkNames) {
                    if(ApplicationInstance.getNetworksFilter().contains(network)) {
                        newTempList.add(model);
                    }
                }
            }

            //Remove duplicates in newTempList should they occur;


            filteredActionList = (List<ActionsModel>) Utils.removeDuplicatesFromList(newTempList);
            filterListAsBeenVisited = true;
            Log.d("FILTER_THROUGH", "PASSED STAGE 2 WITH ITEMS COUNT: "+filteredActionList.size());
            Log.d("FILTER_THROUGH", "PASSED STAGE 2 WITH ITEMS COUNT AGIN: "+filteredActionList.size());
        }

        if(filterListAsBeenVisited && filteredActionList.size() == 0) return filteredActionList;
        // STAGE 3: FILTER THROUGH PARSERS, IF ITS SELECTED IN THE PARAMETER
        // TIME COMPLEXITY: Seems to be 0(n), but it's actually O(n²). Because:
        // Although there is only for loop, the inner method calls a MYSQL select which is a form of for loop.
        // Default value is unchecked, therefore if isWithParsers, then it's been added into the parameter.
        if(ApplicationInstance.isWithParsers()) {
            ArrayList<ActionsModel> newTempList = new ArrayList<>();
            for(ActionsModel model: filteredActions(filteredActionList, actionsModelList, filterListAsBeenVisited)) {
                if(new DatabaseCallsToHover().doesActionHasParsers(model.getActionId()))
                    newTempList.add(model);
            }
            filteredActionList = newTempList;
            filterListAsBeenVisited = true;
            Log.d("FILTER_THROUGH", "PASSED STAGE 3");

        }

        if(filterListAsBeenVisited && filteredActionList.size() == 0) return filteredActionList;
        // STAGE 4: SEARCH FOR ENTERED VALUE, IN ACTION ID, NAME AND ROOTCODE : IF SEARCH IS PROVIDED
        // TIME COMPLEXITY: O(n²)
        if(ApplicationInstance.getActionSearchText() !=null) {
            if(TextUtils.getTrimmedLength(ApplicationInstance.getActionSearchText()) > 0) {
                ArrayList<ActionsModel> newTempList = new ArrayList<>();
                for(ActionsModel model: filteredActions(filteredActionList, actionsModelList, filterListAsBeenVisited)) {
                    if(model.getActionTitle().contains(ApplicationInstance.getActionSearchText())) {
                        newTempList.add(model);
                    }
                }
                filteredActionList = newTempList;
                filterListAsBeenVisited = true;
                Log.d("FILTER_THROUGH", "PASSED STAGE 4");

            }
        }

        // STAGE 5: FILTER THROUGH TRANSACTION DATA.
        //Transaction model list is by default arranged by most recent: So its safe to shortlist by first appearance.

        //The presence of "No transaction" is a huge factor to consider. It's been checked and unchecked has in effect  based on values of 3 other.
        //If either (one or more in) failed, pending or success is checked and "No transaction is also checked", it basically auto fetches data based on the latter
        //But if failed, pending and success are all unchecked, and only "No transaction is left checked", it means fetch data that has not been run at all irrespective of other data factors.

        //STEP 5:
        // TIME COMPLEXITY: O(n)
        if(filterListAsBeenVisited && filteredActionList.size() == 0) return filteredActionList;
        if(ApplicationInstance.isStatusNoTrans() && !ApplicationInstance.isStatusFailed() &&
                !ApplicationInstance.isStatusPending() && !ApplicationInstance.isStatusSuccess()) {

            // STEP 1: CREATE A SHORTLIST  OF ONLY MOST RECENT TRANSACTION AND SAVE IN NON-DUPLICATE ACTION IDS
            ArrayList<String> shortListedTransactionActionId = new ArrayList<>();

            for(TransactionModels transactionModels : transactionModelList) {
                if(!shortListedTransactionActionId.contains(transactionModels.getActionId())) {
                    shortListedTransactionActionId.add(transactionModels.getActionId());
                }
            }

            List<ActionsModel> newTempList = filteredActions(filteredActionList, actionsModelList, filterListAsBeenVisited);
            for(Iterator<ActionsModel> md= newTempList.iterator(); md.hasNext();) {
                //If it is found in the transaction list that has been previous run, remove it from action list to be displayed
                ActionsModel model = md.next();
                if(shortListedTransactionActionId.contains(model.getActionId())) {
                    md.remove();
                }
            }
            filteredActionList = newTempList;
            filterListAsBeenVisited = true;
            Log.d("FILTER_THROUGH", "PASSED STAGE 5");
        }

        else if(ApplicationInstance.getDateRange() !=null || ApplicationInstance.getCategoryFilter().size() > 0
                || !ApplicationInstance.isStatusFailed() || !ApplicationInstance.isStatusNoTrans()
                || !ApplicationInstance.isStatusPending() || !ApplicationInstance.isStatusSuccess()) {


            // STEP 1: CREATE A SHORTLIST  OF ONLY MOST RECENT TRANSACTION
            // TIME COMPLEXITY: O(n)
            ArrayList<String> shortListedTransactionActionId = new ArrayList<>();
            ArrayList<TransactionModels> shortListedTransactions = new ArrayList<>();

            for(TransactionModels transactionModels : transactionModelList) {
                if(!shortListedTransactionActionId.contains(transactionModels.getActionId())) {
                    shortListedTransactions.add(transactionModels);
                    shortListedTransactionActionId.add(transactionModels.getActionId());
                }
            }


            // MID STAGE NOTICE: START USING THE STRING ARRAY LIST initialized at the main top most THAT HOLDS ACTION ID
            // VERY IMPORTANT TO NOTE: Since we're only only getting actions from a specific date (If date is in parameter)
            // We need to filter through date, and remove from the shortlisted those that are not within the date range.

            // STAGE 6: FILTER FOR DATE RANGE
            // TIME COMPLEXITY: 0(n)
            if (ApplicationInstance.getDateRange() !=null) {
                long startDate = (long) nonNullDateRange(ApplicationInstance.getDateRange().first);
                long endDate = (long) nonNullDateRange(ApplicationInstance.getDateRange().second);
                for(Iterator<TransactionModels> ts= shortListedTransactions.iterator(); ts.hasNext();) {
                    TransactionModels transaction = ts.next();
                    if (transaction.getDateTimeStamp() < startDate || transaction.getDateTimeStamp() > endDate) {
                        ts.remove();
                        shortListedTransactionActionId.remove(transaction.getActionId());
                    }
                }
                Log.d("FILTER_THROUGH", "PASSED STAGE 6");
            }


            for (Iterator<TransactionModels> ts = shortListedTransactions.iterator(); ts.hasNext(); ) {
                // STAGE 7: FILTER THROUGH CATEGORIES, IF ITS IN THE PARAMETER
                TransactionModels transaction = ts.next();
                if (ApplicationInstance.getCategoryFilter().size() > 0) {
                    if (!ApplicationInstance.getCategoryFilter().contains(transaction.getCategory())) {
                        ts.remove();
                        shortListedTransactionActionId.remove(transaction.getActionId());
                    }
                    Log.d("FILTER_THROUGH", "PASSED STAGE 7");
                }

                // STAGE 8: REMOVE ACTION ID IF IT WAS SUCCESSFUL
                if (!ApplicationInstance.isStatusSuccess()) {
                    if (transaction.getStatusEnums() == StatusEnums.SUCCESS) {
                        ts.remove();
                        shortListedTransactionActionId.remove(transaction.getActionId());
                    }
                    Log.d("FILTER_THROUGH", "PASSED STAGE 8");
                }

                //STAGE 9: REMOVE ACTION ID IF IT IS PENDING
                if (!ApplicationInstance.isStatusPending()) {
                    if (transaction.getStatusEnums() == StatusEnums.PENDING) {
                        ts.remove();
                        shortListedTransactionActionId.remove(transaction.getActionId());
                    }
                    Log.d("FILTER_THROUGH", "PASSED STAGE 9");
                }

                //STAGE 9: REMOVE ACTION ID IF IT WAS UNSUCCESSFUL
                if (!ApplicationInstance.isStatusFailed()) {
                    if (transaction.getStatusEnums() == StatusEnums.UNSUCCESSFUL) {
                        ts.remove();
                        shortListedTransactionActionId.remove(transaction.getActionId());
                    }
                    Log.d("FILTER_THROUGH", "PASSED STAGE 10");
                }
            }



            // STAGE 10: NO TRANSACTION IS THIS CASE: MEANS IT HAS NOT YET BE RUN.
            // THEREFORE, IF THIS CHECKBOX IS UNTICKED: IT MEANS TO SHOW ACTIONS THAT MUST HAVE BEEN RAN
            // NO NEED TO PUT (No trans in an if statement, since if it does not exists, it wont be part of the data anyway)
            // TIME COMPLEXITY: O(n)

            List<ActionsModel> newTempList = filteredActions(filteredActionList, actionsModelList, filterListAsBeenVisited);
            for(Iterator<ActionsModel> md= newTempList.iterator(); md.hasNext();) {
                //If this action is not found in the filtered transaction data, remove it.
                ActionsModel model = md.next();
                if(!shortListedTransactionActionId.contains(model.getActionId())) {
                    md.remove();
                }
            }
            filteredActionList = newTempList;
            filterListAsBeenVisited = true;
            Log.d("FILTER_THROUGH", "PASSED STAGE 11");
        }

        if(filterListAsBeenVisited && filteredActionList.size() == 0) return filteredActionList;

        // STAGE 11: CHECK FOR ACTIONS WITH THAT HAS PRESENT HNIS : IF PARAMETERS EXISTS
        // TIME COMPLEXITY: O(n²)
        if(ApplicationInstance.isOnlyWithSimPresent()) {
            List<ActionsModel> newTempList = filteredActions(filteredActionList, actionsModelList, filterListAsBeenVisited);
            for(Iterator<ActionsModel> md= newTempList.iterator(); md.hasNext();) {
                ActionsModel model = md.next();
                if(!Hover.isActionSimPresent(model.getActionId(), ApplicationInstance.getContext())) {
                    md.remove();
                }
            }
            filteredActionList = newTempList;
            filterListAsBeenVisited = true;
        }


        if(filterListAsBeenVisited && filteredActionList.size() == 0) return filteredActionList;
        if(filterListAsBeenVisited) {
            Log.d("FILTER_THROUGH", "FILTER HAS "+filteredActionList.size());
            ApplicationInstance.setResultFilter_Actions(filteredActionList);
            return filteredActionList;
        }
        else {
            Log.d("FILTER_THROUGH", "RESULT HAS DEFAULT "+actionsModelList.size());
            return actionsModelList;
        }

    }
    private Object nonNullDateRange(Object value) {
        if(value == null) return 0;
        else return value;
    }

}
