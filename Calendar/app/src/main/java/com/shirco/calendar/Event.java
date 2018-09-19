package com.shirco.calendar;

import java.util.Date;

/**
 * Created by Shirco on 27/09/2016.
 * Event Class
 */
public class Event {
    private String description;

    public Event(String strDate, String des, Date date) {
        setStrDate(strDate);
        setDate(date);
        setDescription(des);
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String date) {
        this.strDate = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String strDate;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
