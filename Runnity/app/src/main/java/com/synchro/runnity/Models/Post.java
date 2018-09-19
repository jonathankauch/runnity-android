package com.synchro.runnity.Models;

import io.realm.RealmObject;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Post {
    private int id;
    private String content;
    private String picture;
    private String user_id;
    private String event_id;
    private String created_at;
    private String user_fullname;
    private String likes;
    private boolean is_liked;
    private Comment[] comments;
    private User user;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String group_id;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDate() {
        return created_at;
    }

    public void setDate(String created_at) {
        this.created_at = created_at;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComment(Comment[] comments) {
        this.comments = comments;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public boolean is_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }
}

