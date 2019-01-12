package com.example.android.kidtrackerparent.Utils;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.BasicClasses.SerializableLatLng;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parsers {

    public static ArrayList<LatLng> parseLatLngListFromSerializable(List<SerializableLatLng> serializableLatLngList) {
        ArrayList<LatLng> latLngList = new ArrayList<>();

        for (SerializableLatLng serializableLatLng : serializableLatLngList) {
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

    public static List<SerializableLatLng> parseJsonArrayToLatLngList(JSONArray array) throws JSONException {
        List<SerializableLatLng> positions = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject positionJson = array.getJSONObject(i);
            positions.add(new SerializableLatLng(positionJson.getDouble("lat"), positionJson.getDouble("lng")));
        }
        return positions;
    }
}
