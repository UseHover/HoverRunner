package com.usehover.testerv2.database;

import android.os.Build;
import android.util.Log;

import com.hover.sdk.BuildConfig;
import com.hover.sdk.actions.HoverAction;
import com.hover.sdk.api.Hover;
import com.hover.sdk.parsers.HoverParser;
import com.hover.sdk.transactions.Transaction;
import com.usehover.testerv2.ApplicationInstance;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.ActionDetailsModels;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.ParsersInfoModel;
import com.usehover.testerv2.models.StreamlinedStepsModel;
import com.usehover.testerv2.models.TransactionDetailsInfoModels;
import com.usehover.testerv2.models.TransactionModels;
import com.usehover.testerv2.utils.Utils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseCallsToHover {
    private List<Transaction> transactionListByActionId;

    public List<ActionsModel> getAllActionsFromHover() {
        List<HoverAction> actionList = Hover.getAllActions(ApplicationInstance.getContext());
        List<Transaction> transactionList = Hover.getAllTransactions(ApplicationInstance.getContext());

        List<ActionsModel> actionsModelList = new ArrayList<>(actionList.size());
        Map<String, String> actionsWithStatus = new HashMap<>();
        for(Transaction transaction : transactionList) {
            actionsWithStatus.put(transaction.actionId, transaction.status);

        }


        for(HoverAction action : actionList) {

            String status = actionsWithStatus.get(action.id);
            ActionsModel tempModel = new ActionsModel(action.id, action.name, action.rootCode, action.steps,
                    (status == null) ? StatusEnums.NOT_YET_RUN : Utils.getStatusByString(status));
            actionsModelList.add(tempModel);
        }

        return actionsModelList;
    }

    public  List<TransactionModels> getAllTransactionsFromHover() {
        List<Transaction> transactionList = Hover.getAllTransactions(ApplicationInstance.getContext());
        Log.d("SITUATION", "transaction list reported is: "+transactionList.size());
        List<TransactionModels> transactionModelsList = new ArrayList<>(transactionList.size());

        for(Transaction transaction : transactionList) {
            String lastUSSDMessage = "empty";
            try {
                lastUSSDMessage = transaction.ussdMessages.getString(transaction.ussdMessages.length()-1);
            } catch (JSONException ignored) {}
            TransactionModels transactionModels = new TransactionModels(transaction.id, transaction.uuid,
                    Utils.formatDate(transaction.updatedTimestamp),
                    lastUSSDMessage,
                    Utils.getStatusByString(transaction.status));

            transactionModelsList.add(transactionModels);

        }
        return transactionModelsList;
    }

    public ActionDetailsModels getActionDetailsById(String actionId) {
        Log.d("SITUATION", "requested action id is: "+actionId);
        //Putting into try and catch to prevent Runtime errors.
        try {
            transactionListByActionId = Hover.getTransactionByActionId(ApplicationInstance.getContext(), actionId);
        }catch (Exception e) {transactionListByActionId = new ArrayList<>();}

        List<HoverParser> hoverParsersList = new ArrayList<>();
        try {
            hoverParsersList = Hover.getParsersByActionId(ApplicationInstance.getContext(), actionId);
        }catch (Exception ignored) {}
        HoverAction hoverAction = Hover.getActionById(ApplicationInstance.getContext(), actionId);

        StringBuilder parsers = new StringBuilder();
        for(HoverParser hoverParser : hoverParsersList) {
            parsers.append(", ").append(hoverParser.serverId);
        }
        String totalTransaction = "0";
        int successNo = 0;
        int pendingNo = 0;
        int failedNo = 0;
        if(transactionListByActionId.size() > 0) {
            totalTransaction = String.valueOf(transactionListByActionId.size());
            for(Transaction transaction : transactionListByActionId) {
                if(transaction.status.equals(Utils.HOVER_TRANSAC_SUCCEEDED)) successNo = successNo + 1;
                else if(transaction.status.equals(Utils.HOVER_TRANSAC_PENDING)) pendingNo = pendingNo + 1;
                else failedNo = failedNo + 1;
            }
        }
        StreamlinedStepsModel streamlinedStepsModel = Utils.getStreamlinedStepsStepsFromRaw(hoverAction.rootCode, hoverAction.steps);


        ActionDetailsModels actionDetailsModels = new ActionDetailsModels(
                hoverAction.networkName,
                parsers.toString(),
                totalTransaction,
                String.valueOf(successNo),
                String.valueOf(pendingNo),
                String.valueOf(failedNo));
        actionDetailsModels.setStreamlinedStepsModel(streamlinedStepsModel);
        return actionDetailsModels;

    }

    public  List<TransactionModels> getTransactionByActionIdFromHover(String actionId) {
        if(transactionListByActionId == null)
            try{
                transactionListByActionId = Hover.getTransactionByActionId(ApplicationInstance.getContext(), actionId);
            } catch (Exception e) {transactionListByActionId = new ArrayList<>();};

        List<TransactionModels> transactionModelsList = new ArrayList<>(transactionListByActionId.size());
        for(Transaction transaction : transactionListByActionId) {
            String lastUSSDMessage = "empty";
            try {
                lastUSSDMessage = transaction.ussdMessages.getString(transaction.ussdMessages.length()-1);
            } catch (JSONException ignored) {}
            TransactionModels transactionModels = new TransactionModels(transaction.id, transaction.uuid,
                    Utils.formatDate(transaction.updatedTimestamp),
                    lastUSSDMessage,
                    Utils.getStatusByString(transaction.status));

            transactionModelsList.add(transactionModels);

        }
        return transactionModelsList;
    }


    public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsAbout(String transactionId) {
        Transaction transaction = Hover.getTransactionById(ApplicationInstance.getContext(), transactionId);
        HoverAction action = Hover.getActionById(ApplicationInstance.getContext(), transaction.actionId);
        String lastUSSDMessage = "empty";
        try {
            lastUSSDMessage = transaction.ussdMessages.getString(transaction.ussdMessages.length()-1);
        } catch (JSONException ignored) {}
        ArrayList<TransactionDetailsInfoModels> dataTransacArrayList = new ArrayList<>();
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Status", transaction.status,
                Utils.getStatusByString(transaction.status), false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Action", action.name,
                null, true));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("ActionID", action.id,
                null, true));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Time", Utils.formatDate(transaction.updatedTimestamp),
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("TransactionId", transaction.uuid,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Result", lastUSSDMessage,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Category", transaction.category,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Operator", action.networkName,
                null, false));
        return dataTransacArrayList;
    }

    public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsDevice(String transactionId) {
        Transaction transaction = Hover.getTransactionById(ApplicationInstance.getContext(), transactionId);
        HoverAction action = Hover.getActionById(ApplicationInstance.getContext(), transaction.actionId);
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String osVersionName = Build.VERSION.CODENAME;
        ArrayList<TransactionDetailsInfoModels> dataTransacArrayList = new ArrayList<>();
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Testing mode", Utils.envValueToString(transaction.env),
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Device ID", com.hover.sdk.utils.Utils.getDeviceId(ApplicationInstance.getContext()),
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Brand", manufacturer,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Model", model,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Android ver.", osVersionName,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("App ver.", Utils.TESTER_VERSION,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("SDK ver.", BuildConfig.VERSION_NAME,
                null, false));
        return dataTransacArrayList;
    }
    public ArrayList<TransactionDetailsInfoModels> getTransactionsDetailsDebug(String transactionId){
        Transaction transaction = Hover.getTransactionById(ApplicationInstance.getContext(), transactionId);
        HoverAction action = Hover.getActionById(ApplicationInstance.getContext(), transaction.actionId);
        StringBuilder parsers = new StringBuilder();
        try {
            String[] parserList = Utils.convertNormalJSONArrayToStringArray(transaction.matchedParsers);
            for(String string : parserList) {
                parsers.append(", ").append(string);
            }
        } catch (JSONException ignored) {}

        ArrayList<TransactionDetailsInfoModels> dataTransacArrayList = new ArrayList<>();
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Input extras", Utils.nullToString(transaction.input_extras),
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Matched parsers", Utils.nullToString(parsers),
                null, true));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Parsed variables", Utils.nullToString(transaction.parsed_variables),
                null, false));
        return dataTransacArrayList;
    }

    public String[][] getTransactionMessagesByIdFromHover(String transactionId) {
        Transaction transaction = Hover.getTransactionById(ApplicationInstance.getContext(), transactionId);
        HoverAction action = Hover.getActionById(ApplicationInstance.getContext(), transaction.actionId);
        String[] rootCode = {action.rootCode};
        String[] tempEnteredValues = {};
        try {
            tempEnteredValues = Utils.convertNormalJSONArrayToStringArray(transaction.enteredValues);
        } catch (JSONException ignored) {}

        int aLen = rootCode.length;
        int bLen = tempEnteredValues.length;
        String[] enteredValues = new String[aLen + bLen];
        System.arraycopy(rootCode, 0, enteredValues , 0, aLen);
        System.arraycopy(tempEnteredValues, 0, enteredValues , aLen, bLen);

        String[] ussdMessages = {};
        try {
            ussdMessages = Utils.convertNormalJSONArrayToStringArray(transaction.ussdMessages);
        } catch (JSONException ignored) {}
        return new String[][]{enteredValues, ussdMessages};
    }

    public ParsersInfoModel getParserInfoByIdFromHover(String parserId) {
        HoverParser hoverParser = Hover.getParserByParserId(ApplicationInstance.getContext(), parserId);
        HoverAction action = Hover.getActionById(ApplicationInstance.getContext(), hoverParser.actionId);

        ParsersInfoModel parsersInfoModel = new ParsersInfoModel();
        parsersInfoModel.setParser_action(action.name);
        parsersInfoModel.setParser_actionID(action.id);
        parsersInfoModel.setParser_category(hoverParser.status);
        parsersInfoModel.setParser_created("None");
        parsersInfoModel.setParser_regex(hoverParser.regex);
        parsersInfoModel.setParser_sender(hoverParser.senderNumber);
        parsersInfoModel.setStatusEnums(Utils.getStatusByString(hoverParser.status));
        parsersInfoModel.setParser_type(action.transportType);

        return parsersInfoModel;
    }

    public  List<TransactionModels> getTransactionByParserIdFromHover(String parserId) {
        List<Transaction> subList = Hover.getTransactionByParserId(ApplicationInstance.getContext(),parserId);
        List<TransactionModels> transactionModelsList = new ArrayList<>(subList.size());
        for(Transaction transaction :subList) {
            String lastUSSDMessage = "empty";
            try {
                lastUSSDMessage = transaction.ussdMessages.getString(transaction.ussdMessages.length()-1);
            } catch (JSONException ignored) {}

            TransactionModels transactionModels = new TransactionModels(transaction.id, transaction.uuid,
                    Utils.formatDate(transaction.updatedTimestamp),
                    lastUSSDMessage,
                    Utils.getStatusByString(transaction.status));
            transactionModelsList.add(transactionModels);

        }
        return transactionModelsList;
    }
}
