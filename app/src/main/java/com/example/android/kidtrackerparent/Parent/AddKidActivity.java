package com.example.android.kidtrackerparent.Parent;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.kidtrackerparent.NetwortUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.NetwortUtils.ResponseTuple;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import java.util.HashMap;

public class AddKidActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = AddKidActivity.class.getSimpleName();
    private static String mSelectedColor;
    private Button mAddKidButton;
    private EditText mNameEditText;
    private EditText mCodeEditText;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid);
        setReferencesToViews();
        populateSpinner();
    }

    private void setReferencesToViews() {
        mAddKidButton = findViewById(R.id.button_add_kid);
        mNameEditText = findViewById(R.id.et_kid_name);
        mCodeEditText = findViewById(R.id.et_kid_code);
        mSelectedColor = "niebieski";
    }

    private void populateSpinner() {
        Spinner spinner = findViewById(R.id.spinner_colors);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedColor = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mSelectedColor = parent.getItemAtPosition(0).toString();
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

    public void onAddButtonClicked(View view) {
        if (view.getId() == R.id.button_add_kid && areInputsCorrect()) {

            view.setClickable(false);
            new AddKidTask().execute();
        }
    }

    private boolean areInputsCorrect() {
        return !mNameEditText.getText().toString().isEmpty() && !mCodeEditText.getText().toString().isEmpty();
    }

    private class AddKidTask extends AsyncTask {

        private final String KEY_NAME = "name";
        private final String KEY_CODE = "code";
        private final String KEY_COLOR = "iconColor";

        @Override
        protected Object doInBackground(Object[] objects) {
            HashMap<String, String> map = new HashMap<>();
            map.put(KEY_NAME, mNameEditText.getText().toString());
            map.put(KEY_CODE, mCodeEditText.getText().toString());
            map.put(KEY_COLOR, getChosenColor());
            ResponseTuple response = BackEndServerUtils.performPostCall(BackEndServerUtils.SERVER_ADD_CHILDREN, map, PreferenceUtils.getSessionCookie(AddKidActivity.this));
            if (!response.getResponse().isEmpty()) {
                mToast = Toast.makeText(AddKidActivity.this, "Dodano dziecko", Toast.LENGTH_SHORT);
                mToast.show();
                Intent intent = new Intent(AddKidActivity.this, ParentMainActivity.class);
                startActivity(intent);
            } else {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(AddKidActivity.this, "Dane nie poprawne", Toast.LENGTH_SHORT);
                mToast.show();
            }
            return null;
        }

        private String getChosenColor() {
            Log.d(TAG, "getChosenColor: " + getString(R.string.color_blue_label) + " " + mSelectedColor);
            if (mSelectedColor.equals(getString(R.string.color_blue_label).toLowerCase())) {
                return "blue";
            }
            return "red";
        }

        @Override
        protected void onPostExecute(Object o) {
            mAddKidButton.setClickable(true);
        }
    }
}
