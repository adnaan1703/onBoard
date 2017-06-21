package com.konel.kryptapps.onboard;

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-06-20
 */
public class User {

    public String name;
    public String phoneNumber;
    public String photoUrl;
    public String email;

    public User(String name, String phoneNumber, String photoUrl, String email) {
        setName(name);
        setPhoneNumber(phoneNumber);
        setPhotoUrl(photoUrl);
        setEmail(email);
    }


    public User(String name, String phoneNumber) {
        setName(name);
        setPhoneNumber(phoneNumber);
    }
    public void setName(String name) {

        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
