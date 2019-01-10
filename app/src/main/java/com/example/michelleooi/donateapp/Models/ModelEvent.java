package com.example.michelleooi.donateapp.Models;

import java.util.Date;
import java.util.List;

public class ModelEvent {
    String id, title, name, description, status, submitter;
    double goal, donationamount;
    Date submitDate;
    List<String> images;

    public ModelEvent() {
    }

    public ModelEvent(String title, String name, String description, String status, double goal, Date submitDate, String submitter, double donationamount) {
        this.title = title;
        this.name = name;
        this.description = description;
        this.status = status;
        this.goal = goal;
        this.submitDate = submitDate;
        this.submitter = submitter;
        this.donationamount = donationamount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public double getDonationamount() {
        return donationamount;
    }

    public void setDonationamount(double donationamount) {
        this.donationamount = donationamount;
    }
}
