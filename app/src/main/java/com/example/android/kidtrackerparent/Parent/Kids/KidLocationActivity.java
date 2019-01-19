package com.example.android.kidtrackerparent.Parent.Kids;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.Parent.ParentMainActivity;
import com.example.android.kidtrackerparent.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class KidLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Kid mKid;
    private LatLng mLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_location);

        if (getIntent().hasExtra(ParentMainActivity.INTENT_EXTRA_KEY_KID)) {
            mKid = (Kid) getIntent().getSerializableExtra(ParentMainActivity.INTENT_EXTRA_KEY_KID);
            mLocation = new LatLng(mKid.getLatitude(), mKid.getLongitude());
        }

        setActionBarTitle();

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_kid_location);
        fragment.getMapAsync(this);
    }

    private void setActionBarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lokalizajca " + mKid.getName());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 16));
            mMap.addMarker(new MarkerOptions().position(mLocation).title(mKid.getName())).showInfoWindow();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
