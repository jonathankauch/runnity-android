package com.synchro.runnity.Models;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Shirco on 26/01/2016.
 */
public class Singleton {
    private static Singleton mInstance = null;

    private String mToken;
    private String mId;
    private User mUser;
    private Runs runs;
    private Runs spots;
    private Events events;
    private Groups groups;
    private Posts posts;
    private Friends friends;
    private AllUsers allUsers;
    private Notifications notifications;
    private boolean init = false;

    private Singleton(String token, String id){
        mToken = token;
        mId = id;
    }

    public static Singleton getInstance(String token, String id){
        if(mInstance == null)
        {
            mInstance = new Singleton(token, id);
        } else {
            mInstance.setToken(token);
            mInstance.setId(id);
        }
        return mInstance;
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton("", "");
        }
        return mInstance;
    }

    public String getToken(){
        return this.mToken;
    }

    public void setToken(String value){
        mToken = value;
    }

    public String getId(){
        return this.mId;
    }

    public void setId(String value){
        mId = value;
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public Runs getSpots() {
        return spots;
    }

    public void setSpots(Runs spots) {
        this.spots = spots;
    }


    public Runs getRuns() {
        return runs;
    }

    public void setRuns(Runs runs) {
        this.runs = runs;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public Groups getGroups() {
        if (groups == null) {
            groups = new Groups();
            groups.setGroups(new Group[]{});
        }
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public void initRealm() {
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm.compactRealm(realmConfig);
        init = true;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends= friends;
    }

    public Notifications getNotifications() {
        if (notifications == null) {
            notifications = new Notifications();
            Notification[] notificationsArray = {};
            notifications.setEvents(notificationsArray);
            notifications.setGroups(notificationsArray);
        }
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    public AllUsers getAllUsers() {
        if (allUsers == null) {
            allUsers = new AllUsers();
            Friend[] array = {};
            allUsers.setUsers(array);
        }
        return allUsers;
    }

    public void setAllUsers(AllUsers allUsers) {
        this.allUsers = allUsers;
    }
}