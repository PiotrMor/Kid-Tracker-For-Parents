package com.example.android.kidtrackerparent.BasicClasses;

import android.util.Log;

import com.example.android.kidtrackerparent.Utils.JSONUtils;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Area implements Serializable {
    public final static String TAG = Area.class.getSimpleName();

    private String name;
    private double latitude;
    private double longitude;
    private int radius;
    private String id;
    private String iconId;



    public Area(JSONObject jsonObject) {
        this.id = JSONUtils.getUserIdFromJson(jsonObject);
        this.name = JSONUtils.getValueFromJson(jsonObject, "name");
        this.radius = Integer.parseInt(JSONUtils.getValueFromJson(jsonObject, "radius"));
        this.iconId = JSONUtils.getValueFromJson(jsonObject, "iconId");

        try {
            JSONArray array = jsonObject.getJSONArray("coordinates");
            Log.d(TAG, "Area: " + array.get(0));
            latitude = array.getDouble(0);
            longitude = array.getDouble(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getRadius() {
        return radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }


}
