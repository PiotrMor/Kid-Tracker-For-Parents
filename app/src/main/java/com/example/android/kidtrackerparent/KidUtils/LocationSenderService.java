package com.example.android.kidtrackerparent.KidUtils;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.kidtrackerparent.NetwortUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.LocationUtils;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LocationSenderService extends JobService {
    public final static String TAG = LocationSenderService.class.getSimpleName();
    private static final String NOTIFICATION_CHANNEL_ID = "default channel";


    @Override
    public boolean onStartJob(final JobParameters job) {
        Location location = LocationUtils.getLastLocation(LocationSenderService.this);
        if (location != null) {
            HashMap<String, String> params = new HashMap<>();
            params.put("latitude", location.getLatitude() + "");
            params.put("longitude", location.getLatitude() + "");
            sendNotification(params + " dziala");

            SendLocationToServerAsync mBackgroundTask = new SendLocationToServerAsync();
            mBackgroundTask.execute(this);
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        return true;
    }


    private void sendNotification(String message) {

        Log.d(TAG, "PLZZZZZZZZZZZZZZZZ");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "default channel";
            String description = "Normalny kanał powiadomień";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Wydarzenie geofence")
                .setContentText(message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(0, mBuilder.build());

    }


}
