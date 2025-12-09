package com.project.bank;

import java.util.UUID;

public class BankService {

    public boolean withdraw(Account account, double amount){
        if(!account.isActive()) {
            System.out.println("Account is not active");
            return false;
        }

        if(account.getBalance() < 0 && amount > 100) {
            System.out.println("withdrawal failed! Account has negative balance.");
            System.out.println("maximum withdrawal allowed: $100.00");
            System.out.println("requested amount: $" +amount);
            return false;
        }

        if(account.getDebitCard().withdraw(amount)){
            double currentBalance = account.getBalance();
            double newBalance = currentBalance - amount;
            if(currentBalance >= 0 && newBalance < 0){
                account.incrementOverdraft();
                newBalance -= 35;
                System.out.println("fee of $35 applied");
                System.out.println("overdraft count: " + account.getOverdraftCount());

                if(account.getOverdraftCount() >= 2){
                    account.setActive(false);
                    System.out.println("account locked beacuse of 2 overdrafts , deposit or transer into this account and make its balance positive to restore it.");
                }
            }
            else if(currentBalance < 0){
                account.incrementOverdraft();
                newBalance -= 35;
                System.out.println("overdraft fee of $35 applied (withdrawing with negative balance).");
                System.out.println("Overdraft count: " + account.getOverdraftCount());

                if(account.getOverdraftCount() >= 2){
                    account.setActive(false);
                    System.out.println("account locked beacuse of 2 overdrafts , deposit or transer into this account and make its balance positive to restore it.");
                }
            }

            account.setBalance(newBalance);

            Transaction transaction = new Transaction(
                    UUID.randomUUID().toString(),
                    account.getAccountId(),
                    "WITHDRAWAL",
                    amount,
                    "Self"
            );
            account.addTransaction(transaction);
            FileStorageService.saveTransaction(account, transaction);

            System.out.println("withdrawl successful! New balance: $ "+account.getBalance());
            return true;
        } else {
            DebitCard card = account.getDebitCard();
            System.out.println("withdrawal failed, Daily limit exceeded.");
            System.out.println("daily limit: $" + card.getDailyWithdrawLimit());
            System.out.println("already used today: $ " +card.getUsedWithdrawToday());
            System.out.println("requested amount: $" +amount);
            return false;
        }
    }
    public boolean selfdeposit(double amount, Account account){
        DebitCard card = account.getDebitCard();
        if(card.deposit(amount, true)){
            double newBalance = account.getBalance() + amount;
            account.setBalance(newBalance);

            if(!account.isActive() && newBalance >= 0){
                account.setActive(true);
                account.resetOverdraft();
                System.out.println("account reactivated , Balance is now positive.");
            }

            Transaction transaction = new Transaction(
                    UUID.randomUUID().toString(),
                    account.getAccountId(),
                    "DEPOSIT",
                    amount,
                    "Self"
            );
            account.addTransaction(transaction);
            FileStorageService.saveTransaction(account, transaction);

            System.out.println("deposit successful ,  New balance: $ " + account.getBalance());
            return true;
        } else {
            System.out.println("deposit failed, Daily limit exceeded.");
            System.out.println("daily limit: $ " + card.getDailyOwnDepositLimit());
            System.out.println("already used today: $ " + card.getUsedOwnDepositToday());
            System.out.println("requested amount: $" + amount);
            return false;
        }
    }

    public boolean deposit(double amount, Account account){
        DebitCard card = account.getDebitCard();
        if(card.deposit(amount, false)){
            double newBalance = account.getBalance() + amount;
            account.setBalance(newBalance);

            if(!account.isActive() && newBalance >= 0){
                account.setActive(true);
                account.resetOverdraft();
                System.out.println("Account reactivated ,Balance is now positive.");
            }

            Transaction transaction = new Transaction(
                    UUID.randomUUID().toString(),
                    account.getAccountId(),
                    "DEPOSIT",
                    amount,
                    "External"
            );
            account.addTransaction(transaction);
            FileStorageService.saveTransaction(account, transaction);

            System.out.println("deposit successful! ");
            return true;
        } else {
            System.out.println("deposit failed! Daily limit exceeded.");
            System.out.println("daily limit: $ " +card.getDailyDepositLimit());
            System.out.println("already used today: $ " +  card.getUsedExternalDepositToday());
            System.out.println("requested amount: $ " + amount);
            return false;
        }
    }

    public boolean transfer(Account from, Account to, double amount){
        DebitCard card = from.getDebitCard();
        if(!from.isActive()) {
            System.out.println("Source account is not active");
            return false;
        }
        boolean isOwnAccount = from.getCustomerId().equals(to.getCustomerId());
        if(card.transfer(amount, isOwnAccount)){
            double currentBalance = from.getBalance();
            double newFromBalance = currentBalance - amount;

            if(currentBalance >= 0 && newFromBalance < 0){
                from.incrementOverdraft();
                newFromBalance -= 35;
                System.out.println("fee of $35 applied.");
                System.out.println("overdraft count: " + from.getOverdraftCount());

                if(from.getOverdraftCount() >= 2){
                    from.setActive(false);
                    System.out.println("account locked beacuse of 2 overdrafts , deposit or transer into this account and make its balance positive to restore it.");
                }
            }

            from.setBalance(newFromBalance);
            to.setBalance(to.getBalance() + amount);

            String transactionId = UUID.randomUUID().toString();

            Transaction outTransaction = new Transaction(
                    transactionId,
                    from.getAccountId(),
                    "TRANSFER_OUT",
                    amount,
                    to.getAccountId()
            );
            from.addTransaction(outTransaction);
            FileStorageService.saveTransaction(from, outTransaction);

            Transaction inTransaction = new Transaction(
                    transactionId,
                    to.getAccountId(),
                    "TRANSFER_IN",
                    amount,
                    from.getAccountId()
            );
            to.addTransaction(inTransaction);
            FileStorageService.saveTransaction(to, inTransaction);

            System.out.println("transfer successful");
            System.out.println("from balance: $ " + from.getBalance());
            System.out.println("to balance: $ " +to.getBalance());
            return true;
        } else {
            double limit = isOwnAccount ? card.getDailyOwnTransferLimit() : card.getDailyTransferLimit();
            double usedToday = isOwnAccount ? card.getUsedOwnTransferToday() : card.getUsedExternalTransferToday();
            System.out.println("transfer failed , Daily limit exceeded.");
            System.out.println("daily limit: $ " + limit);
            System.out.println("already used  today: $ " + usedToday);
            System.out.println("request amount: $ " + amount);
            return false;
        }
    }
}