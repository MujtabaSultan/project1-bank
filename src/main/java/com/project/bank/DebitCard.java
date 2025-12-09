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
    private double usedOwnTransferToday;
    private double usedExternalTransferToday;
    private double usedOwnDepositToday;
    private double usedExternalDepositToday;
    private LocalDate lastResetDate;

    public DebitCard(String cardId, String accountId, String cardType, String userId ,
                     double dailyWithdrawLimit, double dailyTransferLimit,
                     double dailyOwnTransferLimit, double dailyDepositLimit,
                     double dailyOwnDepositLimit) {
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
        this.usedOwnTransferToday = 0;
        this.usedExternalTransferToday = 0;
        this.usedOwnDepositToday = 0;
        this.usedExternalDepositToday = 0;
        this.lastResetDate = LocalDate.now();
    }
    public abstract void showCardDetails();

    public void dailyReset(){
        LocalDate today = LocalDate.now();
        if(lastResetDate.isBefore(today)){
            usedWithdrawToday = 0;
            usedOwnTransferToday = 0;
            usedExternalTransferToday = 0;
            usedOwnDepositToday = 0;
            usedExternalDepositToday = 0;
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
        if (isOwnAccount) {
            if (usedOwnTransferToday + amount <= dailyOwnTransferLimit) {
                usedOwnTransferToday += amount;
                return true;
            }
        } else {
            if (usedExternalTransferToday + amount <= dailyTransferLimit) {
                usedExternalTransferToday += amount;
                return true;
            }
        }
        return false;
    }

    public boolean deposit(double amount, boolean isOwnAccount){
        dailyReset();
        if (isOwnAccount) {
            if (usedOwnDepositToday + amount <= dailyOwnDepositLimit) {
                usedOwnDepositToday += amount;
                return true;
            }
        } else {
            if (usedExternalDepositToday + amount <= dailyDepositLimit) {
                usedExternalDepositToday += amount;
                return true;
            }
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

    public double getUsedOwnTransferToday() {
        return usedOwnTransferToday;
    }

    public void setUsedOwnTransferToday(double usedOwnTransferToday) {
        this.usedOwnTransferToday = usedOwnTransferToday;
    }

    public double getUsedExternalTransferToday() {
        return usedExternalTransferToday;
    }

    public void setUsedExternalTransferToday(double usedExternalTransferToday) {
        this.usedExternalTransferToday = usedExternalTransferToday;
    }

    public double getUsedOwnDepositToday() {
        return usedOwnDepositToday;
    }

    public void setUsedOwnDepositToday(double usedOwnDepositToday) {
        this.usedOwnDepositToday = usedOwnDepositToday;
    }

    public double getUsedExternalDepositToday() {
        return usedExternalDepositToday;
    }

    public void setUsedExternalDepositToday(double usedExternalDepositToday) {
        this.usedExternalDepositToday = usedExternalDepositToday;
    }

}
