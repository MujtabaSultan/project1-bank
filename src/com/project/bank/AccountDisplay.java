package com.project.bank;

import java.util.Scanner;
import java.util.UUID;

public class AccountDisplay {

    private Scanner sc = new Scanner(System.in);
    BankService banker = new BankService();



    private Account loadAccountById(String accountId, String email) {
        AuthService auth = new AuthService();
        Customer customer = auth.login(email, "x");

        if (customer != null) {
            for (Account acc : customer.getAccounts()) {
                if (acc.getAccountId().equals(accountId)) {
                    return acc;
                }
            }
        }
        return null;}


    public void run(Account account ,User user) {
        while (true) {
            System.out.println("\n--- Account: " + account.getAccountType() + " ---");
            System.out.println("Acc.id: "+ account.getAccountId());
            System.out.println("Balance: $" + account.getBalance());
            System.out.println("1. Withdraw");
            System.out.println("2. Deposit");
            System.out.println("3. Transfer");
            System.out.println("4. Show Transactions");
            System.out.println("5. Back to User Menu");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    handleWithdraw(account);
                    break;
                case "2":
                    handleDeposit(account,user);
                    break;
                case "3":
                    handleTransfer(account);
                    break;
                case "4":
                    showTransactions(account);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void handleWithdraw(Account account ) {
        System.out.print("Enter amount to withdraw: ");
        double amount = Double.parseDouble(sc.nextLine());


        if (account.getDebitCard().withdraw(amount)) {
            banker.withdraw(account,amount);
            account.addTransaction(new Transaction(UUID.randomUUID().toString(), account.getAccountId(),"Withdraw", amount,"??"));
            System.out.println("Withdraw success.");
        } else {
            System.out.println("Withdraw failed.");
        }
    }

    private void handleDeposit(Account account, User user) {
        System.out.print("Enter target account ID: ");
        String targetId = sc.nextLine();

        System.out.print("Enter amount to deposit: ");
        double amount = Double.parseDouble(sc.nextLine());

        FilesUpdater updater = new FilesUpdater();
        String targetEmail = updater.findAccountOwnerEmail(targetId);

        if (targetEmail == null) {
            System.out.println("Deposit failed: target account not found.");
            return;
        }

        Account targetAccount = loadAccountById(targetId, targetEmail);
        if (targetAccount == null) {
            System.out.println("Deposit failed: could not find account");
            return;
        }

        boolean isOwnAccount = account.getCustomerId().equals(targetAccount.getCustomerId());

        if (!account.getDebitCard().deposit(amount, isOwnAccount)) {
            System.out.println("Deposit failed due to limit rules.");
            return;
        }

        if (!banker.deposit(amount, targetAccount, user)) {
            System.out.println("Deposit failed.");
            return;
        }

        account.addTransaction(new Transaction(
                UUID.randomUUID().toString(),
                account.getAccountId(),
                "Deposit Out",
                amount,
                targetId
        ));

        targetAccount.addTransaction(new Transaction(
                UUID.randomUUID().toString(),
                targetAccount.getAccountId(),
                "Deposit In",
                amount,
                account.getAccountId()
        ));

        updater.updateAccount(updater.findAccountOwnerEmail(account.getAccountId()), account);
        updater.updateAccount(targetEmail, targetAccount);

        System.out.println("Deposit successful.");
    }
    private void handleTransfer(Account account) {
        System.out.print("Enter target account ID: ");
        String targetId = sc.nextLine();

        System.out.print("Enter amount to transfer: ");
        double amount = Double.parseDouble(sc.nextLine());

        FilesUpdater updater = new FilesUpdater();
        String targetEmail = updater.findAccountOwnerEmail(targetId);

        if (targetEmail == null) {
            System.out.println("Target account not found.");
            return;
        }
        Account targetAccount = loadAccountById(targetId, targetEmail);

        if (targetAccount == null) {
            System.out.println("Could not load target account.");
            return;
        }

        boolean isOwnAccount = account.getCustomerId().equals(targetAccount.getCustomerId());

        if (banker.transfer(account, targetAccount, amount)) {
            account.addTransaction(new Transaction(
                    UUID.randomUUID().toString(),
                    account.getAccountId(),
                    "Transfer Out",
                    amount,
                    targetId
            ));

            targetAccount.addTransaction(new Transaction(
                    UUID.randomUUID().toString(),
                    targetAccount.getAccountId(),
                    "Transfer In",
                    amount,
                    account.getAccountId()
            ));

            updater.updateAccount(updater.findAccountOwnerEmail(account.getAccountId()), account);
            updater.updateAccount(targetEmail, targetAccount);

            System.out.println("Transfer successful!");
        } else {
            System.out.println("Transfer failed. Check limits or balance.");
        }
    }




    private void showTransactions(Account account) {
        System.out.println("\n--- Transactions ---");
        if (account.getTransactions().isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            account.getTransactions().forEach(t -> {
                System.out.println(t.getDateTime() + " | " + t.getType() + " | $" + t.getAmount());
            });
        }
    }
}
