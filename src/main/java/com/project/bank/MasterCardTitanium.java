package com.project.bank;

public class MasterCardTitanium extends DebitCard{
    public MasterCardTitanium(String cardId, String accountId,String userId) {
        super(cardId, accountId, "Titanium", userId,
                10000, 20000, 40000, 100000, 200000);
    }

    @Override
    public void showCardDetails() {
        System.out.println("Titanium MasterCard Card ID: " + getCardId() + ", Account ID: " + getAccountId());

    }
}
