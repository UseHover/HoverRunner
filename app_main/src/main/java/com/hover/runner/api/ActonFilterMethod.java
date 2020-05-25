package com.hover.runner.api;

import android.text.TextUtils;
import android.util.Log;

import com.hover.sdk.api.Hover;
import com.hover.runner.ApplicationInstance;
import com.hover.runner.database.ConvertRawDatabaseDataToModels;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.models.TransactionModels;
import com.hover.runner.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ActonFilterMethod {
    private List<TransactionModels> transactionModelList;
    private  List<ActionsModel> actionsModelList;
    private List<ActionsModel> filteredActionList;
    private boolean filterListAsBeenVisited;

    ActonFilterMethod(List<ActionsModel> ams, List<TransactionModels> tml) {
       this.actionsModelList = new ArrayList<>(ams);
        this.transactionModelList = new ArrayList<>(tml);
        this.filteredActionList = new ArrayList<>();
        this.filterListAsBeenVisited = false;
    }

    private List<ActionsModel> filteredActions(List<ActionsModel> f0, List<ActionsModel> f1, boolean visited) {
        //Where f0 is for filtered actions, and f1 for totalActions
        //If f0 size == 0 it means the previous stages weren't part of the filtering params.
        // Therefore use the total action list to filter current stage.
        if(f0.size() == 0 && !visited) return f1;
        return f0;
    }
    List<ActionsModel> startFilterAction() {

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


        // STAGE 1: FILTER THROUGH COUNTRIES IF IT'S INCLUDED IN THE FILTERING PARAMETERS.
        // TIME COMPLEXITY: O(n)

        if(ApplicationInstance.getCountriesFilter().size() > 0 || ApplicationInstance.getNetworksFilter().size() > 0 ||
                ApplicationInstance.isWithParsers() || ApplicationInstance.getActionSearchText() !=null) {

            for(Iterator<ActionsModel> md= actionsModelList.iterator(); md.hasNext();) {
                ActionsModel model = md.next();
               filterThroughCountries(model, md);
               filterThroughNetworks(model, md);
               filterThroughActionSearchText(model, md);
               filterIfItHasParsers(model, md);

            }
            filteredActionList = actionsModelList;
            filterListAsBeenVisited = true;
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
            filteredActionList = shortListedTransactionsNonDuplicate();
            filterListAsBeenVisited = true;
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
            filterThroughDateRange(shortListedTransactions, shortListedTransactionActionId);

            //FILTER THROUGH CATEGORIES, PENDING, FAILED AND SUCCESSFUL STATUS
            filterTransactionsBasedOnCategoryAndRanStatus(shortListedTransactions, shortListedTransactionActionId);


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
        filterThroughIfSimIsPresent();


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


    private void filterThroughCountries(ActionsModel model, Iterator<ActionsModel> md) {
        if(ApplicationInstance.getCountriesFilter().size() > 0) {
            StringBuilder concatenatedSelectedCountries = new StringBuilder();
            for(String countryCode : ApplicationInstance.getCountriesFilter()) {
                concatenatedSelectedCountries = concatenatedSelectedCountries.append(concatenatedSelectedCountries).append(countryCode);
            }
            String allSelectedCountries = concatenatedSelectedCountries.toString();
            if(!allSelectedCountries.contains(model.getCountry())) {
                md.remove();
            }
        }
    }

    private void filterThroughNetworks(ActionsModel model, Iterator<ActionsModel> md) {
        if(ApplicationInstance.getNetworksFilter().size() > 0) {
            String[] networkNames = new Apis().convertNetworkNamesToStringArray(model.getNetwork_name());
            boolean toRemove = true;
            for(String network: networkNames) {
                if (ApplicationInstance.getNetworksFilter().contains(network)) {
                    toRemove = false;
                    break;
                }
            }

            if(toRemove) {
                md.remove();
            }
        }
    }

    private void filterIfItHasParsers(ActionsModel model, Iterator<ActionsModel> md) {
        if(ApplicationInstance.isWithParsers()) {
            if(!new ConvertRawDatabaseDataToModels().doesActionHasParsers(model.getActionId()))
               try{ md.remove();}catch (Exception ignored){}
        }
    }

    private void filterThroughActionSearchText(ActionsModel model, Iterator<ActionsModel> md) {
        if(ApplicationInstance.getActionSearchText() !=null) {
            if(TextUtils.getTrimmedLength(ApplicationInstance.getActionSearchText()) > 0) {
                if(!model.getActionTitle().toLowerCase().contains(ApplicationInstance.getActionSearchText().toLowerCase())) {
                    md.remove();
                }
            }
        }
    }

    private List<ActionsModel> shortListedTransactionsNonDuplicate() {
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
        return newTempList;
    }

    private void filterTransactionsBasedOnCategoryAndRanStatus(ArrayList<TransactionModels> shortListedTransactions, ArrayList<String> shortListedTransactionActionId) {
        for (Iterator<TransactionModels> ts = shortListedTransactions.iterator(); ts.hasNext(); ) {
            // STAGE 7: FILTER THROUGH CATEGORIES, IF ITS IN THE PARAMETER
            TransactionModels transaction = ts.next();
            if (ApplicationInstance.getCategoryFilter().size() > 0) {
                if (!ApplicationInstance.getCategoryFilter().contains(transaction.getCategory())) {
                    ts.remove();
                    shortListedTransactionActionId.remove(transaction.getActionId());
                }
            }

            // STAGE 8: REMOVE ACTION ID IF IT WAS SUCCESSFUL
            if (!ApplicationInstance.isStatusSuccess()) {
                if (transaction.getStatusEnums() == StatusEnums.SUCCESS) {
                    ts.remove();
                    shortListedTransactionActionId.remove(transaction.getActionId());
                }
            }

            //STAGE 9: REMOVE ACTION ID IF IT IS PENDING
            if (!ApplicationInstance.isStatusPending()) {
                if (transaction.getStatusEnums() == StatusEnums.PENDING) {
                    ts.remove();
                    shortListedTransactionActionId.remove(transaction.getActionId());
                }
            }

            //STAGE 9: REMOVE ACTION ID IF IT WAS UNSUCCESSFUL
            if (!ApplicationInstance.isStatusFailed()) {
                if (transaction.getStatusEnums() == StatusEnums.UNSUCCESSFUL) {
                    ts.remove();
                    shortListedTransactionActionId.remove(transaction.getActionId());
                }
            }
        }
    }

    private void filterThroughIfSimIsPresent(){
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
    }

    private void filterThroughDateRange(ArrayList<TransactionModels> shortListedTransactions, ArrayList<String> shortListedTransactionActionId) {
        if (ApplicationInstance.getDateRange() !=null) {
            long startDate = (long) Utils.nonNullDateRange(ApplicationInstance.getDateRange().first);
            long endDate = (long) Utils.nonNullDateRange(ApplicationInstance.getDateRange().second);
            for(Iterator<TransactionModels> ts= shortListedTransactions.iterator(); ts.hasNext();) {
                TransactionModels transaction = ts.next();
                if (transaction.getDateTimeStamp() < startDate || transaction.getDateTimeStamp() > endDate) {
                    ts.remove();
                    shortListedTransactionActionId.remove(transaction.getActionId());
                }
            }
        }
    }

}
