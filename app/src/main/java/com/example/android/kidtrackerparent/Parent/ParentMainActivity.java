package com.example.android.kidtrackerparent.Parent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.LoginActivity;
import com.example.android.kidtrackerparent.NetwortUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, KidsListFragment.OnListFragmentInteractionListener {

    public static final String TAG = ParentMainActivity.class.getSimpleName();

    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);
        getDataFromJson();


        setReferencesToViews();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new KidsListFragment()).commit();
        mNavigationView.setCheckedItem(R.id.nav_kids);

    }



    private void getDataFromJson() {
        Intent intent = getIntent();
        if (intent.hasExtra(LoginActivity.KEY_RESPONSE)) {

        }
    }

    private void setReferencesToViews() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView =  findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parent_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_kids:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new KidsListFragment()).commit();
                break;
            case R.id.nav_areas:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AreasListFragment()).commit();
                break;
            case R.id.nav_rules:
                break;
            case R.id.nav_logout:
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
    public void onListFragmentInteraction(Kid item) {
        Log.d(TAG, "onListFragmentInteraction: " + item.getName());
        Intent intent = new Intent(this, KidLocationActivity.class);
        startActivity(intent);
    }
}
