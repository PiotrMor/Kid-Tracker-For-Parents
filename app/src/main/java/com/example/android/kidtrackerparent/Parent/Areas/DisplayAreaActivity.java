package com.example.android.kidtrackerparent.Parent.Areas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

public class DisplayAreaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = DisplayAreaActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Area mArea;
    private Polygon mPolygon;

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
        drawAreaOnMap();

        mMap.addMarker(new MarkerOptions().position(getPolygonCenter(mPolygon)).title(mArea.getName())).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getPolygonCenter(mPolygon), 18));

    }


    private void drawAreaOnMap() {
        PolygonOptions polygonOptions = new PolygonOptions().strokeColor(Color.RED).fillColor(0x40ff0000).strokeWidth(5);
        polygonOptions.addAll(mArea.getLatLngList());
         mPolygon = mMap.addPolygon(polygonOptions);
    }

    private LatLng getPolygonCenter (Polygon polygon) {
        // x1 - lowest x coordinate, x2 - highest x coordinate, y1 - lowest y coordinate, y2 - highest y coordinate
        Double x1 = null, x2 = null, y1 = null, y2 = null;
        for (LatLng point: polygon.getPoints()) {
            if (x1 == null || point.latitude < x1) {
                x1 = point.latitude;
            }

            if (x2 == null || point.latitude > x2) {
                x2 = point.latitude;
            }

            if (y1 == null || point.longitude < y1) {
                y1 = point.longitude;
            }

            if (y2 == null || point.longitude > y2) {
                y2 = point.longitude;
            }
        }
        return new LatLng(x1 + ((x2 - x1) / 2), y1 + ((y2 - y1) / 2));
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
