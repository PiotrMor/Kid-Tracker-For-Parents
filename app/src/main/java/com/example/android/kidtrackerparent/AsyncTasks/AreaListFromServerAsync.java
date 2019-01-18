package com.example.android.kidtrackerparent.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AreaListFromServerAsync extends AsyncTask<Context, Void, ArrayList<Area>> {
    AsyncResponse<ArrayList<Area>> mListener;
    public AreaListFromServerAsync(AsyncResponse<ArrayList<Area>> listener){
        mListener = listener;
    }

    @Override
    protected ArrayList<Area> doInBackground(Context... contexts) {
        String jsonString = BackEndServerUtils.performCall(BackEndServerUtils.SERVER_GET_AREAS,
                PreferenceUtils.getSessionCookie(contexts[0]),
                BackEndServerUtils.REQUEST_GET);
        if (!jsonString.isEmpty()) {
            ArrayList<Area> areaList = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    areaList.add(new Area(json));
                }
                return areaList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Area> areas) {
        if (areas != null) {
            mListener.onSuccess(areas);
        } else {
            mListener.onFailure();
        }
    }
}
