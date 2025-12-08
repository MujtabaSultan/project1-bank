package com.project.bank;

import java.util.ArrayList;
import java.util.Scanner;

public class LoginMenu {

    private Scanner sc = new Scanner(System.in);
    private AuthService auth = new AuthService();

    public void run() {
        while (true) {
            System.out.println("----- BANK SYSTEM -----");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    register();
                    break;
                case "3":
                    System.out.println("have a good day.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println();
            }
        }
    }

    private void register() {
        System.out.println("----- Register -----");
        System.out.print("first name: ");
        String first = sc.nextLine();

        System.out.print("last name: ");
        String last = sc.nextLine();

        System.out.print("email: ");
        String email = sc.nextLine();

        System.out.print("password: ");
        String pass = sc.nextLine();
        System.out.print("password: ");
        String confPass = sc.nextLine();

        System.out.println("select an option 1-3");
        System.out.println("1-Saving account");
        System.out.println("2-checking account");
        System.out.println("3-both");
        int selectionAccTypes = sc.nextInt();
        sc.nextLine();

        if (confPass.equals(pass)) {
            ArrayList<Account> accounts = new ArrayList<>();

            if (selectionAccTypes == 3) {
                System.out.println("Select card type for Saving account (1-3):");
                System.out.println("1-MasterCard");
                System.out.println("2-Titanium MasterCard");
                System.out.println("3-Platinium MasterCard");
                int selectCardType1 = sc.nextInt();
                sc.nextLine();

                DebitCard card1;
                switch (selectCardType1) {
                    case 1:
                        card1 = new MasterCard("010", "10", "1");
                        break;
                    case 2:
                        card1 = new MasterCardTitanium("11", "10", "1");
                        break;
                    case 3:
                        card1 = new MasterCardPlatinium("11", "10", "1");
                        break;
                    default:
                        card1 = new MasterCard("11", "10", "1");
                        break;
                }
                SavingAccount savingAccount = new SavingAccount("1", "1", card1);
                accounts.add(savingAccount);

                System.out.println("Select card type for Checking account (1-3):");
                System.out.println("1-MasterCard");
                System.out.println("2-Titanium MasterCard");
                System.out.println("3-Platinium MasterCard");
                int selectCardType2 = sc.nextInt();
                sc.nextLine();

                DebitCard card2;
                switch (selectCardType2) {
                    case 1:
                        card2 = new MasterCard("010", "10", "1");
                        break;
                    case 2:
                        card2 = new MasterCardTitanium("11", "10", "1");
                        break;
                    case 3:
                        card2 = new MasterCardPlatinium("11", "10", "1");
                        break;
                    default:
                        card2 = new MasterCard("11", "10", "1");
                        break;
                }
                CheckingAccount checkingAccount = new CheckingAccount("1", "1", card2);
                accounts.add(checkingAccount);
            } else {
                System.out.println("Select card type (number please 1-3):");
                System.out.println("1-MasterCard");
                System.out.println("2-Titanium MasterCard");
                System.out.println("3-Platinium MasterCard");
                int selectCardType = sc.nextInt();
                sc.nextLine();

                DebitCard card;
                switch (selectCardType) {
                    case 1:
                        card = new MasterCard("010", "10", "1");
                        break;
                    case 2:
                        card = new MasterCardTitanium("11", "10", "1");
                        break;
                    case 3:
                        card = new MasterCardPlatinium("11", "10", "1");
                        break;
                    default:
                        card = new MasterCard("11", "10", "1");
                        break;
                }

                Account accType;
                switch (selectionAccTypes) {
                    case 1:
                        accType = new SavingAccount("1", "1", card);
                        break;
                    case 2:
                        accType = new CheckingAccount("1", "1", card);
                        break;
                    default:
                        accType = new SavingAccount("1", "1", card);
                }
                accounts.add(accType);
            }

            Customer c = auth.register(first, last, email, pass, accounts);

            if (c == null) {
                System.out.println("Error creating account.");
            } else {
                for(Account acc : accounts) {
                    acc.setCustomerId(c.getId());
                    acc.getDebitCard().setUserId(c.getId());
                    acc.getDebitCard().setAccountId(acc.getAccountId());
                    c.addAccount(acc);
                }

                FileStorageService.updateCustomerFile(c, email);
                System.out.println("Registered successfully! Please log in.");
            }
        } else {
            System.out.println("Password dont match");
        }
    }

    private void login() {
        System.out.println("----- Login -----");

        System.out.println("Email: ");
        String email = sc.nextLine();

        System.out.println("Password: ");
        String pass = sc.nextLine();

        Customer customer = auth.login(email, pass);
        if (customer == null) {
            return;
        }

        System.out.println("Login successful. Welcome " + customer.getFullName() + "!");
        customerMenu(customer, email);
    }

    private void customerMenu(Customer customer, String email) {
        BankService bankService = new BankService();

        while (true) {
            System.out.println("\n----- Customer Menu -----");
            System.out.println("1. View Accounts");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Transaction History");
            System.out.println("6. Filter Transactions");
            System.out.println("7. Bank Statement");
            System.out.println("8. Logout");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAccounts(customer);
                    break;
                case "2":
                    depositMoney(customer, bankService, email);
                    break;
                case "3":
                    withdrawMoney(customer, bankService, email);
                    break;
                case "4":
                    transferMoney(customer, bankService, email);
                    break;
                case "5":
                    viewTransactionHistory(customer);
                    break;
                case "6":
                    filterTransactions(customer);
                    break;
                case "7":
                    viewBankStatement(customer);
                    break;
                case "8":
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAccounts(Customer customer) {
        System.out.println("\n----- Your Accounts -----");
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (int i = 0; i < customer.getAccounts().size(); i++) {
            Account acc = customer.getAccounts().get(i);
            System.out.println((i + 1) + ". " + acc.getAccountType() +
                    " - Balance: $" + String.format("%.2f", acc.getBalance()) +
                    " - Status: " + (acc.isActive() ? "Active" : "Locked"));
        }
    }

    private void depositMoney(Customer customer, BankService bankService, String email) {
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        System.out.println("\n----- Deposit Money -----");
        System.out.println("1. Deposit to my own account");
        System.out.println("2. Deposit to another person's account");
        System.out.print("Choose: ");
        int depositChoice = sc.nextInt();
        sc.nextLine();

        if (depositChoice == 1) {
            viewAccounts(customer);
            System.out.print("Select account (number): ");
            int accIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (accIndex < 0 || accIndex >= customer.getAccounts().size()) {
                System.out.println("Invalid account selection.");
                return;
            }

            Account account = customer.getAccounts().get(accIndex);

            System.out.print("Enter amount to deposit: $");
            double amount = sc.nextDouble();
            sc.nextLine();

            if(bankService.selfdeposit(amount, account)) {
                FileStorageService.updateCustomerFile(customer, email);
            }
        } else if (depositChoice == 2) {
            System.out.print("Enter recipient's email: ");
            String recipientEmail = sc.nextLine();

            Customer recipient = auth.login(recipientEmail, "");
            if (recipient == null) {
                recipient = loadCustomerByEmail(recipientEmail);
            }

            if (recipient == null || recipient.getAccounts().isEmpty()) {
                System.out.println(" Recipient not found or has no accounts.");
                return;
            }

            System.out.println("\nRecipient: " + recipient.getFullName());
            System.out.println("Available accounts:");
            for (int i = 0; i < recipient.getAccounts().size(); i++) {
                Account acc = recipient.getAccounts().get(i);
                System.out.println((i + 1) + ". " + acc.getAccountType() + " - " + acc.getAccountId());
            }

            System.out.print("Select recipient account (number): ");
            int recipientAccIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (recipientAccIndex < 0 || recipientAccIndex >= recipient.getAccounts().size()) {
                System.out.println("Invalid account selection.");
                return;
            }

            Account recipientAccount = recipient.getAccounts().get(recipientAccIndex);

            System.out.print("Enter amount to deposit: $");
            double amount = sc.nextDouble();
            sc.nextLine();

            if(bankService.deposit(amount, recipientAccount)) {
                FileStorageService.updateCustomerFile(recipient, recipientEmail);
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private Customer loadCustomerByEmail(String email) {
        java.io.File f = new java.io.File("Customer-" + email + ".txt");
        if(!f.exists()) return null;

        try(java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(f))){
            String fName = br.readLine();
            String lName = br.readLine();
            String userEmail = br.readLine();
            String userId = br.readLine();
            String hashed = br.readLine();

            Customer customer = new Customer(fName, lName, userId, hashed, userEmail);

            String line;
            while((line = br.readLine()) != null) {
                if(line.startsWith("ACCOUNT:")) {
                    String[] parts = line.split(":");
                    if(parts.length >= 6) {
                        String accountId = parts[1];
                        String accountType = parts[2];
                        double balance = Double.parseDouble(parts[3]);
                        boolean isActive = Boolean.parseBoolean(parts[4]);
                        String cardType = parts[5];

                        DebitCard card;
                        switch(cardType) {
                            case "MasterCardPlatinium":
                                card = new MasterCardPlatinium(accountId, userId, accountId);
                                break;
                            case "MasterCardTitanium":
                                card = new MasterCardTitanium(accountId, userId, accountId);
                                break;
                            default:
                                card = new MasterCard(accountId, userId, accountId);
                        }

                        if(parts.length >= 12) {
                            try {
                                double usedWithdraw = Double.parseDouble(parts[6]);
                                double usedOwnTransfer = Double.parseDouble(parts[7]);
                                double usedExternalTransfer = Double.parseDouble(parts[8]);
                                double usedOwnDeposit = Double.parseDouble(parts[9]);
                                double usedExternalDeposit = Double.parseDouble(parts[10]);
                                java.time.LocalDate lastReset = java.time.LocalDate.parse(parts[11]);

                                card.setUsedWithdrawToday(usedWithdraw);
                                card.setUsedOwnTransferToday(usedOwnTransfer);
                                card.setUsedExternalTransferToday(usedExternalTransfer);
                                card.setUsedOwnDepositToday(usedOwnDeposit);
                                card.setUsedExternalDepositToday(usedExternalDeposit);
                                card.setLastResetDate(lastReset);
                            } catch (Exception e) {
                            }
                        }

                        Account account;
                        if(accountType.equals("Checking")) {
                            account = new CheckingAccount(userId, accountId, card);
                        } else {
                            account = new SavingAccount(userId, accountId, card);
                        }

                        account.setAccountId(accountId);
                        account.setBalance(balance);
                        account.setActive(isActive);

                        if(parts.length >= 13) {
                            try {
                                int overdraftCount = Integer.parseInt(parts[12]);
                                for(int i = 0; i < overdraftCount; i++) {
                                    account.incrementOverdraft();
                                }
                            } catch (Exception e) {
                            }
                        }

                        FileStorageService.loadTransactions(account);

                        customer.addAccount(account);
                    }
                }
            }

            return customer;
        } catch(java.io.IOException e){
            return null;
        }
    }

    private void withdrawMoney(Customer customer, BankService bankService, String email) {
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        viewAccounts(customer);
        System.out.print("Select account (number): ");
        int accIndex = sc.nextInt() - 1;
        sc.nextLine();

        if (accIndex < 0 || accIndex >= customer.getAccounts().size()) {
            System.out.println("Invalid account selection.");
            return;
        }

        Account account = customer.getAccounts().get(accIndex);

        System.out.print("Enter amount to withdraw: $");
        double amount = sc.nextDouble();
        sc.nextLine();

        if(bankService.withdraw(account, amount)) {
            FileStorageService.updateCustomerFile(customer, email);
        }
    }

    private void transferMoney(Customer customer, BankService bankService, String email) {
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        viewAccounts(customer);
        System.out.print("Select source account (number): ");
        int fromIndex = sc.nextInt() - 1;
        sc.nextLine();

        if (fromIndex < 0 || fromIndex >= customer.getAccounts().size()) {
            System.out.println("Invalid account selection.");
            return;
        }

        Account fromAccount = customer.getAccounts().get(fromIndex);

        System.out.println("Transfer to:");
        System.out.println("1. My own account");
        System.out.println("2. Another customer's account");
        System.out.print("Choose: ");
        int transferChoice = sc.nextInt();
        sc.nextLine();

        Account toAccount = null;
        Customer recipient = null;
        String recipientEmail = null;

        if (transferChoice == 1) {
            if (customer.getAccounts().size() < 2) {
                System.out.println("You only have one account.");
                return;
            }
            viewAccounts(customer);
            System.out.print("Select destination account (number): ");
            int toIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (toIndex < 0 || toIndex >= customer.getAccounts().size() || toIndex == fromIndex) {
                System.out.println("Invalid account selection.");
                return;
            }
            toAccount = customer.getAccounts().get(toIndex);
            recipient = customer;
            recipientEmail = email;
        } else if (transferChoice == 2) {
            System.out.print("Enter recipient's email: ");
            recipientEmail = sc.nextLine();

            recipient = loadCustomerByEmail(recipientEmail);

            if (recipient == null || recipient.getAccounts().isEmpty()) {
                System.out.println(" Recipient not found or has no accounts.");
                return;
            }

            System.out.println("\nRecipient: " + recipient.getFullName());
            System.out.println("Available accounts:");
            for (int i = 0; i < recipient.getAccounts().size(); i++) {
                Account acc = recipient.getAccounts().get(i);
                System.out.println((i + 1) + ". " + acc.getAccountType() + " - " + acc.getAccountId());
            }

            System.out.print("Select recipient account (number): ");
            int recipientAccIndex = sc.nextInt() - 1;
            sc.nextLine();

            if (recipientAccIndex < 0 || recipientAccIndex >= recipient.getAccounts().size()) {
                System.out.println("Invalid account selection.");
                return;
            }

            toAccount = recipient.getAccounts().get(recipientAccIndex);
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        System.out.print("Enter amount to transfer: $");
        double amount = sc.nextDouble();
        sc.nextLine();

        if(bankService.transfer(fromAccount, toAccount, amount)) {
            FileStorageService.updateCustomerFile(customer, email);

            if (!email.equals(recipientEmail)) {
                FileStorageService.updateCustomerFile(recipient, recipientEmail);
            }
        }
    }

    private void viewTransactionHistory(Customer customer) {
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        viewAccounts(customer);
        System.out.print("Select account (number): ");
        int accIndex = sc.nextInt() - 1;
        sc.nextLine();

        if (accIndex < 0 || accIndex >= customer.getAccounts().size()) {
            System.out.println("Invalid account selection.");
            return;
        }

        Account account = customer.getAccounts().get(accIndex);

        System.out.println("\n----- Transaction History -----");
        if (account.getTransactions().isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction t : account.getTransactions()) {
            System.out.println(t.getTimestamp() + " | " + t.getType() +
                    " | $" + String.format("%.2f", t.getAmount()));
        }
    }

    private void filterTransactions(Customer customer) {
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        viewAccounts(customer);
        System.out.print("Select account (number): ");
        int accIndex = sc.nextInt() - 1;
        sc.nextLine();

        if (accIndex < 0 || accIndex >= customer.getAccounts().size()) {
            System.out.println("Invalid account selection.");
            return;
        }

        Account account = customer.getAccounts().get(accIndex);

        System.out.println("\n----- Filter Transactions -----");
        System.out.println("1. Today");
        System.out.println("2. Yesterday");
        System.out.println("3. Last 7 days");
        System.out.println("4. Last 30 days");
        System.out.print("Choose filter: ");

        int filterChoice = sc.nextInt();
        sc.nextLine();

        TransactionFilter filter;
        switch (filterChoice) {
            case 1:
                filter = TransactionFilter.today();
                break;
            case 2:
                filter = TransactionFilter.yesterday();
                break;
            case 3:
                filter = TransactionFilter.lastWeek();
                break;
            case 4:
                filter = TransactionFilter.lastMonth();
                break;
            default:
                System.out.println("Invalid filter choice.");
                return;
        }

        System.out.println("\n----- Filtered Transactions -----");
        long count = account.getTransactions().stream()
                .filter(filter::test)
                .peek(t -> System.out.println(t.getTimestamp() + " | " + t.getType() +
                        " | $" + String.format("%.2f", t.getAmount())))
                .count();

        if (count == 0) {
            System.out.println("No transactions found for selected filter.");
        }
    }

    private void viewBankStatement(Customer customer) {
        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        viewAccounts(customer);
        System.out.print("Select account (number): ");
        int accIndex = sc.nextInt() - 1;
        sc.nextLine();

        if (accIndex < 0 || accIndex >= customer.getAccounts().size()) {
            System.out.println("Invalid account selection.");
            return;
        }

        Account account = customer.getAccounts().get(accIndex);

        System.out.println("║                      BANK STATEMENT                            ║");
        System.out.println();
        System.out.println("Account Holder: " + customer.getFullName());
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Account ID: " + account.getAccountId());
        System.out.println("Account Status: " + (account.isActive() ? "Active" : "Locked"));
        System.out.println("Statement Date: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        System.out.println("CURRENT BALANCE: $" + String.format("%.2f", account.getBalance()));
        System.out.println("----------------------------------------------------------------");
        System.out.println();

        if (account.getTransactions().isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("TRANSACTION HISTORY:");
            System.out.println("----------------------------------------------------------------");
            System.out.printf("%-20s | %-15s | %-12s | %-12s%n",
                    "Date & Time", "Type", "Amount", "Balance");
            System.out.println("----------------------------------------------------------------");

            double runningBalance = account.getBalance();

            java.util.List<Transaction> transactions = new java.util.ArrayList<>(account.getTransactions());
            java.util.Collections.reverse(transactions);

            for (Transaction t : transactions) {
                String dateTime = t.getTimestamp().format(
                        java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String type = t.getType();
                double amount = t.getAmount();

                System.out.printf("%-20s | %-15s | $%-11.2f | $%-11.2f%n",
                        dateTime, type, amount, runningBalance);

                if (type.contains("DEPOSIT") || type.contains("TRANSFER_IN")) {
                    runningBalance -= amount;
                } else if (type.contains("WITHDRAWAL") || type.contains("TRANSFER_OUT")) {
                    runningBalance += amount;
                }
            }

            System.out.println("----------------------------------------------------------------");
            System.out.println("Total Transactions: " + account.getTransactions().size());
        }

        System.out.println();
        System.out.println("End of Statement");
    }
}
