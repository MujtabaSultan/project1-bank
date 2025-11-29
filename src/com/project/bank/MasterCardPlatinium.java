package com.project.bank;

public class MasterCardPlatinium extends DebitCard{

        public MasterCardPlatinium(String cardId, String accountId) {
            super(cardId, accountId, "MasterCardPlatinium",
                    5000, 20000, 50000, 20000, 100000);
        }

    @Override
    public void showCardDetails() {
        System.out.println("Platinium MasterCard Card ID: " + getCardId() + ", Account ID: " + getAccountId());

    }
}