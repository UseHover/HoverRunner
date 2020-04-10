package com.usehover.testerv2.models;

import com.usehover.testerv2.enums.StatusEnums;

import java.util.List;

public class FullActionResult {
    private StatusEnums actionEnum;
    private List<ActionsModel> actionsModelList;

    public FullActionResult(StatusEnums actionEnum, List<ActionsModel> actionsModelList) {
        this.actionEnum = actionEnum;
        this.actionsModelList = actionsModelList;
    }

    public StatusEnums getActionEnum() {
        return actionEnum;
    }

    public List<ActionsModel> getActionsModelList() {
        return actionsModelList;
    }
}
