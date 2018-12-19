package com.example.android.kidtrackerparent.Utils;

import android.util.Log;

import java.util.Arrays;

public class CookieUtils {
    public static final String TAG = CookieUtils.class.getSimpleName();

    public static String getSessionFromCookie(String cookie) {
        String[] cookies = cookie.split("; ");
        String result = "";
        for (String str : cookies) {
            Log.d(TAG, "getSessionFromCookie: " + str);
            if (str.matches("session=.*")) {
                result += str;
            } else if (str.matches("httponlysession.sig=.*")) {
                result += str.replace("httponly", ";");
            }
            
        }
        Log.d(TAG, "getSessionFromCookie: " + result);
        return result;
    }
}
