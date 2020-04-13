package com.usehover.testerv2.models;

public class ActionDetailsModels {
    private String operators, steps, parsers, transactionsNo, successNo, pendingNo, failedNo;

    public ActionDetailsModels(String operators, String steps, String parsers, String transactionsNo, String successNo, String pendingNo, String failedNo) {
        this.operators = operators;
        this.steps = steps;
        this.parsers = parsers;
        this.transactionsNo = transactionsNo;
        this.successNo = successNo;
        this.pendingNo = pendingNo;
        this.failedNo = failedNo;
    }

    public String getOperators() {
        return operators;
    }

    public String getSteps() {
        return steps;
    }

    public String getParsers() {
        return parsers;
    }

    public String getTransactionsNo() {
        return transactionsNo;
    }

    public String getSuccessNo() {
        return successNo;
    }

    public String getPendingNo() {
        return pendingNo;
    }

    public String getFailedNo() {
        return failedNo;
    }
}
