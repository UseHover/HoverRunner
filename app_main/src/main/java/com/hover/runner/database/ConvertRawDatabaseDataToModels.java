package com.hover.runner.database;

import android.os.Build;
import android.util.Log;

import com.google.android.gms.common.util.ArrayUtils;
import com.hover.runner.BuildConfig;
import com.hover.sdk.actions.HoverAction;
import com.hover.sdk.api.Hover;
import com.hover.sdk.parsers.HoverParser;
import com.hover.sdk.sms.MessageLog;
import com.hover.sdk.transactions.Transaction;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.models.ActionDetailsModels;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.models.ParsersInfoModel;
import com.hover.runner.models.StreamlinedStepsModel;
import com.hover.runner.models.TransactionDetailsInfoModels;
import com.hover.runner.models.TransactionModels;
import com.hover.runner.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertRawDatabaseDataToModels {
    private List<Transaction> transactionListByActionId;
    private DatabaseRepo repo = new DatabaseRepo();

    public List<ActionsModel> getAllActionsFromHover(boolean withMetaInfo) {
        List<HoverAction> actionList = repo.getAllActionsFromHover();
        List<Transaction> transactionList = repo.getAllTransactionsFromHover();

        List<ActionsModel> actionsModelList = new ArrayList<>(actionList.size());
        Map<String, String> actionsWithStatus = new HashMap<>();
        for(Transaction transaction : transactionList) {
            //Only put the last run status.
            if(actionsWithStatus.get(transaction.actionId) == null)
                actionsWithStatus.put(transaction.actionId, transaction.status);
        }


        for(HoverAction action : actionList) {
            String status = actionsWithStatus.get(action.id);
            ActionsModel tempModel = new ActionsModel(action.id, action.name, action.rootCode, action.steps,
                    (status == null) ? StatusEnums.NOT_YET_RUN : Utils.getStatusByString(status));
            if(withMetaInfo) {
                tempModel.setCountry(action.country);
                tempModel.setNetwork_name(action.networkName);
            }
            actionsModelList.add(tempModel);
        }

        return actionsModelList;
    }

    public  List<TransactionModels> getAllTransactionsFromHover(String args) {
        List<Transaction> transactionList = repo.getTransactionsWithArgsFromHover(args);
        List<TransactionModels> transactionModelsList = new ArrayList<>(transactionList.size());

        for(Transaction transaction : transactionList) {
            String lastUSSDMessage = "empty";
            String smsMessage = getLastSMSMessage(transaction.smsHits);
            if(smsMessage != null) {
                lastUSSDMessage = smsMessage;
            }
            else {
                try {
                    lastUSSDMessage = transaction.ussdMessages.getString(transaction.ussdMessages.length()-1);

                } catch (JSONException ignored) {}
            }


            TransactionModels transactionModels = new TransactionModels(transaction.id, transaction.uuid, Utils.formatDate(transaction.updatedTimestamp), lastUSSDMessage, Utils.getStatusByString(transaction.status));
            transactionModels.setDateTimeStamp(transaction.updatedTimestamp);
            transactionModels.setActionId(transaction.actionId);
            transactionModels.setCategory(transaction.category);
            transactionModelsList.add(transactionModels);
        }
        return transactionModelsList;
    }

    public boolean doesActionHasParsers(String actionId) {
        if(repo.getParsersByActionId(actionId) !=null) return repo.getParsersByActionId(actionId).size() > 0;
        else return false;
    }
    public ActionDetailsModels getActionDetailsById(String actionId) {
        //Putting into try and catch to prevent Runtime errors.
        try {
            transactionListByActionId = repo.getTransactionsByActionId(actionId);
        }catch (Exception e) {transactionListByActionId = new ArrayList<>();}

        List<HoverParser> hoverParsersList = new ArrayList<>();
        try {
            hoverParsersList = repo.getParsersByActionId(actionId);
        }catch (Exception ignored) {}
        HoverAction hoverAction = repo.getSingleActionByIdActionId(actionId);

        StringBuilder parsers = new StringBuilder();
        for(HoverParser hoverParser : hoverParsersList) {
            parsers.append(hoverParser.serverId).append(", ");
        }
        String parserString = "";
        if(!parsers.toString().isEmpty())  parserString= parsers.toString().substring(0, parsers.length()-2);
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
                parserString,
                totalTransaction,
                String.valueOf(successNo),
                String.valueOf(pendingNo),
                String.valueOf(failedNo));
        actionDetailsModels.setStreamlinedStepsModel(streamlinedStepsModel);
        return actionDetailsModels;

    }

    public  List<TransactionModels> getTransactionsByActionIdFromHover(String actionId) {
        if(transactionListByActionId == null)
            try{
                transactionListByActionId = repo.getTransactionsByActionId(actionId);
            } catch (Exception e) {transactionListByActionId = new ArrayList<>();}

        List<TransactionModels> transactionModelsList = new ArrayList<>(transactionListByActionId.size());
        for(Transaction transaction : transactionListByActionId) {
            String lastUSSDMessage = "empty";
            String smsMessage = getLastSMSMessage(transaction.smsHits);
            if(smsMessage != null) {
                lastUSSDMessage = smsMessage;
            }
            else {
                try {
                    lastUSSDMessage = transaction.ussdMessages.getString(transaction.ussdMessages.length()-1);

                } catch (JSONException ignored) {}
            }
            TransactionModels transactionModels = new TransactionModels(transaction.id, transaction.uuid,
                    Utils.formatDate(transaction.updatedTimestamp),
                    lastUSSDMessage,
                    Utils.getStatusByString(transaction.status));

            transactionModelsList.add(transactionModels);

        }
        return transactionModelsList;
    }


    public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsAbout(String transactionId) {
        Transaction transaction = repo.getTransactionByTransId(transactionId);
        HoverAction action = repo.getSingleActionByIdActionId(transaction.actionId);

        String lastUSSDMessage = "empty";
        String smsMessage = getLastSMSMessage(transaction.smsHits);
        if(smsMessage != null) {
            lastUSSDMessage = smsMessage;
        }
        else {
            try {
                lastUSSDMessage = transaction.ussdMessages.getString(transaction.ussdMessages.length()-1);

            } catch (JSONException ignored) {}
        }
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
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Category", Utils.nullToString(transaction.category),
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Operator", action.networkName,
                null, false));
        return dataTransacArrayList;
    }

    public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsDevice(String transactionId) {
        Transaction transaction = repo.getTransactionByTransId(transactionId);
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String osVersionName =  String.valueOf(Build.VERSION.SDK_INT);
        ArrayList<TransactionDetailsInfoModels> dataTransacArrayList = new ArrayList<>();
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Testing mode", Utils.envValueToString(transaction.env),
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Device ID", Utils.getDeviceId(ApplicationInstance.getContext()),
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Brand", manufacturer,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Model", model,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Android ver.", "SDK "+osVersionName,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("App ver.", Utils.TESTER_VERSION,
                null, false));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("SDK ver.", BuildConfig.VERSION_NAME,
                null, false));
        return dataTransacArrayList;
    }

    public ArrayList<TransactionDetailsInfoModels> getTransactionsDetailsDebug(String transactionId){
        Transaction transaction = repo.getTransactionByTransId(transactionId);
        StringBuilder parsers = new StringBuilder();
        try {
            String[] parserList = Utils.convertNormalJSONArrayToStringArray(transaction.matchedParsers);
            for(String string : parserList) {
                parsers.append(string).append(", ");
            }

        } catch (JSONException ignored) {}

        ArrayList<TransactionDetailsInfoModels> dataTransacArrayList = new ArrayList<>();
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Input extras", Utils.nullToString(transaction.input_extras),
                null, false));

        String parserString = "";
        if(!parsers.toString().isEmpty())  parserString= parsers.toString().substring(0, parsers.length()-2);

        dataTransacArrayList.add(new TransactionDetailsInfoModels("Matched parsers", Utils.nullToString(parserString),
                null, true));
        dataTransacArrayList.add(new TransactionDetailsInfoModels("Parsed variables", Utils.nullToString(transaction.parsed_variables),
                null, false));
        return dataTransacArrayList;
    }

    public String[][] getTransactionMessagesByIdFromHover(String transactionId) {
        Transaction transaction = repo.getTransactionByTransId(transactionId);
        HoverAction action = repo.getSingleActionByIdActionId(transaction.actionId);
        List<MessageLog> smsMessages = new ArrayList<>();
        try {
            String[] smsUUIDS = Utils.convertNormalJSONArrayToStringArray(transaction.smsHits);
            for(String uuid : smsUUIDS) {
                MessageLog log = repo.getSMSMessageByUUID(uuid);
                smsMessages.add(log);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String[] smsSenderList = new String[smsMessages.size()];
        String[] smsContentList = new String[smsMessages.size()];
        for(int i=0; i<smsMessages.size(); i++) {
            smsSenderList[i] = "";
            smsContentList[i] = smsMessages.get(i).msg;
        }
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
        enteredValues = ArrayUtils.concat(enteredValues, smsSenderList);

        String[] ussdMessages = {};
        try {
            ussdMessages = Utils.convertNormalJSONArrayToStringArray(transaction.ussdMessages);
            ussdMessages = ArrayUtils.concat(ussdMessages, smsContentList);
        } catch (Exception ignored) {}
        return new String[][]{enteredValues, ussdMessages};
    }

    public ParsersInfoModel getParserInfoByIdFromHover(int parserId) {
        HoverParser hoverParser = repo.getSingleParserByParserId(parserId);
        HoverAction action = repo.getSingleActionByIdActionId(hoverParser.actionId);

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

    public  List<TransactionModels> getTransactionsByParserIdFromHover(int parserId) {
        List<Transaction> subList = repo.getTransactionsByParserId(parserId);
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

    private List<String> getLastSMSMessages(JSONArray smsHits) {
        List<String> smsMessages = new ArrayList<>();
        try {
            String[] smsUUIDS = Utils.convertNormalJSONArrayToStringArray(smsHits);
            for(String uuid : smsUUIDS) {
                smsMessages.add(repo.getSMSMessageByUUID(uuid).msg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return smsMessages;
    }

    private String getLastSMSMessage(JSONArray smsHits) {
        String smsMessage = null;
        try {
            String[] smsUUIDS = Utils.convertNormalJSONArrayToStringArray(smsHits);
            if(smsUUIDS.length > 0) {
                String lastUUID = smsUUIDS[smsUUIDS.length-1];
                smsMessage = repo.getSMSMessageByUUID(lastUUID).msg;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return smsMessage;
    }
}
