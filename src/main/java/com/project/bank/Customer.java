package com.project.bank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.*;
import java.util.Optional;


public class Customer extends User{

    private String status ;
    private ArrayList<Account> accounts;
    private boolean isBanker;

    public Customer(String firstName, String lastName, String id, String password,String email) {
        super(firstName, lastName, "Customer", id, password,email);
        this.status = "Active";
        this.accounts = new ArrayList<>();
        this.isBanker = false;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBanker() {
        return isBanker;
    }

    public void setIsBanker(boolean isBanker) {
        this.isBanker = isBanker;
    }
    public Optional<Account> findAccountById(String accountId) {
        return accounts.stream().filter(account -> account.getAccountId().equals(accountId)).findFirst();
    }
}