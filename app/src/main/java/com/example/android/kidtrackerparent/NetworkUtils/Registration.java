package com.example.android.kidtrackerparent.NetworkUtils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.kidtrackerparent.Enums.AccountType;

import java.util.HashMap;

public class Registration extends AsyncTask<Void, Void, ResponseTuple> {
    public static final String TAG = Registration.class.getSimpleName();
    private String firstName;
    private String secondName;
    private String email;
    private String password;
    private AccountType accountType;
    public AsyncResponse delegate = null;

    public Registration(@NonNull String firstName, @NonNull String secondName, @NonNull String email, @NonNull String password, @NonNull AccountType accountType, @NonNull AsyncResponse asyncResponse) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.delegate = asyncResponse;

    }

    @Override
    protected ResponseTuple doInBackground(Void... void_) {
        HashMap<String, String> params = createParamsForPostCall();
        if (accountType == AccountType.PARENT) {
            return BackEndServerUtils.performPostCall(BackEndServerUtils.SERVER_REGISTER_PARENT, params,null);
        } else if (accountType == AccountType.KID) {
            return BackEndServerUtils.performPostCall(BackEndServerUtils.SERVER_REGISTER_CHILD, params, null);
        }

        return BackEndServerUtils.performPostCall(BackEndServerUtils.SERVER_REGISTER_PARENT, params, null);
    }

    @Override
    protected void onPostExecute(ResponseTuple responseTuple) {
        delegate.processFinish(responseTuple);
    }

    private HashMap<String, String> createParamsForPostCall() {
        HashMap<String, String> params = new HashMap<>();
        params.put("firstName", firstName);
        params.put("lastName", secondName);
        params.put("password", password);
        params.put("email", email);
        return params;
    }

    public interface AsyncResponse {
        void processFinish(ResponseTuple tuple);
    }
}
