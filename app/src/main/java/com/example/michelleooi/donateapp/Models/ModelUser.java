package com.example.michelleooi.donateapp.Models;

public class ModelUser {
    private String id, email, name, role, proPic, ic;

    public ModelUser() {
    }

    public ModelUser(String id, String email, String name, String role, String ic) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.ic = ic;
    }

    public ModelUser(String id, String email, String name, String role, String proPic, String ic) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.proPic = proPic;
        this.ic = ic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }
}
