package com.konel.kryptapps.onboard.model;

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-06-21
 */
public class User {

    public String displayName;
    public String email;
    public String phoneNumber;
    public String fcmId;
    public String photoUrl;

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
        this.displayName = displayName;
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
}
