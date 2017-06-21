package com.konel.kryptapps.onboard.model;

import com.konel.kryptapps.onboard.Home.Event;

import java.util.ArrayList;

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-06-21
 */
public class User {

    public String name;
    public String email;
    public String phoneNumber;
    public String fcmId;
    public String photoUrl;
    public ArrayList<String> eventsCreated;
    ArrayList<Event> eventsInvited;

    public User() {

    }

    public User(String displayName, String email, String phoneNumber, String photoUrl) {
        setDisplayName(displayName);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPhotoUrl(photoUrl);
    }

    public User(String displayName, String email, String phoneNumber, String photoUrl, String fcmId) {
        setDisplayName(displayName);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setFcmId(fcmId);
        setPhotoUrl(photoUrl);
    }

    public User(String fcmId) {
        setFcmId(fcmId);
    }


    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public void setDisplayName(String displayName) {
        this.name = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<String> getEventsCreated() {
        return eventsCreated;
    }

    public void setEventsCreated(ArrayList<String> eventsCreated) {
        this.eventsCreated = eventsCreated;
    }

    public ArrayList<Event> getEventsInvited() {
        return eventsInvited;
    }

    public void setEventsInvited(ArrayList<Event> eventsInvited) {
        this.eventsInvited = eventsInvited;
    }
}
