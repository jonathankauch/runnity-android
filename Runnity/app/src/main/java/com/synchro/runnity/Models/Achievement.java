package com.synchro.runnity.Models;

/**
 * Created by Shirco on 24/02/2016.
 */
public class Achievement {
    private String content;
    private String start_date;
    private String due_date;
    private boolean is_achieve;
    private String achievement_type;
    private String value;
    private String id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public boolean is_achieve() {
        return is_achieve;
    }

    public void setIs_achieve(boolean is_achieve) {
        this.is_achieve = is_achieve;
    }

    public String getAchievement_type() {
        return achievement_type;
    }

    public void setAchievement_type(String achievement_type) {
        this.achievement_type = achievement_type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
