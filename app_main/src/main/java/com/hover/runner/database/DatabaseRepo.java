package com.hover.runner.database;

import com.hover.runner.ApplicationInstance;
import com.hover.sdk.actions.HoverAction;
import com.hover.sdk.api.Hover;
import com.hover.sdk.parsers.HoverParser;
import com.hover.sdk.sms.MessageLog;
import com.hover.sdk.transactions.Transaction;

import java.util.List;

class DatabaseRepo {
     List<HoverAction> getAllActionsFromHover() { return Hover.getAllActions(ApplicationInstance.getContext());}
     HoverAction getSingleActionByIdActionId(String actionId) {return Hover.getAction(actionId, ApplicationInstance.getContext());}Transaction getTransactionByTransId(String transId) {return Hover.getTransaction(transId, ApplicationInstance.getContext());}

     List<Transaction> getAllTransactionsFromHover() { return Hover.getAllTransactions(ApplicationInstance.getContext()); }
     List<Transaction> getTransactionsWithArgsFromHover(String args) { return Hover.getTransactions(args, ApplicationInstance.getContext()); }
     List<Transaction> getTransactionsByActionId(String actionId) {return Hover.getTransactionsByActionId(actionId, ApplicationInstance.getContext());}
     List<Transaction> getTransactionsByParserId(int parserId) {return Hover.getTransactionsByParserId(parserId, ApplicationInstance.getContext());}

     List<HoverParser> getParsersByActionId(String actionId){ return Hover.getParsersByActionId(actionId, ApplicationInstance.getContext()); }
     HoverParser getSingleParserByParserId(int parserId) {return Hover.getParser(parserId, ApplicationInstance.getContext());}
     MessageLog getSMSMessageByUUID(String uuid) {return Hover.getSMSMessageByUUID(uuid, ApplicationInstance.getContext());}



}
