package com.example.android.kidtrackerparent;

import android.Manifest;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.android.kidtrackerparent.KidUtils.LocationSenderService;
import com.example.android.kidtrackerparent.KidUtils.LocationSenderUtilities;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class KidActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updateLastLocation();
        scheduleJob();
    }

    private void scheduleJob() {
        LocationSenderUtilities.scheduleSendingLocation(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLastLocation();
        // Add a marker in Sydney and move the camera
        if (mLastLocation != null){
            LatLng sydney = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Tu jest Jessica"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLastLocation();
        // Add a marker in Sydney and move the camera
        if (mLastLocation != null){
            LatLng sydney = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Tu jest Jessica"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mLastLocation = location;
                }
            }
        });
    }
}
