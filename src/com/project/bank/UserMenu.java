package com.project.bank;

import java.util.List;
import java.util.Scanner;

public class UserMenu {

    private Scanner sc = new Scanner(System.in);

    public void run(Customer user) {
        while (true) {
            System.out.println("\n===============================");
            System.out.println(" Hello, " + user.getFullName() + "!");
            System.out.println(" Your Accounts:");
            System.out.println("===============================");

            List<Account> accounts = user.getAccounts();

            if (accounts.isEmpty()) {
                System.out.println("You have no accounts yet.");
            } else {
                for (int i = 0; i < accounts.size(); i++) {
                    Account acc = accounts.get(i);
                    System.out.println((i + 1) + ". " + acc.getAccountType() + " | Balance: " + acc.getBalance());
                }
            }

            System.out.println("\n0. Logout");
            System.out.print("Select an account: ");
            int choice = sc.nextInt();

            if (choice == 0) {
                System.out.println("Logged out.");
                return;
            }

            if (choice < 1 || choice > accounts.size()) {
                System.out.println("Invalid choice.");
                continue;
            }

            Account selected = accounts.get(choice - 1);

            new AccountDisplay().run(user.getAccounts().get(choice-1),user);
        }
    }
}
