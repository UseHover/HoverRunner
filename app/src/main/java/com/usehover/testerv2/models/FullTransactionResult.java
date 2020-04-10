package com.usehover.testerv2.models;

import com.usehover.testerv2.enums.StatusEnums;

import java.util.List;

public class FullTransactionResult {
    private StatusEnums enums;
    private List<TransactionModels> transactionModelsList;

    public FullTransactionResult(StatusEnums enums, List<TransactionModels> transactionModelsList) {
        this.enums = enums;
        this.transactionModelsList = transactionModelsList;
    }

    public StatusEnums getEnums() {
        return enums;
    }

    public List<TransactionModels> getTransactionModelsList() {
        return transactionModelsList;
    }
}
