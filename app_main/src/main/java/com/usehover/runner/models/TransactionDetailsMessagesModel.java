package com.usehover.runner.models;

public class TransactionDetailsMessagesModel {
    private String enteredValue, messageContent;

    public TransactionDetailsMessagesModel(String enteredValue, String messageContent) {
        this.enteredValue = enteredValue;
        this.messageContent = messageContent;
    }

    public String getEnteredValue() {
        return enteredValue;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
