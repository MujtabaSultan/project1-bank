package com.project.bank;

public class Banker extends User{
    public Banker(String first_name, String second_name, String id , String password,String email) {
        super(first_name, second_name, "Banker", id , password,email);
    }


//    public void createCustomer(String fname,String lname,){};

    @Override
    public String getRole() {
        return "Banker";
    }
}
