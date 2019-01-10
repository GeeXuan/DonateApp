package com.example.michelleooi.donateapp.Models;

import java.util.Date;
import java.util.List;

public class ModelFeedComment {
    private int upvotes, downvotes;
    private String id, text, userid, status, postPic;
    private Date postTime;
    private List<String> upvotedUsers, downvotedUsers;

    public ModelFeedComment() {
    }

    public ModelFeedComment(int upvotes, int downvotes, String text, String userid, String status, Date postTime) {
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.text = text;
        this.userid = userid;
        this.status = status;
        this.postTime = postTime;
    }

    public ModelFeedComment(int upvotes, int downvotes, String text, String userid, String status, String postPic, Date postTime) {
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.text = text;
        this.userid = userid;
        this.status = status;
        this.postPic = postPic;
        this.postTime = postTime;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostPic() {
        return postPic;
    }

    public void setPostPic(String postPic) {
        this.postPic = postPic;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getUpvotedUsers() {
        return upvotedUsers;
    }

    public void setUpvotedUsers(List<String> upvotedUsers) {
        this.upvotedUsers = upvotedUsers;
    }

    public List<String> getDownvotedUsers() {
        return downvotedUsers;
    }

    public void setDownvotedUsers(List<String> downvotedUsers) {
        this.downvotedUsers = downvotedUsers;
    }
}
