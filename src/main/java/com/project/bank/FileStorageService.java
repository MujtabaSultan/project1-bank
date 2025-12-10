package com.project.bank;

import java.io.*;
import java.time.format.DateTimeFormatter;

public class FileStorageService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void saveTransaction(Account account, Transaction transaction) {
        String filename = "Account-" + account.getAccountId() + "-transactions.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(formatTransaction(transaction, account.getBalance()));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }

    private static String formatTransaction(Transaction transaction, double postBalance) {
        return String.format("%s|%s|%s|%.2f|%.2f|%s",
                transaction.getTimestamp().format(FORMATTER),
                transaction.getTransactionId(),
                transaction.getType(),
                transaction.getAmount(),
                postBalance,
                transaction.getAccountId());
    }

    public static void updateCustomerFile(Customer customer, String email) {
        String filename = "Customer-" + email + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println(customer.getFirst_name());
            pw.println(customer.getSecond_name());
            pw.println(email);
            pw.println(customer.getId());
            pw.println(customer.getPassword());

            pw.println("LOCK:" + customer.getFailedLoginAttempts() + ":" +
                    (customer.getLockUntil() != null ? customer.getLockUntil().toString() : "null"));

            pw.println("BANKER:" + customer.isBanker());

            for (Account account : customer.getAccounts()) {
                DebitCard card = account.getDebitCard();
                pw.println("ACCOUNT:" + account.getAccountId() + ":" +
                        account.getAccountType() + ":" + account.getBalance() + ":" +
                        account.isActive() + ":" + card.getClass().getSimpleName() + ":" +
                        card.getUsedWithdrawToday() + ":" +
                        card.getUsedOwnTransferToday() + ":" +
                        card.getUsedExternalTransferToday() + ":" +
                        card.getUsedOwnDepositToday() + ":" +
                        card.getUsedExternalDepositToday() + ":" +
                        card.getLastResetDate() + ":" +
                        account.getOverdraftCount());
            }
        } catch (IOException e) {
            System.err.println("Error updating customer file: " + e.getMessage());
        }
    }

    public static void updateCustomerLockState(String email, int failedAttempts, java.time.LocalDateTime lockUntil) {
        String filename = "Customer-" + email + ".txt";
        File file = new File(filename);

        if (!file.exists()) return;

        try {
            java.util.List<String> lines = new java.util.ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }

            boolean lockLineFound = false;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("LOCK:")) {
                    lines.set(i, "LOCK:" + failedAttempts + ":" +
                            (lockUntil != null ? lockUntil.toString() : "null"));
                    lockLineFound = true;
                    break;
                }
            }

            if (!lockLineFound && lines.size() >= 5) {
                lines.add(5, "LOCK:" + failedAttempts + ":" +
                        (lockUntil != null ? lockUntil.toString() : "null"));
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
                for (String line : lines) {
                    pw.println(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating lock state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadTransactions(Account account) {
        String filename = "Account-" + account.getAccountId() + "-transactions.txt";
        File file = new File(filename);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String transactionId = parts[1];
                    String type = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String receiver = parts[5];

                    Transaction transaction = new Transaction(transactionId, account.getAccountId(), type, amount, receiver);
                    try {
                        transaction.setDateTime(java.time.LocalDateTime.parse(parts[0], FORMATTER));
                    } catch (Exception e) {
                    }
                    account.addTransaction(transaction);
                }
            }
        } catch (IOException e) {
            System.err.println("error loading transactions: " + e.getMessage());
        }
    }
}