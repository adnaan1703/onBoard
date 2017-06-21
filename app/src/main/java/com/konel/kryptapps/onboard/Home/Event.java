package com.konel.kryptapps.onboard.Home;

import java.util.ArrayList;

/**
 * Created by tushargupta on 21/06/17.
 */

public class Event {
    private String mEventName;
    private String mEventDate;
    private String mEventDescription;
    private String mEventVenue;
    private String url;
    private String id;
    private ArrayList<String> attendees;
    private int attendees_count;
    private String owner_id;

    public String getEventName() {
        return mEventName;
    }

    public void setEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public String getEventDate() {
        return mEventDate;
    }

    public void setEventDate(String mEventDate) {
        this.mEventDate = mEventDate;
    }

    public String getEventDescription() {
        return mEventDescription;
    }

    public void setEventDescription(String mEventDescription) {
        this.mEventDescription = mEventDescription;
    }

    public String getEventVenue() {
        return mEventVenue;
    }

    public void setEventVenue(String mEventVenue) {
        this.mEventVenue = mEventVenue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }

    public int getAttendees_count() {
        return attendees_count;
    }

    public void setAttendees_count(int attendees_count) {
        this.attendees_count = attendees_count;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
}
