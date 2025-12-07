package com.project.bank;

import java.io.File;
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

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("have a good day.");
                    return;
                default:
                    System.out.println("Invalid choice.");
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

        Account accType;
        MasterCard mockCard = new MasterCard("s", "S", "s");

        switch (selectionAccTypes) {
            case 1:
                accType = new SavingAccount("1", "1", mockCard);
                break;
            case 2:
                accType = new CheckingAccount("1", "1", mockCard);
                break;
            case 3:
                accType = new SavingAccount("1", "1", mockCard);
                break;
            default:
                accType = new SavingAccount("1", "1", mockCard);
        }

        System.out.println("Select card type (number please 1-3):");
        System.out.println("1-MasterCard");
        System.out.println("2-Titanium MasterCard");
        System.out.println("1-Platinium MasterCard");

        int selectCardType = sc.nextInt();
        sc.nextLine(); // fix: consume leftover newline

        String cardType;
        DebitCard card;
        switch (selectCardType) {
            case 1:
                cardType = "1";
                card = new MasterCard("010", "10", "1");
                break;
            case 2:
                cardType = "2";
                card = new MasterCardTitanium("11", "10", "1");
                break;
            case 3:
                cardType = "3";
                card = new MasterCardPlatinium("11", "10", "1");
                break;
            default:
                cardType = "MasterCard";
                card = new MasterCard("11", "10", "1");
                break;
        }

        if (confPass.equals(pass)) {
            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(accType);
            Customer c = auth.register(first, last, email, pass, accounts);
            card.setAccountId(accType.getAccountId());
            card.setUserId(c.getId());
            accType.setCustomerId(c.getId());
            accType.setUserEMail(c.getEmail());
            c.addAccount(accType);

            if (selectionAccTypes == 3) {
                CheckingAccount secondAccount = new CheckingAccount("generatedId2", c.getId(), card);
                c.addAccount(secondAccount);
            }

            if (c == null) {
                System.out.println("Error creating account.");
            } else {
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

        File userFile = new File("Customer-" + email + ".txt");
        if (!userFile.exists()) {
            System.out.println("Invalid email or password.");
            return;
        }

        System.out.println("Password: ");
        String pass = sc.nextLine();

        Customer customer = auth.login(email, pass);
        if (customer == null) {
            System.out.println("Invalid email or password.");
            return;
        }
        if (customer.isLocked()) {
            System.out.println("Account is locked.");
            System.out.println("Lock expires at: " + customer.getLockUntil());
            return;
        }
        customer.loginSuccess();
        System.out.println("Login successful. Welcome " + customer.getFullName() + "!");
        new UserMenu().run(customer);
    }
}
