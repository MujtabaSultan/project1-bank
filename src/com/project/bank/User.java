package com.project.bank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

public abstract class User {
    private String first_name;
    private String last_name;
    private String id;
    private String password;
    private String role;
    private Integer failed_login_attempts;
    private LocalDateTime lockUntil;

    public User(String first_name, String last_name, String role, String id,String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.role = role;
        this.id = id;
        this.password=password;
        failed_login_attempts=0;
        lockUntil=null;
    }

    public abstract String getRole ();

    public boolean isLocked(){
        return lockUntil != null && LocalDateTime.now().isBefore(lockUntil);
    }
    public void loginFail(){
        failed_login_attempts++;
        if(failed_login_attempts>=3){
            lockUntil=LocalDateTime.now().plusMinutes(1);
        }
    }

    public String getFullName(){
        return this.first_name+this.last_name;
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return last_name;
    }

    public void setSecond_name(String second_name) {
        this.last_name = second_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
