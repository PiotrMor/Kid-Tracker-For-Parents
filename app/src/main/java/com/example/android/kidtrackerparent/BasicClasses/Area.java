package com.example.android.kidtrackerparent.BasicClasses;

import android.util.Log;

import com.example.android.kidtrackerparent.Utils.JSONUtils;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Area {
    public final static String TAG = Area.class.getSimpleName();

    private String name;
    private LatLng position;
    private int radius;
    private String id;
    private String iconId;

    public Area(String name, LatLng position, int radius) {
        this.name = name;
        this.position = position;
        this.radius = radius;
    }

    public Area(JSONObject jsonObject) {
        this.id = JSONUtils.getUserIdFromJson(jsonObject);
        this.name = JSONUtils.getValueFromJson(jsonObject, "name");
        this.radius = Integer.parseInt(JSONUtils.getValueFromJson(jsonObject, "radius"));
        this.iconId = JSONUtils.getValueFromJson(jsonObject, "iconId");

        try {
            JSONArray array = jsonObject.getJSONArray("coordinates");
            Log.d(TAG, "Area: " + array.get(0));
            position = new LatLng(array.getDouble(0),
                    array.getDouble(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getRadius() {
        return radius;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Area{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", radius=" + radius +
                ", id='" + id + '\'' +
                '}';
    }
}
