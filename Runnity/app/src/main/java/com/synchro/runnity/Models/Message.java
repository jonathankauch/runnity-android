package com.synchro.runnity.Models;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Message {
    private int id;
    private String body;
    private String user_id;
    private String created_at;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return created_at;
    }

    public void setDate(String created_at) {
        this.created_at = created_at;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

