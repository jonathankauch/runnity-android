package com.synchro.runnity.Models;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Notifications {
    private Notification[] groups;
    private Notification[] events;

    public Notification[] getGroups() {
        return groups;
    }

    public void setGroups(Notification[] groups) {
        this.groups = groups;
    }

    public Notification[] getEvents() {
        return events;
    }

    public void setEvents(Notification[] events) {
        this.events = events;
    }
}

