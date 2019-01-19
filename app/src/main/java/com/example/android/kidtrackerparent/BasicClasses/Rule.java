package com.example.android.kidtrackerparent.BasicClasses;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.JSONUtils;
import com.example.android.kidtrackerparent.Utils.Parsers;

import org.json.JSONObject;

public class Rule {
    public static final String TAG = Rule.class.getSimpleName();

    public static final String REPETITION_NONE = "NONE";
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

    //Optional
    private String mAreaName;
    private String mAreaIcon;

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

    public void setAreaIcon(String areaIcon) {
        mAreaIcon = areaIcon;
    }

    public void setAreaName(String areaName) {
        mAreaName = areaName;
    }

    public String getAreaName() {
        return mAreaName;
    }

    public String getAreaIcon() {
        return mAreaIcon;
    }

    public String getRuleId() {
        return mRuleId;
    }

    public String getAreaId() {
        return mAreaId;
    }

    public String getRepetition() {
        return mRepetition.toLowerCase();
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public boolean isActive() {
        return mActive;
    }

    public String getFullStartTime() {
        return mStartTime + " " + mStartDate;
    }

    public String getFullEndTime() {
        return mEndTime + " " + mEndDate;
    }

    public int getRepetitionResourceStringId() {
        switch (mRepetition) {
            case REPETITION_NONE:
                return R.string.rules_repetition_none;
            case REPETITION_DAILY:
                return R.string.rules_repetition_daily;
            case REPETITION_MONTHLY:
                return R.string.rules_repetition_monthly;
            case REPETITION_WEEKLY:
                return R.string.rules_repetition_weekly;
            case REPETITION_YEARLY:
                return R.string.rules_repetition_yearly;
            case REPETITION_WEEKENDS:
                return R.string.rules_repetition_weekends;
            case REPETITION_WORKDAYS:
                return R.string.rules_repetition_work_days;
            default:
                return R.string.rules_repetition_none;
        }
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

    public class Builder {

    }
}
