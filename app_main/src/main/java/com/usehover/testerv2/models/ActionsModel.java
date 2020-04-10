package com.usehover.testerv2.models;

import com.usehover.testerv2.enums.StatusEnums;

public class ActionsModel {

    private String actionId, actionTitle;
    private StatusEnums actionEnum;

    public ActionsModel(String actionId, String actionTitle, StatusEnums actionEnum) {
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

    public StatusEnums getActionEnum() {
        return actionEnum;
    }
}
