package com.example.android.kidtrackerparent.Utils;

import android.util.Log;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.BasicClasses.SerializableLatLng;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Parsers {

    public static final String TAG = Parsers.class.getSimpleName();

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

    public static String parseLocationTimeToString(String locationTime) {

        if (locationTime != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = null;
            try {
                date = format.parse(locationTime);
                SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
                return myFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getTimeFromDateString(String dateString) {
        if (dateString != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date date = format.parse(dateString);
                SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm");
                return myFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getDateFromString(String dateString) {
        if (dateString != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date date = format.parse(dateString);
                SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
                return myFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
