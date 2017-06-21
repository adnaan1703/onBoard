package com.konel.kryptapps.onboard;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

/**
 * Created by tushargupta on 20/06/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Timber.d("From: " + remoteMessage.getFrom());
        Timber.d("Message data payload: " + remoteMessage.getData());
        Timber.d("Message Notification Body: " + remoteMessage.getNotification().getBody());
    }
}
