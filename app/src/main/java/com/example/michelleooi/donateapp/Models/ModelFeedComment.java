package com.example.michelleooi.donateapp.Models;

public class ModelFeedComment {
    int id, likes, propic;
    String name, time, comments;

    public ModelFeedComment(int id, int likes, int propic, String name, String time, String comments) {
        this.id = id;
        this.likes = likes;
        this.propic = propic;
        this.name = name;
        this.time = time;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getPropic() {
        return propic;
    }

    public void setPropic(int propic) {
        this.propic = propic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
