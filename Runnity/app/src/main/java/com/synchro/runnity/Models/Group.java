package com.synchro.runnity.Models;

import io.realm.RealmObject;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Group {
    private String description;
    private String name;
    private String user_id;
    private Boolean private_status;
    private String id;
    private String updated_at;
    private Boolean is_register;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Boolean getPrivate_status() {
        return private_status;
    }

    public void setPrivate_status(Boolean private_status) {
        this.private_status = private_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Boolean getIs_register() {
        return is_register;
    }

    public void setIs_register(Boolean is_register) {
        this.is_register = is_register;
    }
}
