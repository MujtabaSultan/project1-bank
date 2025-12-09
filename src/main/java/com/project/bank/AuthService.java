package com.project.bank;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;


public class AuthService {

    PasswordHasher hasher = new PasswordHasher();

    public Customer register(String firstName, String lastName, String email, String password, ArrayList<Account>accounts){
        File existingFile = new File("Customer-" + email + ".txt");
        if(existingFile.exists()) {
            System.out.println(" Registration failed! Email already exists.");
            return null;
        }

        String hashed = hasher.encypt(password);
        String id = UUID.randomUUID().toString();
        Customer customer = new Customer(firstName, lastName, id, hashed, email);

        try(PrintWriter pw = new PrintWriter(new FileWriter("Customer-"+email+".txt"))){
            pw.println(firstName);
            pw.println(lastName);
            pw.println(email);
            pw.println(id);
            pw.println(hashed);

            pw.println("LOCK:0:null");

            for(Account acc : accounts) {
                pw.println("ACCOUNT:" + acc.getAccountId() + ":" + acc.getAccountType() + ":" +
                        acc.getBalance() + ":" + acc.isActive() + ":" + acc.getDebitCard().getClass().getSimpleName());
            }
        } catch(IOException e){
            return null;
        }
        return customer;
    }

    public Customer login(String email, String password){
        File f = new File("Customer-" + email + ".txt");

        if(!f.exists()) {
            System.out.println("Invalid email or password.");
            return null;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(f))){
            String fName = br.readLine();
            String lName = br.readLine();
            String userEmail = br.readLine();
            String userId = br.readLine();
            String hashed = br.readLine();

            Customer tempCustomer = new Customer(fName, lName, userId, hashed, userEmail);

            String nextLine = br.readLine();
            String firstAccountLine = null;

            if(nextLine != null && nextLine.startsWith("LOCK:")) {
                String[] lockParts = nextLine.split(":", 3); // Limit to 3 parts so timestamp stays intact
                if(lockParts.length >= 3) {
                    try {
                        int failedAttempts = Integer.parseInt(lockParts[1]);
                        String lockUntilStr = lockParts[2];

                        tempCustomer.setFailedLoginAttempts(failedAttempts);
                        if(!lockUntilStr.equals("null")) {
                            LocalDateTime lockUntil = LocalDateTime.parse(lockUntilStr);
                            tempCustomer.setLockUntil(lockUntil);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                tempCustomer.setFailedLoginAttempts(0);
                tempCustomer.setLockUntil(null);
                firstAccountLine = nextLine;
            }

            if(tempCustomer.isLocked()) {
                System.out.println("Account is locked for 1 minute due to multiple failed login attempts.");
                System.out.println("Please try again later.");
                return null;
            }

            if(hasher.check(password, hashed)){
                tempCustomer.resetLock();
                FileStorageService.updateCustomerLockState(email, 0, null);

                Customer customer = tempCustomer;

                if(firstAccountLine != null && firstAccountLine.startsWith("ACCOUNT:")) {
                    processAccountLine(firstAccountLine, customer, userId);
                }

                String line;
                while((line = br.readLine()) != null) {
                    if(line.startsWith("ACCOUNT:")) {
                        processAccountLine(line, customer, userId);
                    }
                }

                return customer;
            } else {
                tempCustomer.loginFail();
                FileStorageService.updateCustomerLockState(email,
                        tempCustomer.getFailedLoginAttempts(),
                        tempCustomer.getLockUntil());

                if(tempCustomer.isLocked()) {
                    System.out.println("Too many failed login attempts!");
                    System.out.println("Account is now locked for 1 minute.");
                } else {
                    int attemptsLeft = 3 - tempCustomer.getFailedLoginAttempts();
                    System.out.println("Invalid password. " + attemptsLeft + " attempt(s) remaining.");
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private void processAccountLine(String line, Customer customer, String userId) {
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
