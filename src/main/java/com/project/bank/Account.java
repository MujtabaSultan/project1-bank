package com.project.bank;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Account implements IAccount {
    private String accountId;
    private String customerId;
    private String accountType;
    private double balance;
    private boolean isActive;
    private int overdraftCount;
    private DebitCard debitCard;
    private ArrayList<Transaction> transactions;


    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Account(String customerId, String accountType, boolean isActive, double balance, DebitCard debitCard) {
        this.accountId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.accountType = accountType;
        this.isActive = isActive;
        this.balance = balance;
        this.overdraftCount = 0;
        this.debitCard = debitCard;
        this.transactions = new ArrayList<>();

    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public int getOverdraftCount() {
        return overdraftCount;
    }

    public void incrementOverdraft() {
        this.overdraftCount++;
    }
    public void resetOverdraft(){
        this.overdraftCount=0;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", isActive=" + isActive +
                ", overdraftCount=" + overdraftCount +
                ", debitCard=" + debitCard +
                ", transactions=" + transactions +
                '}';
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }
}

