package com.example.android.kidtrackerparent.Parent;

import android.content.Context;
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
import android.view.View;
import android.widget.TextView;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.LoginActivity;
import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.Parent.Areas.AreasListFragment;
import com.example.android.kidtrackerparent.Parent.Areas.DisplayAreaActivity;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class ParentMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        KidsListFragment.OnListFragmentInteractionListener,
        AreasListFragment.OnListFragmentInteractionListener {

    public static final String TAG = ParentMainActivity.class.getSimpleName();

    private NavigationView mNavigationView;
    private TextView mParentNameTextView;
    private TextView mParentMailTextVIew;

    public final static String INTENT_EXTRA_KEY_KID = "kid";
    public final static String INTENT_EXTRA_KEY_AREA = "area";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);
        getDataFromJson();
        setReferencesToViews();


        ObtainUserInfo task = new ObtainUserInfo(mParentNameTextView, mParentMailTextVIew);
        task.execute(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new KidsListFragment()).commit();
        mNavigationView.setCheckedItem(R.id.nav_kids);
    }


    private void getDataFromJson() {
        Intent intent = getIntent();
        if (intent.hasExtra(LoginActivity.KEY_RESPONSE)) {

        }
    }

    private void setReferencesToViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        View headerView = mNavigationView.getHeaderView(0);
        mParentNameTextView = headerView.findViewById(R.id.tv_parent_name);
        mParentMailTextVIew = headerView.findViewById(R.id.tv_parent_email);
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
                deleteToken();
                logoutFromAccount();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteToken() {
        DeleteFirebaseTokenAsync deleteTokenAsync = new DeleteFirebaseTokenAsync(
                PreferenceUtils.getSessionCookie(this));
        deleteTokenAsync.execute();
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
        intent.putExtra(INTENT_EXTRA_KEY_KID, item);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Area area) {
        Intent intent = new Intent(this, DisplayAreaActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY_AREA, area);
        startActivity(intent);
    }


    private static class ObtainUserInfo extends AsyncTask<Context, Void, String> {
        private WeakReference<TextView> nameTextView;
        private WeakReference<TextView> mailTextView;


        ObtainUserInfo(TextView nameTextView, TextView mailTextView) {
            this.nameTextView = new WeakReference<>(nameTextView);
            this.mailTextView = new WeakReference<>(mailTextView);
        }

        @Override
        protected String doInBackground(Context... contexts) {
            String result = BackEndServerUtils.performCall(BackEndServerUtils.SERVER_CURRENT_USER,
                    PreferenceUtils.getSessionCookie(contexts[0]),
                    BackEndServerUtils.REQUEST_GET);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Log.d(TAG, "onPostExecute: " + s);
                try {
                    JSONObject parentInfo = new JSONObject(s);
                    String name = parentInfo.getString("firstName") + " " + parentInfo.getString("lastName");
                    nameTextView.get().setText(name);
                    mailTextView.get().setText(parentInfo.getString("email"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class DeleteFirebaseTokenAsync extends AsyncTask<Void, Void, Void> {
        private String mCookie;

        DeleteFirebaseTokenAsync(String cookie) {
            mCookie = cookie;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BackEndServerUtils.performCall(BackEndServerUtils.SERVER_DELETE_FIREBASE_TOKEN,
                    mCookie,
                    BackEndServerUtils.REQUEST_DELETE);
            return null;
        }
    }
}
