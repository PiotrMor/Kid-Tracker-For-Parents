package com.example.android.kidtrackerparent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.kidtrackerparent.Enums.AccountType;
import com.example.android.kidtrackerparent.NetworkUtils.Registration;
import com.example.android.kidtrackerparent.NetworkUtils.ResponseTuple;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegisterActivity extends AppCompatActivity implements Registration.AsyncResponse {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText mName;
    private EditText mSurname;
    private EditText mEmail;
    private EditText mPassword;
    private AccountType mAccountType;
    private Button mRegisterButton;

    private Toast mToast;

    private boolean isInputDataCorrect = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);

        setReferencesToViews();
        addRegisterButtonOnClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addRegisterButtonOnClick() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInputDataCorrect = true;
                checkUserInput();
                if (isInputDataCorrect) {
                    registerUser();
                }
            }
        });
    }

    private void registerUser() {
        Registration registration = new Registration(
                getText(mName),
                getText(mSurname),
                getText(mEmail),
                getText(mPassword),
                mAccountType,
                this);
        registration.execute();
    }



    private String getText(EditText view) {
        return view.getText().toString();
    }

    private void checkUserInput() {
        checkPersonalData();
        checkEmail();
        checkPassword();
        checkAccountType();
    }

    private void checkPassword() {
        if (mPassword.getText().toString().length() < 6) {
            isInputDataCorrect = false;
            showToastMessage("Hasło powinno mieć przynajmniej 6 znaków");
        }
    }

    private void checkEmail() {
        if (!mEmail.getText().toString().contains("@")) {
            isInputDataCorrect = false;
            showToastMessage("Zły adres email");
        }
    }

    private void checkPersonalData() {
        if (mName.getText().toString().isEmpty()) {
            isInputDataCorrect = false;
            showToastMessage("Wpisz imię");
        }

        if (mSurname.getText().toString().isEmpty()) {
            isInputDataCorrect = false;
            showToastMessage("Wpisz nazwisko");
        }
    }


    private void setReferencesToViews() {
        mName = findViewById(R.id.et_name_register);
        mSurname = findViewById(R.id.et_surname_register);
        mEmail = findViewById(R.id.et_email_register);
        mPassword = findViewById(R.id.et_password_register);
        mRegisterButton = findViewById(R.id.button_register);
    }

    private void checkAccountType() {
        if (mAccountType == null) {
            isInputDataCorrect = false;
            showToastMessage("Wybierz rodzaj konta");
        }
    }


    public void onRadioButtonClicked(View view) {
        int id = view.getId();
        if (id == R.id.radio_kid) {
            mAccountType = AccountType.KID;
        } else if (id == R.id.radio_parent) {
            mAccountType = AccountType.PARENT;
        }
    }

    @Override
    public void processFinish(ResponseTuple tuple) {
        if (!tuple.getResponse().isEmpty()) {

            Toast.makeText(this,"Zarejestrowano", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            showToastMessage("Błąd przy rejestracji");
        }
    }

    private void showToastMessage(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();
    }
}
