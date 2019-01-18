package com.example.android.kidtrackerparent.KidUtils;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.NetworkUtils.ResponseTuple;
import com.example.android.kidtrackerparent.Utils.LocationUtils;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import java.util.HashMap;

public class SendLocationToServerAsync extends AsyncTask<Context, Void, Void> {
    public static final String TAG = SendLocationToServerAsync.class.getSimpleName();
    @Override
    protected Void doInBackground(Context... contexts) {
        Context context = contexts[0];
        Location location = LocationUtils.getLastLocation(context);
        if (location != null) {
            HashMap<String, String> params = new HashMap<>();
            params.put("latitude", location.getLatitude() + "");
            params.put("longitude", location.getLongitude() + "");
            ResponseTuple response = BackEndServerUtils.performPostCall(BackEndServerUtils.SERVER_SEND_KID_LOCATION, params, PreferenceUtils.getSessionCookie(context));
            Log.d(TAG, "doInBackground: " + params);
        }
        return null;
    }
}
