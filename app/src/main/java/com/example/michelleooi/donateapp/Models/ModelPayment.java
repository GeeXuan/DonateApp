package com.example.michelleooi.donateapp.Models;

import java.util.Date;

public class ModelPayment {
    String id, userid, eventid, status;
    double amount;
    Date date;

    public ModelPayment(String userid, String eventid, String status, Date date, double amount) {
        this.userid = userid;
        this.eventid = eventid;
        this.status = status;
        this.date = date;
        this.amount=amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
