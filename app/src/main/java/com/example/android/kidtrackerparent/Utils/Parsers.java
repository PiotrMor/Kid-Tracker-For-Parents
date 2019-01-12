package com.example.android.kidtrackerparent.Utils;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parsers {

    public static ArrayList<LatLng> parseLatLngListFromSerializable(List<Area.SerializableLatLng> serializableLatLngList) {
        ArrayList<LatLng> latLngList = new ArrayList<>();

        for (Area.SerializableLatLng serializableLatLng : serializableLatLngList) {
            latLngList.add(new LatLng(serializableLatLng.getLatitude(), serializableLatLng.getLongitude()));
        }
        return latLngList;
    }

    public static JSONArray parseLatLngListToJsonArray(List<LatLng> latLngList) throws JSONException {
        JSONArray array = new JSONArray();
        for (LatLng latLng : latLngList) {
            JSONObject object = new JSONObject();
            object.put("lat", latLng.latitude);
            object.put("lng", latLng.longitude);
            array.put(object);
        }
        return array;
    }
}
