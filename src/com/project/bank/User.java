package com.project.bank;

public abstract class User {
    private String first_name;
    private String second_name;
    private String id;
    private String role;

    public User(String first_name, String second_name, String role, String id) {
        this.first_name = first_name;
        this.second_name = second_name;
        this.role = role;
        this.id = id;
    }

    public abstract String getRole ();

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
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
