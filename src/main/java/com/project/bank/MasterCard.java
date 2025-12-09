package com.project.bank;

public class MasterCard extends DebitCard{
    public MasterCard(String cardId, String accountId,String userId) {
        super(cardId, accountId, "MasterCard",userId,5000, 10000, 20000, 100000, 200000);
    }

    @Override
    public void showCardDetails() {
        System.out.println("MasterCard Card ID: " + getCardId() + ", Account ID: " + getAccountId());
    }
}
