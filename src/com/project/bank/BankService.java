package com.project.bank;

public class BankService {

    FilesUpdater updater = new FilesUpdater();

    public boolean withdraw(Account account,double amount ){
        if(!account.isActive()) return false;
        if(account.getBalance() < 0 && amount > 100)return false;
        DebitCard card = account.getDebitCard();
        if (!card.withdraw(amount)) return false;

        double newBalance = account.getBalance() - amount;

        if (newBalance < 0) {
            account.incrementOverdraft();
            newBalance -= 35;
            if (account.getOverdraftCount() >= 2) {
                account.setActive(false);
            }
        }
        account.setBalance(newBalance);

        String fetchedEmail = updater.findAccountOwnerEmail(account.getAccountId());
        System.out.println("the faggot is " + fetchedEmail);
        updater.updateAccount(account.getUserEMail(), account);

        return true;
    }
    public boolean deposit(double amount , Account account , User user) {
        DebitCard card = account.getDebitCard();
        if (!account.isActive()) return false;
        boolean isOwn = account.getCustomerId().equals(user.getId());

        if (!card.deposit(amount, isOwn)) return false;

        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);

        if (newBalance >= 0 && account.getOverdraftCount() > 0) {
            account.resetOverdraft();
            account.setActive(true);
        }

        String fetchedEmail = updater.findAccountOwnerEmail(account.getAccountId());
        updater.updateAccount(fetchedEmail, account);

        return true;
    }
    public boolean transfer(Account from, Account to, double amount){
        if(!from.isActive()) return false;
        boolean isOwn = from.getCustomerId().equals(to.getCustomerId());
        DebitCard card = from.getDebitCard();
        if (!card.transfer(amount, isOwn)) return false;
        double newFromBalance = from.getBalance() - amount;
        if (newFromBalance < 0) {
            from.incrementOverdraft();
            newFromBalance -= 35;
            if (from.getOverdraftCount() >= 2) {
                from.setActive(false);
            }
        }
        from.setBalance(newFromBalance);
        to.setBalance(to.getBalance() + amount);
        String fromEmail = updater.findAccountOwnerEmail(from.getAccountId());
        String toEmail = updater.findAccountOwnerEmail(to.getAccountId());
        updater.updateAccount(fromEmail, from);
        updater.updateAccount(toEmail, to);
        return true;
    }
}
