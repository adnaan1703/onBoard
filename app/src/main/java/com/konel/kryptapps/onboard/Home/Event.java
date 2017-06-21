package com.konel.kryptapps.onboard.Home;

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
}
