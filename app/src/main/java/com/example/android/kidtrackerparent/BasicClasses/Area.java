package com.example.android.kidtrackerparent.BasicClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.kidtrackerparent.Utils.JSONUtils;
import com.example.android.kidtrackerparent.Utils.Parsers;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Area implements Serializable {
    public final static String TAG = Area.class.getSimpleName();

    private String name;
    private List<SerializableLatLng> mPositionPoints;
    private String id;
    private String iconId;
    private List<Kid> mKids;

    public static final String ICON_HOME = "home";
    public static final String ICON_BUILDING = "building";
    public static final String ICON_WORK = "briefcase";
    public static final String ICON_BOOK = "book";

    public Area(JSONObject jsonObject) {
        this.id = JSONUtils.getValueFromJson(jsonObject, "_id");
        this.name = JSONUtils.getValueFromJson(jsonObject, "name");
        this.iconId = JSONUtils.getValueFromJson(jsonObject, "iconId");
        try {
            JSONObject location = jsonObject.getJSONObject("location");
            JSONArray array = location.getJSONArray("coordinates");
            mPositionPoints = Parsers.parseJsonArrayToLatLngList(array);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public List<Kid> getKids() {
        return mKids;
    }

    public List<SerializableLatLng> getPositionPoints() {
        return mPositionPoints;
    }

    public String getName() {
        return name;
    }

    public String getIconId() {
        return iconId;
    }

    public List<LatLng> getLatLngList() {
        List<LatLng> list = new ArrayList<>();
        for (SerializableLatLng latLng : mPositionPoints) {
            list.add(new LatLng(latLng.getLatitude(), latLng.getLongitude()));
        }
        return list;
    }



    @Override
    public String toString() {
        return "Area{" +
                "name='" + name + '\'' +
                ", mPositionPoints=" + mPositionPoints +
                '}';
    }

}
