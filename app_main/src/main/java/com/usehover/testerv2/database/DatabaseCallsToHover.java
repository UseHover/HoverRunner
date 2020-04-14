package com.usehover.testerv2.database;

import com.maximeroussy.invitrode.WordGenerator;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.ActionDetailsModels;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.ParsersInfoModel;
import com.usehover.testerv2.models.StreamlinedStepsModel;
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

    public  List<TransactionModels> getTransactionByActionIdFromHover() {
        List<TransactionModels> transactionModelsList = new ArrayList<>();
        for(int i=0; i<20; i++) {
            String randomDate = new Date().toString();
            String randomSentence = newSentence(10);
            transactionModelsList.add(new TransactionModels(String.valueOf(i), randomDate, randomSentence, randomStatus(new Random().nextInt(3))));

        }
        return transactionModelsList;
    }

    public ParsersInfoModel getParserInfoByIdFromHover(String parserId) {
        ParsersInfoModel parsersInfoModel = new ParsersInfoModel();
        parsersInfoModel.setParser_action("Send Money to GTBank");
        parsersInfoModel.setParser_actionID("343323423");
        parsersInfoModel.setParser_category("Confirmed");
        parsersInfoModel.setParser_created("12/12/12");
        parsersInfoModel.setParser_regex(".*continue");
        parsersInfoModel.setParser_sender("YourCrew MTN");
        parsersInfoModel.setParser_status("Success");
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
