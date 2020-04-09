package com.usehover.testerv2.enums;

import com.usehover.testerv2.models.ActionsModel;

import java.util.List;

public class FullActionResult {
    private ActionEnums actionEnum;
    private List<ActionsModel> actionsModelList;

    public FullActionResult(ActionEnums actionEnum, List<ActionsModel> actionsModelList) {
        this.actionEnum = actionEnum;
        this.actionsModelList = actionsModelList;
    }

    public ActionEnums getActionEnum() {
        return actionEnum;
    }

    public List<ActionsModel> getActionsModelList() {
        return actionsModelList;
    }
}
