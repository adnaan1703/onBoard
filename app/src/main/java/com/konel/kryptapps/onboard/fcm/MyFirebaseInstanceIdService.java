package com.konel.kryptapps.onboard.fcm;

import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.konel.kryptapps.onboard.model.User;
import com.konel.kryptapps.onboard.utils.PreferenceUtil;

import timber.log.Timber;

/**
 * Created by tushargupta on 21/06/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        PreferenceUtil.setString(PreferenceUtil.FCM_TOKEN, FirebaseInstanceId.getInstance().getToken());
        if (!TextUtils.isEmpty(PreferenceUtil.getString(PreferenceUtil.USER_ID))) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userCollection = database.getReference("users");
            DatabaseReference userObject = userCollection.child(PreferenceUtil.getString(PreferenceUtil.USER_ID));
            User user = new User(FirebaseInstanceId.getInstance().getToken());
            userObject.setValue(user);

        }
        Timber.d(FirebaseInstanceId.getInstance().getToken());
    }
}
