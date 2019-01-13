package com.example.android.kidtrackerparent.BasicClasses;

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

    public Kid(String id, String name) {
        this.mId = id;
        this.mName = name;
    }


    public Kid(JSONObject jsonObject) {
        this.mId = JSONUtils.getUserIdFromJson(jsonObject);
        this.mName = JSONUtils.getValueFromJson(jsonObject, "name");
        this.mEmail = JSONUtils.getValueFromJson(jsonObject, "email");
        this.mColor = JSONUtils.getValueFromJson(jsonObject, "iconColor");

        try {
            JSONObject location = jsonObject.getJSONObject("location");
            JSONArray array = location.getJSONArray("coordinates");


        } catch (JSONException e) {
            e.printStackTrace();
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


    @Override
    public String toString() {
        return mId + " " + mName;
    }
}
