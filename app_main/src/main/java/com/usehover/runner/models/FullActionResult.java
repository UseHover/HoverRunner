package com.usehover.runner.models;

import com.usehover.runner.enums.StatusEnums;

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
