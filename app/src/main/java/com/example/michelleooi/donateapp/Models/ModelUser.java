package com.example.michelleooi.donateapp.Models;

public class ModelUser {
    private String email, name, role, proPic;

    public ModelUser() {
    }

    public ModelUser(String email, String name, String role, String proPic) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.proPic = proPic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProPic() {
        return proPic;
    }

    public void setProPic(String proPic) {
        this.proPic = proPic;
    }
}
