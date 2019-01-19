package com.example.android.kidtrackerparent.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

public class ChangeRuleStateAsync extends AsyncTask<String, Void, String> {
    Context mContext;
    AsyncResponse<String> mListener;
    public ChangeRuleStateAsync(Context context, AsyncResponse<String> listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {

        return BackEndServerUtils.performPutCall(
                BackEndServerUtils.SERVER_PUT_CHANGE_RULE_STATE + strings[0] + "/active",
                null,
                PreferenceUtils.getSessionCookie(mContext));
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
