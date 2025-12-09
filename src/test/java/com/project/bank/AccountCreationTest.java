package com.project.bank;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountCreationTest {
//its not even account creation test its just operations checking for overdraft ,
    private DebitCard card;

    @Before
    public void setUp() {
        card = new MasterCard("acc123", "user123", "acc123");
    }

    @Test
    public void testAccountBalance() {
        Account account = new SavingAccount("user123", "acc123", card);
        account.setBalance(100.0);
        assertEquals(100.0, account.getBalance(), 0.01);
    }

    @Test
    public void testOverdraftIncrement() {
        Account account = new SavingAccount("user123", "acc123", card);
        assertEquals(0, account.getOverdraftCount());
        account.incrementOverdraft();
        assertEquals(1, account.getOverdraftCount());
    }

    @Test
    public void testOverdraftReset() {
        Account account = new SavingAccount("user123", "acc123", card);
        account.incrementOverdraft();
        account.resetOverdraft();
        assertEquals(0, account.getOverdraftCount());
    }
}
