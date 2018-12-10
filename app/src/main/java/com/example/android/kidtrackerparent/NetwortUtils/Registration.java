package com.example.android.kidtrackerparent.NetwortUtils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

public class Registration extends AsyncTask<Void, Void, ResponseTuple> {
    public static final String TAG = Registration.class.getSimpleName();
    private String firstName;
    private String secondName;
    private String email;
    private String password;

    public Registration(@NonNull String firstName, @NonNull String secondName, @NonNull String email, @NonNull String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.password = password;
    }

    @Override
    protected ResponseTuple doInBackground(Void... vvoid) {
        HashMap<String, String> params = createParamsForPostCall();
        return BackEndServerUtils.performPostCall(BackEndServerUtils.SERVER_REGISTER, params);
    }



    private HashMap<String, String> createParamsForPostCall() {
        HashMap<String, String> params = new HashMap<>();
        params.put("firstName", firstName);
        params.put("lastName", secondName);
        params.put("password", password);
        params.put("email", email);
        return params;
    }
}
