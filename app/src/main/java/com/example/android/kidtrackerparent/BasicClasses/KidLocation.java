package com.example.android.kidtrackerparent.BasicClasses;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;

public class KidLocation {
    private final double longitude, latitude, accuracy;
    private final Timestamp timestamp;

    public KidLocation(double latitude, double longitude, double accuracy, Timestamp timestamp) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
