package com.example.android.kidtrackerparent.BasicClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.kidtrackerparent.Utils.JSONUtils;
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
        this.id = JSONUtils.getUserIdFromJson(jsonObject);
        this.name = JSONUtils.getValueFromJson(jsonObject, "name");
        this.iconId = JSONUtils.getValueFromJson(jsonObject, "iconId");
        try {
            JSONObject location = jsonObject.getJSONObject("location");
            JSONArray array = location.getJSONArray("coordinates");
            mPositionPoints = parseJsonArrayToList(array);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private List parseJsonArrayToList(JSONArray array) throws JSONException {
        List<SerializableLatLng> positions = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject positionJson = array.getJSONObject(i);
            positions.add(new SerializableLatLng(positionJson.getDouble("lat"), positionJson.getDouble("lng")));
        }
        return positions;
    }

    @Override
    public String toString() {
        return "Area{" +
                "name='" + name + '\'' +
                ", mPositionPoints=" + mPositionPoints +
                '}';
    }

    public static class SerializableLatLng implements Serializable, Parcelable {
        double latitude;
        double longitude;

        public SerializableLatLng(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        protected SerializableLatLng(Parcel in) {
            latitude = in.readDouble();
            longitude = in.readDouble();
        }

        public static final Creator<SerializableLatLng> CREATOR = new Creator<SerializableLatLng>() {
            @Override
            public SerializableLatLng createFromParcel(Parcel in) {
                return new SerializableLatLng(in);
            }

            @Override
            public SerializableLatLng[] newArray(int size) {
                return new SerializableLatLng[size];
            }
        };

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }



        @Override
        public String toString() {
            return "SerializableLatLng{" +
                    "lat=" + latitude +
                    ", lng=" + longitude +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(latitude);
            dest.writeDouble(longitude);
        }
    }
}
