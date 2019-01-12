package com.example.android.kidtrackerparent.BasicClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SerializableLatLng implements Serializable, Parcelable {
    private double latitude;
    private double longitude;

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
