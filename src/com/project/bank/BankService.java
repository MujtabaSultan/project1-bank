package com.project.bank;

import java.util.UUID;

public class BankService {

    public boolean withdraw(Account account, double amount){
        if(!account.isActive()) {
            System.out.println("Account is not active");
            return false;
        }
        if(account.getDebitCard().withdraw(amount)){
            double newBalance = account.getBalance() - amount;
            if(newBalance < 0){
                account.incrementOverdraft();
                if(account.getOverdraftCount() >= 2){
                    newBalance -= 35;
                    account.setActive(false);
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

            System.out.println("Withdrawal successful! New balance: $" + String.format("%.2f", account.getBalance()));
            System.out.println("Transaction logged to file");
            return true;
        } else {
            DebitCard card = account.getDebitCard();
            System.out.println("Withdrawal failed! Daily limit exceeded.");
            System.out.println("Daily limit: $" + String.format("%.2f", card.getDailyWithdrawLimit()));
            System.out.println("Already used today: $" + String.format("%.2f", card.getUsedWithdrawToday()));
            System.out.println("Requested amount: $" + String.format("%.2f", amount));
            return false;
        }
    }
    public boolean selfdeposit(double amount, Account account){
        DebitCard card = account.getDebitCard();
        if(!account.isActive()) {
            System.out.println("Account is not active");
            return false;
        }
        if(card.deposit(amount, true)){
            account.setBalance(account.getBalance() + amount);

            // Log transaction
            Transaction transaction = new Transaction(
                    UUID.randomUUID().toString(),
                    account.getAccountId(),
                    "DEPOSIT",
                    amount,
                    "Self"
            );
            account.addTransaction(transaction);
            FileStorageService.saveTransaction(account, transaction);

            System.out.println("Deposit successful! New balance: $" + String.format("%.2f", account.getBalance()));
            System.out.println("Transaction logged to file");
            return true;
        }
        return false;
    }

    public boolean deposit(double amount, Account account){
        DebitCard card = account.getDebitCard();
        if(!account.isActive()) {
            System.out.println("Account is not active");
            return false;
        }
        if(card.deposit(amount, false)){
            account.setBalance(account.getBalance() + amount);

            // Log transaction
            Transaction transaction = new Transaction(
                    UUID.randomUUID().toString(),
                    account.getAccountId(),
                    "DEPOSIT",
                    amount,
                    "External"
            );
            account.addTransaction(transaction);
            FileStorageService.saveTransaction(account, transaction);

            System.out.println("Deposit successful! New balance: $" + String.format("%.2f", account.getBalance()));
            System.out.println("Transaction logged to file");
            return true;
        } else {
            System.out.println("Deposit failed! Daily limit exceeded.");
            System.out.println("Daily limit: $" + String.format("%.2f", card.getDailyDepositLimit()));
            System.out.println("Already used today: $" + String.format("%.2f", card.getUsedDepositToday()));
            System.out.println("Requested amount: $" + String.format("%.2f", amount));
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
            double newFromBalance = from.getBalance() - amount;
            if(newFromBalance < 0){
                from.incrementOverdraft();
                if(from.getOverdraftCount() >= 2){
                    newFromBalance -= 35;
                    from.setActive(false);
                }
            }
            from.setBalance(newFromBalance);
            to.setBalance(to.getBalance() + amount);

            // Log transactions
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

            System.out.println("Transfer successful!");
            System.out.println("From balance: $" + String.format("%.2f", from.getBalance()));
            System.out.println("To balance: $" + String.format("%.2f", to.getBalance()));
            System.out.println("Transactions logged to files");
            return true;
        } else {
            double limit = isOwnAccount ? card.getDailyOwnTransferLimit() : card.getDailyTransferLimit();
            System.out.println("Transfer failed! Daily limit exceeded.");
            System.out.println("Daily limit: $" + String.format("%.2f", limit));
            System.out.println("Already used today: $" + String.format("%.2f", card.getUsedTransferToday()));
            System.out.println("Requested amount: $" + String.format("%.2f", amount));
            return false;
        }
    }
}
