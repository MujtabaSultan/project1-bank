package com.project.bank;

import java.util.ArrayList;

public class Customer extends User{

    private String status ;
    private ArrayList<Account> accounts;

    public Customer(String firstName, String lastName, String id, String password) {
        super(firstName, lastName, "Customer", id, password);
        this.status = "Active";
        this.accounts = new ArrayList<>();
    }


    @Override
    public String getRole() {
        return "Customer";
    }
    //also not sure if im going to keep this way like multiple acounts under a single user login or going to change it later idk
    public void addAccount(Account account){
        this.accounts.add(account);
    }
    public ArrayList<Account> getAccounts(){
        return accounts;
    }
    public String getStatus(){
        return this.status;
    }


}
