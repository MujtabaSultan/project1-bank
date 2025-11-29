package com.project.bank;

public class MasterCardTitanium extends DebitCard{
    public MasterCardTitanium(String cardId, String accountId) {
        super(cardId, accountId, "Titanium",
                2000, 5000, 10000, 5000, 20000);
    }

    @Override
    public void showCardDetails() {
        System.out.println("Titanium MasterCard Card ID: " + getCardId() + ", Account ID: " + getAccountId());

    }
}
