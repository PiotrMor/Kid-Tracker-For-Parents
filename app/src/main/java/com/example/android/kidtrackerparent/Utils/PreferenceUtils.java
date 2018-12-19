package com.example.android.kidtrackerparent.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import com.example.android.kidtrackerparent.R;

public class PreferenceUtils {
    private static SharedPreferences preferences;
    private static final String SESSION_KEY = "session";
    public static final String ACCOUNT_TYPE_KEY = "account type";
    private static SharedPreferences.Editor mEditor;

    public static void addSessionCookie(Context context, String cookie){
        preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (cookie == null) {
            editor.putString(SESSION_KEY, null);

        } else {
            editor.putString(SESSION_KEY, CookieUtils.getSessionFromCookie(cookie));

        }
        editor.apply();
    }

    public static String getSessionCookie(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        return preferences.getString(SESSION_KEY, null);
    }

    public static void addStringPreference(Context context, String key, String value) {
        preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        mEditor = preferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public static void addIntegerPreference(Context context, String key, Integer value) {
        preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        mEditor = preferences.edit();
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public static void removePreference(Context context, String key) {
        preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        mEditor = preferences.edit();
        mEditor.remove(key);
        mEditor.apply();
    }

    public static int getIntegerPreference(Context context, String key) {
        preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        return preferences.getInt(key, -1);
    }

    public static String getStringPreference(Context context, String key) {
        preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }
}
