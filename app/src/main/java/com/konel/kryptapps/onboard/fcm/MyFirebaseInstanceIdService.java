package com.konel.kryptapps.onboard.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import timber.log.Timber;

/**
 * Created by tushargupta on 21/06/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Timber.d("Refresh Token: " + FirebaseInstanceId.getInstance().getToken());
    }
}
