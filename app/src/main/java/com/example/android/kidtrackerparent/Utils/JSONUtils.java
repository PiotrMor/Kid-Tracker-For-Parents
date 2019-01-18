package com.example.android.kidtrackerparent.Utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    public static final String TAG = JSONUtils.class.getSimpleName();

    public static final String KEY_ID = "_id";

    public static String getUserIdFromJson(JSONObject json){
        String id = "";
        try {
            id = json.getString(KEY_ID);
            Log.d(TAG, "getUserIdFromJson: " + id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getUserIdFromJson: " + createJsonString(KEY_ID, id));
        return createJsonString(KEY_ID, id);
    }

    public static String getValueFromJsonString(String jsonString, String key) {
        try {
            return new JSONObject(jsonString).getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getValueFromJson(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            Log.d(TAG, "getValueFromJson: " + key + " jest puste");
        }
        return null;
    }

    private static String createJsonString(String key, String value) {
        return "\"" + key + "\"" + ":\"" + value + "\"";
    }
}
