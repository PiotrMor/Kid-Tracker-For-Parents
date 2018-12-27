package com.example.android.kidtrackerparent.Parent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DisplayAreaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = DisplayAreaActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Area mArea;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_display);
        mArea = (Area) getIntent().getSerializableExtra("area");
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_area_display);
        if (fragment != null) {
            fragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng position = new LatLng(mArea.getLatitude(), mArea.getLongitude());
        Log.d(TAG, "onMapReady: " + position);
        mMap.addMarker(new MarkerOptions().position(position).title(mArea.getName())).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
        CircleOptions circleOptions =  new CircleOptions()
                .center(position)
                .radius(mArea.getRadius())
                .fillColor(0x40ff0000)
                .strokeColor(Color.RED)
                .strokeWidth(5);

        mMap.addCircle(circleOptions);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
