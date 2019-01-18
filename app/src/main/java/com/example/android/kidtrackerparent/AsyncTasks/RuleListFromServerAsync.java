package com.example.android.kidtrackerparent.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.BasicClasses.Rule;
import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RuleListFromServerAsync extends AsyncTask<Context, Void, ArrayList<Rule>> {
    private AsyncResponse<ArrayList<Rule>> mListener;
    private String mKidId;

    public static final String TAG = RuleListFromServerAsync.class.getSimpleName();

    public RuleListFromServerAsync(String kidId, AsyncResponse<ArrayList<Rule>> listener){
        mListener = listener;
        mKidId = kidId;
    }

    @Override
    protected ArrayList<Rule> doInBackground(Context... contexts) {
        String jsonString = BackEndServerUtils.performCall(
                BackEndServerUtils.SERVER_GET_RULES_FOR_KID + mKidId,
                PreferenceUtils.getSessionCookie(contexts[0]),
                BackEndServerUtils.REQUEST_GET);
        Log.d(TAG, "doInBackground: " + jsonString);
        if (!jsonString.isEmpty()) {
            ArrayList<Rule> ruleList = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    ruleList.add(new Rule(json));
                }
                return ruleList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Rule> rules) {
        if (rules != null) {
            mListener.onSuccess(rules);
        } else {
            mListener.onFailure();
        }
    }
}
