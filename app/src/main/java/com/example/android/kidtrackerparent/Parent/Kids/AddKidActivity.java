package com.example.android.kidtrackerparent.Parent.Kids;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.NetworkUtils.ResponseTuple;
import com.example.android.kidtrackerparent.Parent.ParentMainActivity;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import java.util.HashMap;

public class AddKidActivity extends AppCompatActivity {

    public static final String TAG = AddKidActivity.class.getSimpleName();
    private Button mAddKidButton;
    private EditText mNameEditText;
    private EditText mCodeEditText;
    private TextView mIconPreview;
    private Toast mToast;
    private ColorPickerDialog mColorPicker;
    private int mSelectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kid);
        setReferencesToViews();
        setIconPreview();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dodaj dziecko");
        }
    }

    private void setIconPreview() {
        mSelectedColor = R.color.color1;

        mColorPicker = new ColorPickerDialog();
        int[] colors = getResources().getIntArray(R.array.picker_colors);
        mColorPicker.initialize(R.string.select_color_title, colors, R.color.color1, 4, colors.length);
        mColorPicker.setSelectedColor(R.color.color1);
        mColorPicker.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mIconPreview.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

                mSelectedColor = color;
            }
        });

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    mIconPreview.setText(s.toString().substring(0, 1).toUpperCase());
                }
            }
        });
        mIconPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorPicker.show(getFragmentManager(), "TAG");
            }
        });
    }

    private void setReferencesToViews() {
        mAddKidButton = findViewById(R.id.button_add_kid);
        mNameEditText = findViewById(R.id.et_kid_name);
        mCodeEditText = findViewById(R.id.et_kid_code);
        mIconPreview = findViewById(R.id.tv_icon_preview);
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
            map.put(KEY_COLOR, "#" + Integer.toHexString(mSelectedColor).substring(2));
            Log.d(TAG, "doInBackground: " + map);
            ResponseTuple response = BackEndServerUtils.performPostCall(BackEndServerUtils.SERVER_ADD_CHILDREN, map, PreferenceUtils.getSessionCookie(AddKidActivity.this));
            if (!response.getResponse().isEmpty()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToastMessage("Dodano dziecko");
                    }
                });
                finish();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToastMessage("Wystąpił błąd");
                    }
                });
            }
            return null;
        }



        @Override
        protected void onPostExecute(Object o) {
            mAddKidButton.setClickable(true);
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
