package com.usehover.testerv2.database;

import com.maximeroussy.invitrode.WordGenerator;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.enums.TransactionDetailsDataType;
import com.usehover.testerv2.models.ActionDetailsModels;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.ParsersInfoModel;
import com.usehover.testerv2.models.StreamlinedStepsModel;
import com.usehover.testerv2.models.TransactionDetailsInfoModels;
import com.usehover.testerv2.models.TransactionModels;
import com.usehover.testerv2.utils.Dummy;
import com.usehover.testerv2.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DatabaseCallsToHover {
    public List<ActionsModel> getAllActionsFromHover() {
        List<ActionsModel> actionsModelList = new ArrayList<>();

        for(int i =0; i<50; i++) {
            String randomId = String.valueOf(new Random().nextInt(1000000));
            String randomSentence = newSentence(5);
            actionsModelList.add(new ActionsModel(randomId, randomSentence, randomStatus(new Random().nextInt(4))));
        }

        return actionsModelList;
    }

    public  List<TransactionModels> getAllTransactionsFromHover() {
        List<TransactionModels> transactionModelsList = new ArrayList<>();
        for(int i=0; i<50; i++) {
            String randomDate = new Date().toString();
            String randomSentence = newSentence(10);
            transactionModelsList.add(new TransactionModels(String.valueOf(i), randomDate, randomSentence, randomStatus(new Random().nextInt(3))));

        }
        return transactionModelsList;
    }

    public ActionDetailsModels getActionDetailsById(String actionId) {
        StreamlinedStepsModel streamlinedStepsModel = null;
        try {
            streamlinedStepsModel = Utils.getStreamlinedStepsStepsFromRaw("*737#", new JSONArray(Dummy.getStringTwo()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ActionDetailsModels actionDetailsModels = new ActionDetailsModels(
                "MTN NIGERIA, AIRTEL KE, AIRTEL NG",
                "123456, 434556, 32345, 678787566, 534232, 6575575, 42323534, 345365456, 45464556",
                "15",
                "4",
                "4",
                "3");
        actionDetailsModels.setStreamlinedStepsModel(streamlinedStepsModel);
        return actionDetailsModels;

    }

    public  List<TransactionModels> getTransactionByActionIdFromHover(String actionId) {
        List<TransactionModels> transactionModelsList = new ArrayList<>();
        for(int i=0; i<20; i++) {
            String randomDate = new Date().toString();
            String randomSentence = newSentence(10);
            transactionModelsList.add(new TransactionModels(String.valueOf(i), randomDate, randomSentence, randomStatus(new Random().nextInt(3))));

        }
        return transactionModelsList;
    }

    public ArrayList<TransactionDetailsInfoModels> getTransactionDetailsByIdFromHover(TransactionDetailsDataType type, String transactionId) {
        ArrayList<TransactionDetailsInfoModels> dataTransacArrayList = new ArrayList<>();
        switch (type) {
            case ABOUT:
                    dataTransacArrayList.add(new TransactionDetailsInfoModels("Status", "Success",
                            StatusEnums.SUCCESS, false));
                    dataTransacArrayList.add(new TransactionDetailsInfoModels("Action", "Safaricom airtime Balance",
                            null, true));
                    dataTransacArrayList.add(new TransactionDetailsInfoModels("ActionID", "2521b88d",
                        null, true));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Time", "11/11/11 at 11:11",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("TransactionId", "e493fee9-2537-43ba-836b-ey329175b0d4",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Result", "Your balance is Ksh 0.00 valid until 2019-11-15. Ziada Points 2507 valid until 2020-03-20",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Category", "Succeed",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Operator", "Safaricom LTD",
                        null, false));



                break;
            case DEVICE:
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Transactions", "4",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Device ID", "20bf04dd07c9d33f",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Brand", "Samsung",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Brand", "j4primeltedx/SM-J415F",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Android ver.", "9",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("App ver.", "1.0(1)",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("SDK ver.", "1.4.5-androidx",
                        null, false));
                break;
            case DEBUG_INFO:
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Input extras", "None",
                        null, false));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Matched parsers", "256106",
                        null, true));
                dataTransacArrayList.add(new TransactionDetailsInfoModels("Parsed variables", "None",
                        null, true));

                break;
        }
        return dataTransacArrayList;
    }

    public ParsersInfoModel getParserInfoByIdFromHover(String parserId) {
        ParsersInfoModel parsersInfoModel = new ParsersInfoModel();
        parsersInfoModel.setParser_action("Send Money to GTBank");
        parsersInfoModel.setParser_actionID("343323423");
        parsersInfoModel.setParser_category("Confirmed");
        parsersInfoModel.setParser_created("12/12/12");
        parsersInfoModel.setParser_regex(".*continue");
        parsersInfoModel.setParser_sender("YourCrew MTN");
        parsersInfoModel.setStatusEnums(randomStatus(new Random().nextInt(4)));
        parsersInfoModel.setParser_type("SMS");

        return parsersInfoModel;
    }

    public  List<TransactionModels> getTransactionByParserIdFromHover(String parserId) {
        List<TransactionModels> transactionModelsList = new ArrayList<>();
        for(int i=0; i<20; i++) {
            String randomDate = new Date().toString();
            String randomSentence = newSentence(10);
            transactionModelsList.add(new TransactionModels(String.valueOf(i), randomDate, randomSentence, randomStatus(new Random().nextInt(3))));

        }
        return transactionModelsList;
    }





    private StatusEnums randomStatus(int id) {
        StatusEnums[] statusEnums = {StatusEnums.PENDING, StatusEnums.SUCCESS, StatusEnums.UNSUCCESSFUL, StatusEnums.NOT_YET_RUN};
        return statusEnums[id];
    }
    private String newSentence(int words) {
        WordGenerator generator = new WordGenerator();
        StringBuilder newWord = new StringBuilder();
        for(int i=0; i< words; i++) {
            newWord.append(generator.newWord(5).toLowerCase()).append(" ");
        }
        return newWord.toString();
    }
}
