package com.usehover.testerv2.models;

import com.usehover.testerv2.enums.StatusEnums;

public class TransactionModels {
    private String transaction_id, date, caption;
    private StatusEnums statusEnums;

    public TransactionModels(String transaction_id, String date, String caption, StatusEnums statusEnums) {
        this.transaction_id = transaction_id;
        this.date = date;
        this.caption = caption;
        this.statusEnums = statusEnums;
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
}
