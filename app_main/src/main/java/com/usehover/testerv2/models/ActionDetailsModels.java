package com.usehover.testerv2.models;

public class ActionDetailsModels {
    private String operators,  parsers, transactionsNo, successNo, pendingNo, failedNo;
    private StreamlinedStepsModel streamlinedStepsModel;
    public ActionDetailsModels(String operators,  String parsers, String transactionsNo, String successNo, String pendingNo, String failedNo) {
        this.operators = operators;

        this.parsers = parsers;
        this.transactionsNo = transactionsNo;
        this.successNo = successNo;
        this.pendingNo = pendingNo;
        this.failedNo = failedNo;
    }

    public String getOperators() {
        return operators;
    }

    public StreamlinedStepsModel getStreamlinedStepsModel() {
        return streamlinedStepsModel;
    }

    public void setStreamlinedStepsModel(StreamlinedStepsModel streamlinedStepsModel) {
        this.streamlinedStepsModel = streamlinedStepsModel;
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
