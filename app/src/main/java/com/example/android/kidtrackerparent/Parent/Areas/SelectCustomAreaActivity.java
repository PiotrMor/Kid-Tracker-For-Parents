package com.example.android.kidtrackerparent.Parent.Areas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.android.kidtrackerparent.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;
import java.util.List;

public class SelectCustomAreaActivity extends AppCompatActivity implements OnMapReadyCallback {

    public final static String TAG = SelectCustomAreaActivity.class.getSimpleName();

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleMap mMap;
    Marker mMarker;
    List<LatLng> mPoints;
    Polyline mPolyline;
    private Button mConfirmButton;
    private Button mCancelButton;
    private Button mBackButton;
    private Polygon mPolygon;

    @Override
    protected void onStart() {
        super.onStart();

        if (mPolygon != null) {
            mConfirmButton.setVisibility(View.VISIBLE);
            mCancelButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_custom_area);

        mPoints = new LinkedList<>();
        mConfirmButton = findViewById(R.id.button_confirm);
        mCancelButton = findViewById(R.id.button_cancel);
        mBackButton = findViewById(R.id.button_back);

        addButtonsOnClicks();


        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_google_map);
        fragment.getMapAsync(this);
    }

    private void addButtonsOnClicks() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPoints.remove(mPoints.size() - 1);
                mPolyline.setPoints(mPoints);
                mMarker.setPosition(mPoints.get(mPoints.size() - 1));
                if (mPoints.size() < 2) {
                    mBackButton.setVisibility(View.INVISIBLE);

                }

                if (mPoints.size() < 3) {
                    mConfirmButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPolygon == null) {
                    mMarker.remove();
                    mMarker = null;
                    mPolyline.remove();
                    mPolyline = null;
                } else {
                    mPolygon.remove();
                    mPolygon = null;
                }

                mCancelButton.setVisibility(View.INVISIBLE);
                mBackButton.setVisibility(View.INVISIBLE);
                mConfirmButton.setVisibility(View.INVISIBLE);
            }
        });

        

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPolygon == null) {
                    mPolyline.remove();
                    mPolyline = null;
                    mMarker.remove();
                    mMarker = null;
                    mBackButton.setVisibility(View.INVISIBLE);
                    mPolygon = mMap.addPolygon(new PolygonOptions().addAll(mPoints)
                            .strokeColor(Color.GREEN)
                                .fillColor(0x7F00FF00));
                } else {
                    mCancelButton.setVisibility(View.INVISIBLE);
                    mConfirmButton.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(SelectCustomAreaActivity.this, AddAreaActivity.class);
                    startActivity(intent);
                    // TODO coś tu zrobić
                }
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_add_area_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.search_places:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 18));
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMarker == null) {
                    mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                    mPoints = new LinkedList<>();
                    mCancelButton.setVisibility(View.VISIBLE);
                    mPolyline = mMap.addPolyline(new PolylineOptions().color(Color.RED));

                } else {
                    mMarker.setPosition(latLng);
                }

                mPoints.add(latLng);
                if (mPoints.size() > 1) {
                    mPolyline.setPoints(mPoints);
                }

                if (mPoints.size() == 2) {
                    mBackButton.setVisibility(View.VISIBLE);
                    mConfirmButton.setVisibility(View.INVISIBLE);
                }

                if (mPoints.size() == 3) {
                    mConfirmButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
