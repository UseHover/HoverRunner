package com.hover.runner.models;

import com.hover.runner.enums.StatusEnums;

public class TransactionDetailsInfoModels {
    private String label, value;
    private StatusEnums statusEnums;
    private boolean clickable;

    public TransactionDetailsInfoModels(String label, String value, StatusEnums statusEnums, boolean clickable) {
        this.label = label;
        this.value = value;
        this.statusEnums = statusEnums;
        this.clickable = clickable;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public StatusEnums getStatusEnums() {
        return statusEnums;
    }

    public boolean isClickable() {
        return clickable;
    }
}
