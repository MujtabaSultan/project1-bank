package com.project.bank;

public class BankService {
    private Account account;

    public BankService(Account account) {
        this.account = account;
    }

    public boolean withdraw(double amount){
        if(this.account.getDebitCard().getDailyDepositLimit()<amount){

            this.account.getDebitCard().withdraw(amount);

            this.account.setBalance(this.account.getBalance()-amount);
            return true;
        }
        if(amount>account.getBalance()) account.incrementOverdraft();
        if(account.getBalance()>=-100&&account.getBalance()<amount&&account.getOverdraftCount()==2){
            account.setBalance(account.getBalance()-35);
        }
       return false;
    }
    public boolean deposit(double amount , Account target){
        if(this.account.getDebitCard().getDailyDepositLimit()<amount){

            this.account.getDebitCard().deposit(amount,target);

            this.account.setBalance(this.account.getBalance()-amount);
            return true;
        }
        if(amount>account.getBalance()) account.incrementOverdraft();
        if(account.getBalance()>=-100&&account.getBalance()<amount&&account.getOverdraftCount()==2){
            account.setBalance(account.getBalance()-35);
        }
        return false;
    }
}
