package com.example.android.kidtrackerparent;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.kidtrackerparent.NetwortUtils.BackEndServerUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReferencesToViews();
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mServerPost = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        String email = mEmailView.getText().toString();
                        String password = mPasswordView.getText().toString();
                        Log.d(TAG, "onClick: " + email + " " + password);
                        HashMap<String, String> params = new HashMap<>();
                        params.put("email", "admin@gmail.com");
                        params.put("password", "admin");
                        Log.d(TAG, "doInBackground: " + BackEndServerUtils.performPostCall(BackEndServerUtils.BACKEND_SERVER_LOGIN_AUTH, params));
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
            String idToken = account.getIdToken();

            //String response = BackEndServerUtils.performPostCall(BackEndServerUtils.performPostCall());


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


}

