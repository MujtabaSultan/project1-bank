package com.project.bank;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String accountId;
    private LocalDateTime date;
    private String type;
    private String receiver;
    private double amount;


    public Transaction(String transactionId,String accountId, String type, double amount,String receiver){
        this.transactionId = transactionId;
        this.accountId=accountId;
        this.receiver=receiver;
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getDateTime() {
        return date;
    }

    public LocalDateTime getTimestamp() {
        return date;
    }

    public void setDateTime(LocalDateTime date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
