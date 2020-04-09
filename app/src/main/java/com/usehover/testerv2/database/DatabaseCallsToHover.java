package com.usehover.testerv2.database;

import com.maximeroussy.invitrode.WordGenerator;
import com.usehover.testerv2.enums.ActionEnums;
import com.usehover.testerv2.models.ActionsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseCallsToHover {
    public List<ActionsModel> getAllActionsFromHover() {
        List<ActionsModel> actionsModelList = new ArrayList<>();

        for(int i =0; i<50; i++) {
            String randomId = String.valueOf(new Random().nextInt(1000000));
            String randomSentence = newSentence();
            actionsModelList.add(new ActionsModel(randomId, randomSentence, randomStatus(new Random().nextInt(3))));
        }

        return actionsModelList;
    }


    private ActionEnums randomStatus(int id) {
        ActionEnums[] actionEnums = {ActionEnums.PENDING, ActionEnums.SUCCESS, ActionEnums.UNSUCCESSFUL};
        return actionEnums[id];
    }
    private String newSentence() {
        WordGenerator generator = new WordGenerator();
        return generator.newWord(5) .toLowerCase()+" "+ generator.newWord(6).toLowerCase() +" "+ generator.newWord(5).toLowerCase();
    }
}
