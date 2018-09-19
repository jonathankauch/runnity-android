package com.shirco.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Shirco on 27/09/2016.
 * SingleTon
 * Class containing the an Instance of itself
 * Can be called from anywhere in the code to get events
 */
public class Singleton {
    private static Singleton mInstance = null;

    private List<Event> events;
    private List<Event> searchedEvents;

    private Singleton() {
        events = new ArrayList<>();
        searchedEvents = new ArrayList<>();
    }

    public static Singleton getInstance() {
        if (mInstance == null) {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void sortEvents() {
        Collections.sort(events, new Comparator<Event>() {

            @Override
            public int compare(Event lev, Event rev) {
                //here getTitle() method return app name...
                return lev.getDate().compareTo(rev.getDate());

            }
        });

    }

    public List<Event> getSearchedEvents() {
        return searchedEvents;
    }

    public void setSearchedEvents(List<Event> searchedEvents) {
        this.searchedEvents = searchedEvents;
    }
}
