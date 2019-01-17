package com.example.android.kidtrackerparent.BasicClasses;

import android.util.Log;

import org.json.JSONObject;

public class Rule {
    public static final String TAG = Rule.class.getSimpleName();

    public static final String REPETITION_NONE = "NODE";
    public static final String REPETITION_DAILY = "DAILY";
    public static final String REPETITION_WEEKLY = "WEEKLY";
    public static final String REPETITION_MONTHLY = "MONTHLY";
    public static final String REPETITION_YEARLY = "YEARLY";
    public static final String REPETITION_WORKDAYS = "WORKDAYS";
    public static final String REPETITION_WEEKENDS = "WEEKENDS";

    private String mRuleId;
    private String mAreaId;
    private String mRepetition;
    private String mStartDate;
    private String mEndDate;
    private String mStartTime;
    private String mEndTime;
    private boolean mActive;

    public Rule(JSONObject jsonObject) {
        Log.d(TAG, "Rule: " + jsonObject);
    }
}
