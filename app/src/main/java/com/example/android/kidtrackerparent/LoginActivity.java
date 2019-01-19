package com.example.android.kidtrackerparent;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.kidtrackerparent.Enums.AccountType;
import com.example.android.kidtrackerparent.Kid.KidMainActivity;
import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.NetworkUtils.ResponseTuple;
import com.example.android.kidtrackerparent.Parent.ParentMainActivity;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();


    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int RC_SIGN_IN = 1;
    public static final String KEY_RESPONSE = "response";
    private GoogleSignInClient mGoogleSignInClient;

    // UI references.
    private EditText mPasswordView;
    private Button mSignInButton;
    private AutoCompleteTextView mEmailView;
    private ConstraintLayout mConstraintLayout;
    private ProgressBar mProgressBar;

    private AsyncTask mServerPost;
    private AsyncTask mServerGet;

    private AccountType mAccountType;

    private Toast mToast;
    private String mFirebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReferencesToViews();
        checkIfUserIsLoggedIn();
        getFirebaseToken();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void checkIfUserIsLoggedIn() {
        if (PreferenceUtils.getSessionCookie(this) != null) {
            mConstraintLayout.setVisibility(View.INVISIBLE);
            int accountTypeValue = PreferenceUtils.getIntegerPreference(this, PreferenceUtils.ACCOUNT_TYPE_KEY);
            if (accountTypeValue == AccountType.KID.getNumVal()) {
                mAccountType = AccountType.KID;
            } else if (accountTypeValue == AccountType.PARENT.getNumVal()) {
                mAccountType = AccountType.PARENT;
            }

            mServerGet = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    String response = BackEndServerUtils.performCall(BackEndServerUtils.SERVER_CURRENT_USER,
                            PreferenceUtils.getSessionCookie(LoginActivity.this),
                            BackEndServerUtils.REQUEST_GET);
                    if (!response.isEmpty()){
                        navigateToNextActivity(response);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                    return null;
                }
            };
            mServerGet.execute();
        } else {
            mConstraintLayout.setVisibility(View.VISIBLE);
        }
    }


    private void logInToServer(String email, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        if (mAccountType == AccountType.PARENT) {
            params.put("firebaseToken", mFirebaseToken);
        }

        String requestURL = getRequestURL();
        if (requestURL != null) {
            ResponseTuple response = BackEndServerUtils.performPostCall(requestURL, params, null);

            String responseBody = response.getResponse();
            String responseCookie = response.getCookie();

            if (!responseCookie.equals(BackEndServerUtils.NO_COOKIES)) {
                saveSessionIdAndAccountType(responseCookie);

                navigateToNextActivity(responseBody);
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



    private void saveSessionIdAndAccountType(String responseCookie) {
        setSession(responseCookie);
        PreferenceUtils.addIntegerPreference(
                        this,
                        PreferenceUtils.ACCOUNT_TYPE_KEY,
                        mAccountType.getNumVal());
    }

    private void navigateToNextActivity(String responseBody) {
        if (mAccountType == AccountType.PARENT) {
            navigateToParentActivity(responseBody);
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

    private void navigateToParentActivity(String responseBody) {
        Intent intent = new Intent(this, ParentMainActivity.class);
        intent.putExtra(KEY_RESPONSE, responseBody);
        startActivity(intent);
        finish();
    }

    private void setSession(String cookie) {
        PreferenceUtils.addSessionCookie(this, cookie);
    }


    private void setReferencesToViews() {
        mConstraintLayout = findViewById(R.id.constraintLayout_login);
        mProgressBar = findViewById(R.id.login_progress);
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
                            , map, null);

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

    void getFirebaseToken() {
        FirebaseMessaging.getInstance().subscribeToTopic("kidLocation")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: sub");
                        }
                    }
                });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        mFirebaseToken = task.getResult().getToken();
                        Log.d(TAG, "onComplete: token: " + mFirebaseToken );
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);
                        Toast.makeText(LoginActivity.this, "Dodano token", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

