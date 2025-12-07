package com.project.bank;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


public class FilesUpdater {

    public void updateAccount(String email, Account updated) {
        if (email == null) return;

        File file = new File("Customer-" + email + ".txt");
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            List<String> out = new ArrayList<>();

            boolean replaced = false;

            // copy all lines, replace ACCOUNT line for this account, and skip old TRANSACTION lines for it
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.startsWith("ACCOUNT|")) {
                    String[] p = line.split("\\|");
                    String accId = p.length > 1 ? p[1] : "";

                    if (accId.equals(updated.getAccountId())) {
                        // write the canonical account header (no transactions)
                        out.add(updated.toFileString());
                        replaced = true;

                        // skip any following TRANSACTION lines that belong to this account
                        int j = i + 1;
                        while (j < lines.size() && lines.get(j).startsWith("TRANSACTION|")) {
                            String[] tp = lines.get(j).split("\\|");
                            String tAcc = tp.length > 1 ? tp[1] : "";
                            if (tAcc.equals(updated.getAccountId())) {
                                j++;
                            } else {
                                break;
                            }
                        }
                        // advance i to j-1 so the outer loop continues after skipped transactions
                        i = j - 1;
                        continue;
                    } else {
                        out.add(line);
                    }
                } else {
                    // keep any other lines (including transactions of other accounts)
                    out.add(line);
                }
            }

            // If account not present in file, append account header
            if (!replaced) {
                out.add(updated.toFileString());
            }

            // Append ALL current transactions for this account as separate lines
            for (Transaction t : updated.getTransactions()) {
                out.add(t.toString()); // Transaction.toString() must be "TRANSACTION|txId|accountId|type|amount|receiver"
            }

            Files.write(file.toPath(), out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String findAccountOwnerEmail(String targetAccountId) {
        try {
            File folder = new File(".");
            File[] files = folder.listFiles((dir, name) ->
                    name.startsWith("Customer-") && name.endsWith(".txt")
            );

            if (files == null) return null;

            for (File file : files) {

                List<String> lines = Files.readAllLines(file.toPath());

                for (String rawLine : lines) {

                    String line = rawLine.trim().replace("\uFEFF", ""); // remove BOM

                    if (!line.startsWith("ACCOUNT|")) continue;

                    String[] parts = line.split("\\|");

                    if (parts.length < 2) continue;

                    String accInFile = parts[1].trim();

                    if (accInFile.equals(targetAccountId)) {
                        // filename is Customer-email.txt
                        return file.getName()
                                .replace("Customer-", "")
                                .replace(".txt", "");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String findAllUserData(String targetAccountId) {
        try {
            File folder = new File(".");
            File[] files = folder.listFiles((dir, name) -> name.startsWith("Customer-") && name.endsWith(".txt"));

            if (files == null) return null;

            for (File file : files) {

                List<String> lines = Files.readAllLines(file.toPath());

                for (String line : lines) {

                    if (line.startsWith("ACCOUNT|")) {
                        String[] parts = line.split("\\|");

                        if (parts.length > 1 && parts[1].equals(targetAccountId)) {
                            return file.toString();
                        }
                    }
                }
            }

        } catch (Exception e) {}

        return null;
    }




}

