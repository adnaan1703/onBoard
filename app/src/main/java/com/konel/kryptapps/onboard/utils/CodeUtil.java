package com.konel.kryptapps.onboard.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-06-21
 */
public class CodeUtil {

    private static CodeUtil codeUtilInstance;
    private AlertDialog alertDialog;

    private CodeUtil() {
    }

    public static CodeUtil getInstance() {
        if (null == codeUtilInstance) {
            codeUtilInstance = new CodeUtil();
        }
        return codeUtilInstance;
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = connectivityManager.getAllNetworks();
                NetworkInfo networkInfo;
                for (Network mNetwork : networks) {
                    networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }

            } else {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network", "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    public static String getHashFromEventTitle(String eventTitle) {

        int hash = 7;
        for (int i = 0; i < eventTitle.length(); i++) {
            hash = hash * 31 + eventTitle.charAt(i);
        }

        return String.valueOf(hash);
    }
}
