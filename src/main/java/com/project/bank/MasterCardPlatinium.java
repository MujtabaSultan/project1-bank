package com.project.bank;

public class MasterCardPlatinium extends DebitCard{

    public MasterCardPlatinium(String cardId, String accountId,String userId) {
        super(cardId, accountId, "MasterCardPlatinium", userId,
                20000, 40000, 80000, 100000, 200000);
    }

    @Override
    public void showCardDetails() {
        System.out.println("Platinium MasterCard Card ID: " + getCardId() + ", Account ID: " + getAccountId());

    }
}