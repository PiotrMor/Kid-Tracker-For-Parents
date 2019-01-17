package com.example.android.kidtrackerparent.BasicClasses;

import android.util.Log;

import com.example.android.kidtrackerparent.Utils.JSONUtils;
import com.example.android.kidtrackerparent.Utils.Parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Kid implements Serializable {

    private String mId;
    private String mName;
    private String mEmail;
    private String mColor;
    private double mLongitude;
    private double mLatitude;
    private String mLastLocationTime;

    public final static String TAG = Kid.class.getSimpleName();

    public Kid(String id, String name) {
        this.mId = id;
        this.mName = name;
    }


    public Kid(JSONObject jsonObject) {
        this.mId = JSONUtils.getUserIdFromJson(jsonObject);
        this.mName = JSONUtils.getValueFromJson(jsonObject, "name");
        this.mEmail = JSONUtils.getValueFromJson(jsonObject, "email");
        this.mColor = JSONUtils.getValueFromJson(jsonObject, "iconColor");
        this.mLastLocationTime = Parsers
                .parseLocationTimeToString(JSONUtils.getValueFromJson(jsonObject, "locationTime"));
        try {
            JSONObject location = jsonObject.getJSONObject("location");

            JSONArray coordinates = location.getJSONArray("coordinates");
            mLatitude = coordinates.getDouble(1);
            mLongitude = coordinates.getDouble(0);


        } catch (JSONException e) {
            Log.d(TAG, "Kid: location not set");
        }
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getColor() {
        return mColor;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getLastLocationTime() {
        return mLastLocationTime;
    }

    @Override
    public String toString() {
        return mId + " " + mName;
    }
}
