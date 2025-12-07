package com.project.bank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DebitCard {
    private String cardId;
    private String accountId;
    private String userId;
    private String cardType;
    private double dailyWithdrawLimit;
    private double dailyTransferLimit;
    private double dailyOwnTransferLimit;
    private double dailyDepositLimit;
    private double dailyOwnDepositLimit;
    private double usedWithdrawToday;
    private double usedTransferToday;
    private double usedDepositToday;
    private LocalDate lastResetDate;

    public DebitCard(String cardId, String accountId, String cardType, String userId ,
                     double dailyWithdrawLimit, double dailyTransferLimit,
                     double dailyOwnTransferLimit, double dailyDepositLimit,
                     double dailyOwnDepositLimit) {
        //this will be assigbed automatically , i will not remove the ids from all 4 cards im too lazy so ill just put one to shut it up
        this.cardId = UUID.randomUUID().toString();
        this.accountId = accountId;
        this.userId=userId;
        this.cardType = cardType;
        this.dailyWithdrawLimit = dailyWithdrawLimit;
        this.dailyTransferLimit = dailyTransferLimit;
        this.dailyOwnTransferLimit = dailyOwnTransferLimit;
        this.dailyDepositLimit = dailyDepositLimit;
        this.dailyOwnDepositLimit = dailyOwnDepositLimit;
        this.usedWithdrawToday = 0;
        this.usedTransferToday = 0;
        this.usedDepositToday = 0;
        this.lastResetDate = LocalDate.now();
    }
    public abstract void showCardDetails();

    public void dailyReset(){
        LocalDate today = LocalDate.now();
        if(lastResetDate.isBefore(today)){
            usedWithdrawToday = 0;
            usedTransferToday = 0;
            usedDepositToday = 0;
            lastResetDate = today;
        }
    }
    public boolean withdraw(double amount) {
        dailyReset();
        if (usedWithdrawToday + amount <= dailyWithdrawLimit) {
            usedWithdrawToday += amount;
            return true;
        }
        return false;
    }

    public boolean transfer(double amount, boolean isOwnAccount) {
        dailyReset();
        double limit = isOwnAccount ? dailyOwnTransferLimit : dailyTransferLimit;
        if (usedTransferToday + amount <= limit) {
            usedTransferToday += amount;
            return true;
        }
        return false;
    }

    public boolean deposit(double amount, boolean isOwnAccount){
        dailyReset();
        double limit = isOwnAccount ? dailyOwnDepositLimit : dailyDepositLimit;
        if(usedDepositToday + amount <= limit){
            usedDepositToday += amount;
            return true;
        }
        return false;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public LocalDate getLastResetDate() {
        return lastResetDate;
    }

    public void setLastResetDate(LocalDate lastResetDate) {
        this.lastResetDate = lastResetDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getDailyWithdrawLimit() {
        return dailyWithdrawLimit;
    }

    public void setDailyWithdrawLimit(double dailyWithdrawLimit) {
        this.dailyWithdrawLimit = dailyWithdrawLimit;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public double getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(double dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public double getDailyOwnTransferLimit() {
        return dailyOwnTransferLimit;
    }

    public void setDailyOwnTransferLimit(double dailyOwnTransferLimit) {
        this.dailyOwnTransferLimit = dailyOwnTransferLimit;
    }

    public double getDailyDepositLimit() {
        return dailyDepositLimit;
    }

    public void setDailyDepositLimit(double dailyDepositLimit) {
        this.dailyDepositLimit = dailyDepositLimit;
    }

    public double getDailyOwnDepositLimit() {
        return dailyOwnDepositLimit;
    }

    public void setDailyOwnDepositLimit(double dailyOwnDepositLimit) {
        this.dailyOwnDepositLimit = dailyOwnDepositLimit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getUsedWithdrawToday() {
        return usedWithdrawToday;
    }

    public void setUsedWithdrawToday(double usedWithdrawToday) {
        this.usedWithdrawToday = usedWithdrawToday;
    }

    public double getUsedTransferToday() {
        return usedTransferToday;
    }

    public void setUsedTransferToday(double usedTransferToday) {
        this.usedTransferToday = usedTransferToday;
    }

    public double getUsedDepositToday() {
        return usedDepositToday;
    }

    public void setUsedDepositToday(double usedDepositToday) {
        this.usedDepositToday = usedDepositToday;
    }

}
