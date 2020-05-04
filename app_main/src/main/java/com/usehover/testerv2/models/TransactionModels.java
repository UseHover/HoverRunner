package com.usehover.testerv2.models;

import com.usehover.testerv2.enums.StatusEnums;

public class TransactionModels {
    private String transaction_id, date, caption, actionId, category;
    private long idOnDb;
    private StatusEnums statusEnums;


    public TransactionModels(long idOnDb, String transaction_id, String date, String caption, StatusEnums statusEnums) {
        this.idOnDb = idOnDb;
        this.transaction_id = transaction_id;
        this.date = date;
        this.caption = caption;
        this.statusEnums = statusEnums;
    }

    public long getIdOnDb() {
        return idOnDb;
    }

    public StatusEnums getStatusEnums() {
        return statusEnums;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getDate() {
        return date;
    }

    public String getCaption() {
        return caption;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
