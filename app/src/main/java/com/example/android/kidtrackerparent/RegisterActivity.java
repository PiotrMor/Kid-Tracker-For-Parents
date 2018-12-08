package com.example.android.kidtrackerparent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.kidtrackerparent.Enums.AccountType;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText mName;
    private EditText mSurname;
    private EditText mEmail;
    private EditText mPassword;
    private AccountType mAccountType;
    private Button mRegisterButton;

    private boolean isInputDataCorrect = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setReferencesToViews();
        addRegisterButtonOnClick();
    }

    private void addRegisterButtonOnClick() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserInput();
                if (isInputDataCorrect) {

                }
            }
        });
    }

    private void checkUserInput() {
        checkPersonalData();
        checkEmail();
        checkPassword();
        checkRadioGroup();
    }

    private void checkRadioGroup() {
        if (mAccountType == null) {
            isInputDataCorrect = true;
            Log.d(TAG, "checkRadioGroup: zaznacz cos");
        }
    }

    private void checkPassword() {
        if (mPassword.getText().toString().length() < 6) {
            isInputDataCorrect = false;
            Log.d(TAG, "checkPassword: zle haslo");
        }
    }

    private void checkEmail() {
        if (!mEmail.getText().toString().contains("@")) {
            isInputDataCorrect = false;
            Log.d(TAG, "checkEmail: zly email");
        }
    }


    private void checkPersonalData() {
        if (mName.getText().toString().equals("")) {
            isInputDataCorrect = false;
            Log.d(TAG, "checkPersonalData: name not set");
        }

        if (mSurname.getText().toString().equals("")) {
            isInputDataCorrect = false;
            Log.d(TAG, "checkPersonalData: surname not set");
        }
    }


    private void setReferencesToViews() {
        mName = findViewById(R.id.et_name_register);
        mSurname = findViewById(R.id.et_surname_register);
        mEmail = findViewById(R.id.et_email_register);
        mPassword = findViewById(R.id.et_password_register);
        mRegisterButton = findViewById(R.id.button_register);
    }


    public void onRadioButtonClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.radio_kid:
                mAccountType = AccountType.KID;
                break;

            case R.id.radio_parent:
                mAccountType = AccountType.PARENT;
                break;
        }
    }
}