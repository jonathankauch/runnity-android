package com.synchro.runnity.Models;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Users {
    private Friend[] users;

    public Users() {
        this.users = new Friend[]{};
    }

    public Friend[] getFriends() {
        return users;
    }

    public void setFriends(Friend[] friends) {
        this.users = friends;
    }
}

