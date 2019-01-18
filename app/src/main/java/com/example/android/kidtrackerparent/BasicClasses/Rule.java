package com.example.android.kidtrackerparent.BasicClasses;

import android.util.Log;

import com.example.android.kidtrackerparent.Utils.JSONUtils;
import com.example.android.kidtrackerparent.Utils.Parsers;

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
        mActive = Boolean.parseBoolean(JSONUtils.getValueFromJson(jsonObject, "active"));
        mRuleId = JSONUtils.getValueFromJson(jsonObject, "_id");
        mRepetition = JSONUtils.getValueFromJson(jsonObject, "repetition");
        mAreaId = JSONUtils.getValueFromJson(jsonObject, "areaId");

        String startDate = JSONUtils.getValueFromJson(jsonObject, "startDate");
        mStartDate = Parsers.getDateFromString(startDate);
        mStartTime = Parsers.getTimeFromDateString(startDate);

        String endDate = JSONUtils.getValueFromJson(jsonObject, "endDate");
        mEndDate = Parsers.getDateFromString(endDate);
        mEndTime = Parsers.getTimeFromDateString(endDate);
        Log.d(TAG, "Rule: " + toString());
    }

    public String getmRuleId() {
        return mRuleId;
    }

    public String getmAreaId() {
        return mAreaId;
    }

    public String getmRepetition() {
        return mRepetition;
    }

    public String getmStartDate() {
        return mStartDate;
    }

    public String getmEndDate() {
        return mEndDate;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public String getmEndTime() {
        return mEndTime;
    }

    public boolean ismActive() {
        return mActive;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "mRuleId='" + mRuleId + '\'' +
                ", mAreaId='" + mAreaId + '\'' +
                ", mRepetition='" + mRepetition + '\'' +
                ", mStartDate='" + mStartDate + '\'' +
                ", mEndDate='" + mEndDate + '\'' +
                ", mStartTime='" + mStartTime + '\'' +
                ", mEndTime='" + mEndTime + '\'' +
                ", mActive=" + mActive +
                '}';
    }
}
