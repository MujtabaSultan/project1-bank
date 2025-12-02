package com.project.bank;

public class AuthService {

    PasswordHasher hasher;
    public Account login(String email , String input){
      //if(hasher.check())
        //find user by email from files later , then verify that user hasedpass with input using passhsh
        return null;

    }
    public Account register(String email,String firstName , String lastName  , String input){

        //find user by email from files later , if he exists return null, if he doesnt exist will create the user with a hashed pass word
        String hashed = hasher.encypt(input);
        return null;

    }


}
