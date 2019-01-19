package com.example.android.kidtrackerparent.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.NetworkUtils.ResponseTuple;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import org.json.JSONObject;

public class AddRuleAsync extends AsyncTask<JSONObject,Void,String> {
    private AsyncResponse<String> mListener;
    private Context mContext;
    public AddRuleAsync(Context context, AsyncResponse<String> listener){
        mContext = context;
        mListener = listener;
    }


    @Override
    protected String doInBackground(JSONObject... jsonObjects) {
        ResponseTuple responseTuple = BackEndServerUtils.performPostCall(
                BackEndServerUtils.SERVER_ADD_RULE,
                jsonObjects[0],
                PreferenceUtils.getSessionCookie(mContext)
        );

        return responseTuple.getResponse();
    }

    @Override
    protected void onPostExecute(String s) {
        if (!s.isEmpty()) {
            mListener.onSuccess(s);
        } else {
            mListener.onFailure();
        }
    }
}
