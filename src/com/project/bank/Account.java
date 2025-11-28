package com.project.bank;

public abstract class Account {
    private String accountId;
    private double balance;
    private boolean isActive;
    private int overdraftCount;
    private DebitCard debitCard;
}
