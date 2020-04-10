package com.usehover.testerv2.database;

import com.maximeroussy.invitrode.WordGenerator;
import com.usehover.testerv2.enums.StatusEnums;
import com.usehover.testerv2.models.ActionsModel;
import com.usehover.testerv2.models.TransactionModels;

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
