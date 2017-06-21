package com.konel.kryptapps.onboard;

import android.app.Application;

import com.konel.kryptapps.onboard.utils.PreferenceUtil;

import timber.log.Timber;

/**
 * Created by tushargupta on 21/06/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtil.init(this);
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return super.createStackElementTag(element) + "|" + element.getMethodName() + "|" + element.getLineNumber() + "|";
            }
        });
    }
}
