package com.example.android.kidtrackerparent;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.kidtrackerparent.Enums.AccountType;
import com.example.android.kidtrackerparent.Kid.KidMainActivity;
import com.example.android.kidtrackerparent.NetwortUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.NetwortUtils.ResponseTuple;
import com.example.android.kidtrackerparent.Parent.ParentMainActivity;
import com.example.android.kidtrackerparent.Utils.JSONUtils;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int RC_SIGN_IN = 1;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private GoogleSignInClient mGoogleSignInClient;

    // UI references.
    private EditText mPasswordView;

    private Button mSignInButton;
    private AutoCompleteTextView mEmailView;

    private AsyncTask mServerPost;

    private AccountType mAccountType;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfUserIsLoggedIn();
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setReferencesToViews();
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mServerPost = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        logInToServer(mEmailView.getText().toString(), mPasswordView.getText().toString());
                        return null;
                    }
                };
                mServerPost.execute();
            }
        });
        // Set up the login form.
        configureGoogleSignInButton();

        configureGoogleSignInClient();


    }

    private void checkIfUserIsLoggedIn() {
        if (PreferenceUtils.getSessionCookie(this) != null) {
            int accountTypeValue = PreferenceUtils.getIntegerPreference(this, PreferenceUtils.ACCOUNT_TYPE_KEY);
            if (accountTypeValue == AccountType.KID.getNumVal()) {
                mAccountType = AccountType.KID;
            } else if (accountTypeValue == AccountType.PARENT.getNumVal()) {
                mAccountType = AccountType.PARENT;
            }
            navigateToNextActivity();
        }
    }

    private void logInToServer(String email, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        String requestURL = getRequestURL();
        if (requestURL != null) {
            ResponseTuple response = BackEndServerUtils.performPostCall(requestURL, params);

            String responseBody = response.getResponse();
            String responseCookie = response.getCookie();
            
            retrieveIdFromResponse(responseBody);

            if (!responseCookie.equals(BackEndServerUtils.NO_COOKIES)) {
                saveSessionIdAndAccountType(responseCookie);

                navigateToNextActivity();
            }
        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToast = Toast.makeText(LoginActivity.this, "Wybierz rodzaj konta", Toast.LENGTH_LONG);
                    mToast.show();
                }
            });

        }

    }

    private void retrieveIdFromResponse(String responseBody) {
        try {
            Log.d(TAG, "retrieveIdFromResponse:  " + JSONUtils.getUserIdFromJson(new JSONObject(responseBody)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveSessionIdAndAccountType(String responseCookie) {
        setSession(responseCookie);
        PreferenceUtils.addIntegerPreference(
                        this,
                        PreferenceUtils.ACCOUNT_TYPE_KEY,
                        mAccountType.getNumVal());
    }

    private void navigateToNextActivity() {
        if (mAccountType == AccountType.PARENT) {
            navigateToParentActivity();
        } else if (mAccountType == AccountType.KID) {
            navigateToKidActivity();
        }
    }

    private void navigateToKidActivity() {
        Intent intent = new Intent(this, KidMainActivity.class);
        startActivity(intent);
        finish();
    }

    private String getRequestURL() {
        if (mAccountType == AccountType.PARENT) {
            return BackEndServerUtils.SERVER_LOGIN_PARENT;
        } else if (mAccountType == AccountType.KID) {
            return BackEndServerUtils.SERVER_LOGIN_KID;
        }
        return null;
    }

    private void navigateToParentActivity() {
        Intent intent = new Intent(this, ParentMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setSession(String cookie) {
        PreferenceUtils.addSessionCookie(this, cookie);
    }


    private void setReferencesToViews() {
        mSignInButton = findViewById(R.id.email_sign_in_button);
        mPasswordView = findViewById(R.id.et_password);
        mEmailView = findViewById(R.id.email);
    }

    private void configureGoogleSignInButton() {
        findViewById(R.id.google_sign_in_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        });
    }

    private void configureGoogleSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_FILE))
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        handleSignInResult(task);
                    }
                });
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            final String idToken = account.getIdToken();
            Log.d(TAG, "token: " + account.getServerAuthCode());

            AsyncTask getCall = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    HashMap <String, String> map = new HashMap<>();
                    map.put("access_token", "ya29.GlxuBoMwQBGC3F-8tBFoaXmfIjamvDnZvfzKE-xVNy1npF3hw9yunGmYpW6JkqONmRYUE1ghghJC5tPXh89R1brYr9tQlexWXObKCylSaxFc3vbkDtrByDFyBl01_Q");
                    ResponseTuple tuple = BackEndServerUtils.performPostCall(BackEndServerUtils.SERVER_GOOGLE_SIGN_IN
                            , map);

                    Log.d(TAG, "handleSignInResult: " + tuple.getCookie());
                    return null;
                }

            };
            getCall.execute();




            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            // TODO: user is logged in navigate to main activity
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        int id = view.getId();
        if (id == R.id.radio_kid) {
            mAccountType = AccountType.KID;
        } else if (id == R.id.radio_parent) {
            mAccountType = AccountType.PARENT;
        }
    }
}

