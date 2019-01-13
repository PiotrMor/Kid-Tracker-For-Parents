package com.example.android.kidtrackerparent.Kid;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.kidtrackerparent.KidUtils.LocationSenderService;
import com.example.android.kidtrackerparent.KidUtils.LocationSenderUtilities;
import com.example.android.kidtrackerparent.LoginActivity;
import com.example.android.kidtrackerparent.NetwortUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.NetwortUtils.ResponseTuple;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.LocationUtils;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;
import com.google.android.gms.maps.MapFragment;

import java.util.HashMap;
import java.util.Map;

public class KidMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DisplayCodeFragment.OnFragmentInteractionListener {

    public static final String TAG = KidMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        Log.d(TAG, "onCreate: ");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_kid);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.kid_fragment_container, new KidMapFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_map);

        LocationSenderUtilities.scheduleSendingLocation(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.kid_fragment_container, new KidMapFragment()).commit();
                break;
            case R.id.nav_code:
                getSupportFragmentManager().beginTransaction().replace(R.id.kid_fragment_container, new DisplayCodeFragment()).commit();
                break;
            case R.id.nav_logout:
                LocationSenderUtilities.cancelSendingLocation(this);
                logoutFromAccount();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutFromAccount() {
        PreferenceUtils.addSessionCookie(this, null);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
