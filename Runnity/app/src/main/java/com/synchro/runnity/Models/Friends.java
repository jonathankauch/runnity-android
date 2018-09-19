package com.synchro.runnity.Models;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Friends {
    private Friend[] friends;
    private Friend[] requests;

    public Friend[] getFriends() {
        return friends;
    }

    public void setFriends(Friend[] friends) {
        this.friends = friends;
    }

    public Friend[] getRequests() {
        return requests;
    }

    public void setRequests(Friend[] requests) {
        this.requests = requests;
    }
}

