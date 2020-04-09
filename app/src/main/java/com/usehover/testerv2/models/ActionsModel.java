package com.usehover.testerv2.models;

import com.usehover.testerv2.enums.ActionEnums;

public class ActionsModel {

    private String actionId, actionTitle;
    private ActionEnums actionEnum;

    public ActionsModel(String actionId, String actionTitle, ActionEnums actionEnum) {
        this.actionId = actionId;
        this.actionTitle = actionTitle;
        this.actionEnum = actionEnum;
    }

    public String getActionId() {
        return actionId;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public ActionEnums getActionEnum() {
        return actionEnum;
    }
}
