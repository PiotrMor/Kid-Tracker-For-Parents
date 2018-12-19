package com.example.android.kidtrackerparent.KidUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class LocationSenderUtilities {

    public static final String TAG = LocationSenderService.class.getSimpleName();

    private static final int INTERVAL_MINUTES = 1;
    private static final int INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(INTERVAL_MINUTES);
    private static final int FLEXTIME_SECONDS = 60;

    private static boolean sInitialized;

    synchronized public static void scheduleSendingLocation(@NonNull Context context) {
        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job job = dispatcher.newJobBuilder()
                .setService(LocationSenderService.class)
                .setTag(TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0,60))
                .build();

        dispatcher.mustSchedule(job);
        Log.d(TAG, "Start service");
        sInitialized = true;
    }
}
