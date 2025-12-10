package com.project.bank;

public class Banker extends Customer {

    public Banker(String firstName, String lastName, String id, String password, String email) {
        super(firstName, lastName, id, password, email);
        setIsBanker(true);
    }

    @Override
    public String getRole() {
        return "Banker";
    }
}