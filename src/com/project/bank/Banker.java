package com.project.bank;

public class Banker extends User{
    public Banker(String first_name, String second_name, String role, String id) {
        super(first_name, second_name, "Banker", id);
    }

    @Override
    public String getRole() {
        return "Banker";
    }
}
