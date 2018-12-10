package com.example.android.kidtrackerparent.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import com.example.android.kidtrackerparent.R;

public class PreferenceUtils {
    private static SharedPreferences preferences;
    private static final String SESSION_KEY = "session";

    public static void addSessionCookie(Context context, String value){
        preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SESSION_KEY, value);
        editor.apply();
    }

    public static String getSessionCookie(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        return preferences.getString(SESSION_KEY, null);
    }
}
