package com.project.bank;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;


public class AuthService {

    PasswordHasher hasher = new PasswordHasher();


    public Customer register(String firstName, String lastName, String email, String password, ArrayList<Account>accounts){
        String hashed = hasher.encypt(password);
        String id = UUID.randomUUID().toString();
        Customer customer = new Customer(firstName, lastName, id, hashed, email);

        try (PrintWriter pw = new PrintWriter(new FileWriter("Customer-" + email + ".txt"))) {
            pw.println(firstName);
            pw.println(lastName);
            pw.println(email);
            pw.println(id);
            pw.println(hashed);

            for (Account a : accounts) {
                pw.println(a.toFileString());
                for (Transaction t : a.getTransactions()) {
                    pw.println(t.toString());
                }
            }
        } catch (IOException e) {
            return null;
        }
        return customer;
    }
    public Customer login(String email, String password) {

        File f = new File("Customer-" + email + ".txt");
        if (!f.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {

            String fName = br.readLine();
            String lName = br.readLine();
            String storedEmail = br.readLine();
            String userId = br.readLine();
            String hashed = br.readLine();

            if (!hasher.check(password, hashed)) return null;

            Customer customer = new Customer(fName, lName, userId, hashed, storedEmail);

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ACCOUNT|")) {
                    Account acc = parseAccount(line,br, customer.getId());
                    if (acc != null) customer.addAccount(acc);
                }
            }

            return customer;

        } catch (IOException e) {
            return null;
        }
    }

    private Account parseAccount(
            String line,
            BufferedReader br,
            String ownerId
    ) throws IOException {

        String[] p = line.split("\\|");

        String accountId = p[1];
        String accountCustomerId = p[2];
        String accountType = p[3];
        double balance = Double.parseDouble(p[4]);
        boolean isActive = Boolean.parseBoolean(p[5]);
        int overdraftCount = Integer.parseInt(p[6]);

        DebitCard card = null;
        if (p.length > 7 && p[7].equals("CARD")) {
            String cardId = p[8];
            String cardType = p[9];
            double dailyWithdraw = Double.parseDouble(p[10]);
            double dailyDeposit = Double.parseDouble(p[11]);
            double dailyTransfer = Double.parseDouble(p[12]);
            double ownTransfer = Double.parseDouble(p[13]);
            double ownDeposit = Double.parseDouble(p[14]);
            double usedWithdraw = Double.parseDouble(p[15]);
            double usedTransfer = Double.parseDouble(p[16]);
            double usedDeposit = Double.parseDouble(p[17]);
            LocalDate lastReset = LocalDate.parse(p[18]);

            switch(cardType) {
                case "Platinium": card = new MasterCardPlatinium(cardId, accountId, ownerId); break;
                case "Titanium": card = new MasterCardTitanium(cardId, accountId, ownerId); break;
                case "MasterCard": card = new MasterCard(cardId, accountId, ownerId); break;
            }

            if (card != null) {
                card.setDailyWithdrawLimit(dailyWithdraw);
                card.setDailyDepositLimit(dailyDeposit);
                card.setDailyTransferLimit(dailyTransfer);
                card.setDailyOwnTransferLimit(ownTransfer);
                card.setDailyOwnDepositLimit(ownDeposit);
                card.setUsedWithdrawToday(usedWithdraw);
                card.setUsedTransferToday(usedTransfer);
                card.setUsedDepositToday(usedDeposit);
                card.setLastResetDate(lastReset);
            }
        }

        Account acc = accountType.equals("Saving")
                ? new SavingAccount(accountId, accountCustomerId, card)
                : new CheckingAccount(accountId, accountCustomerId, card);

        acc.setBalance(balance);
        acc.setActive(isActive);
        acc.setOverdraftCount(overdraftCount);
        String nextLine;
        while ((nextLine = br.readLine()) != null && nextLine.startsWith("TRANSACTION|")) {
            String[] t = nextLine.split("\\|");
            if (t.length >= 5) {
                String txId = t[1];
                String accId = t[2];
                String type = t[3];
                double amount = Double.parseDouble(t[4]);
                String receiver = t[5];
                Transaction tx = new Transaction(txId, accId, type, amount, receiver);
                acc.addTransaction(tx);
            }
        }

        return acc;
    }

    private Transaction parseTransaction(String line) {
        String[] p = line.split("\\|");
        return new Transaction(
                p[1],
                p[2],
                p[3],
                Double.parseDouble(p[4]),
                p[5]
        );
    }

}
