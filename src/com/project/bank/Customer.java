package com.project.bank;

public class Customer extends User{
    public Customer(String first_name, String second_name, String role, String id) {
        super(first_name, second_name, "Customer", id);
    }

    @Override
    public String getRole() {
        return "Customer";
    }

}
