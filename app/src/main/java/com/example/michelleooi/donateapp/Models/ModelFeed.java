package com.example.michelleooi.donateapp.Models;

import java.util.Date;
import java.util.List;

public class ModelFeed {

    private int upvotes, downvotes, commentNo;
    private String id, status, userid, text, area;
    private Date postTime;
    private List<String> postPic, upvotedUsers, downvotedUsers;

    public ModelFeed() {
    }

    public ModelFeed(int upvotes, int downvotes, int commentNo, String status, String text, String userid, Date postTime, String area) {
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.commentNo = commentNo;
        this.status = status;
        this.text = text;
        this.userid = userid;
        this.postTime = postTime;
        this.area = area;
    }

    public ModelFeed(int upvotes, int downvotes, int commentNo, String status, String text, String userid, Date postTime, List<String> postPic, String area) {
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.commentNo = commentNo;
        this.status = status;
        this.text = text;
        this.userid = userid;
        this.postTime = postTime;
        this.postPic = postPic;
        this.area = area;
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

    public int getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(int commentNo) {
        this.commentNo = commentNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public List<String> getPostPic() {
        return postPic;
    }

    public void setPostPic(List<String> postPic) {
        this.postPic = postPic;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
