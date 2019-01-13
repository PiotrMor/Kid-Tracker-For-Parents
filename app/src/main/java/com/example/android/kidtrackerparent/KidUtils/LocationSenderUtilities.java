package com.example.android.kidtrackerparent.KidUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class LocationSenderUtilities {

    public static final String TAG = LocationSenderService.class.getSimpleName();

    private static final String JOB_TAG = "location-service";
    private static final int INTERVAL_MINUTES = 2;
    private static final int INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(INTERVAL_MINUTES);
    private static final int FLEXTIME_SECONDS = 120;

    private static boolean sInitialized;

    synchronized public static void scheduleSendingLocation(@NonNull Context context) {
        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job job = dispatcher.newJobBuilder()
                .setService(LocationSenderService.class)
                .setTag(JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setTrigger(Trigger.executionWindow(INTERVAL_MINUTES,FLEXTIME_SECONDS))
                .build();

        dispatcher.mustSchedule(job);
        Log.d(TAG, "Start service");
        sInitialized = true;
    }

    synchronized public static void cancelSendingLocation(Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        dispatcher.cancel(JOB_TAG);
        sInitialized = false;
        Log.d(TAG, "cancelSendingLocation: anulowano");
    }
}
