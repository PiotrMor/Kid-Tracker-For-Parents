package com.example.android.kidtrackerparent.Parent.Areas;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.BasicClasses.SerializableLatLng;
import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.NetworkUtils.ResponseTuple;
import com.example.android.kidtrackerparent.Parent.ParentMainActivity;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.Parsers;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class AddAreaActivity extends AppCompatActivity {

    public final static String TAG = AddAreaActivity.class.getSimpleName();

    private ArrayList<LatLng> mArea;
    private String mAreaName;
    private ArrayList<Kid> mChosenKids;

    private EditText mAreaNameTextView;
    private Button mSaveButton;
    private Spinner mSpinnerIcons;
    private Spinner mSpinnerKids;

    private Toast mToast;
    private ArrayList<Icon> mIconList;
    private ArrayList<Kid> mKidList;
    private KidsSpinnerAdapter mKidsAdapter;

    private final String KEY_NAME = "name";
    private final String KEY_ICON_ID = "iconId";
    private final String KEY_CHILDREN = "children";
    private final String KEY_AREA = "area";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area);

        setViewReferences();

        setSaveButtonOnClick();

        initKidSpinner();
        initSpinnerElementsList();
        AreaIconSpinnerAdapter adapter = new AreaIconSpinnerAdapter(this, mIconList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerIcons.setAdapter(adapter);
        mArea = getAreaFromIntent();
        Log.d(TAG, "onCreate: " + mArea);
    }

    private ArrayList<LatLng> getAreaFromIntent() {
        Intent intent = getIntent();
        ArrayList<SerializableLatLng> serializableLatLngList =
                intent.getParcelableArrayListExtra(SelectCustomAreaActivity.INTENT_EXTRA_KEY_AREA);

        return Parsers.parseLatLngListFromSerializable(serializableLatLngList);
    }

    private void setViewReferences() {
        mSpinnerKids = findViewById(R.id.spinner_kids_list);
        mSpinnerIcons = findViewById(R.id.spinner_icons_list);
        mSaveButton = findViewById(R.id.button_save_area);
        mAreaNameTextView = findViewById(R.id.et_area_name);
    }

    private void setSaveButtonOnClick() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaveButton.setClickable(false);
                mChosenKids = mKidsAdapter.getChosenKids();

                if (isInputDataCorrect()) {
                    sendAreaToServer();
                }
                mSaveButton.setClickable(true);
            }

            private boolean isInputDataCorrect() {

                mAreaName = mAreaNameTextView.getText().toString();
                Log.d(TAG, "isInputDataCorrect: " + mAreaName);

                if (mAreaName.isEmpty()) {
                    displayToastMessage(getString(R.string.error_area_name_empty));
                    return false;
                } else if (mChosenKids == null || mChosenKids.size() == 0){
                    displayToastMessage("Wybierz dzieci");
                    return false;
                }
                return true;
            }
        });
    }



    private void sendAreaToServer() {
        new AsyncTask() {

            @Override
            protected String doInBackground(Object[] objects) {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_NAME, mAreaName);
                params.put(KEY_ICON_ID, getIdFromIconName(((Icon)mSpinnerIcons.getSelectedItem()).getName()));
                JSONArray area = null;
                try {
                    area = Parsers.parseLatLngListToJsonArray(mArea);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray kidsArray = new JSONArray();
                for (Kid kid : mChosenKids) {
                    kidsArray.put(kid.getId());
                }
                JSONObject paramsJson = new JSONObject(params);
                try {
                    paramsJson.put(KEY_CHILDREN, kidsArray);
                    paramsJson.put(KEY_AREA, area);
                } catch (JSONException e) {
                    e.printStackTrace();
                    displayToastMessage("Coś nie tak");
                    return null;
                }
                ResponseTuple responseTuple = BackEndServerUtils.performPostCall(
                        BackEndServerUtils.SERVER_ADD_AREA,
                        paramsJson,
                        PreferenceUtils.getSessionCookie(AddAreaActivity.this));
                Log.d(TAG, "doInBackground: " +responseTuple.getResponse());
                return responseTuple.getResponse();
            }

            @Override
            protected void onPostExecute(Object o) {
                String response = (String) o;
                if (!response.isEmpty()) {
                    Intent intent = new Intent(AddAreaActivity.this, ParentMainActivity.class);
                    startActivity(intent);
                } else {
                    displayToastMessage("Nie można utworzyć obszru");
                }
            }
        }.execute();
    }

    private void initKidSpinner() {
        populateKidList();
    }

    private void populateKidList() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {

                String jsonString = BackEndServerUtils.performCall(BackEndServerUtils.SERVER_GET_CHILDREN,
                        PreferenceUtils.getSessionCookie(AddAreaActivity.this),
                        BackEndServerUtils.REQUEST_GET);
                mKidList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        mKidList.add(new Kid(json));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mKidsAdapter = new KidsSpinnerAdapter(AddAreaActivity.this, mKidList);
                        mSpinnerKids.setAdapter(mKidsAdapter);
                    }
                });
                return null;
            }
        }.execute();
    }




    private void initSpinnerElementsList() {
        mIconList = new ArrayList<>();

        String[] labels = getResources().getStringArray(R.array.area_labels);
        TypedArray icons = getResources().obtainTypedArray(R.array.area_icons);
        for (int i = 0; i < labels.length; i++) {
            mIconList.add(new Icon(labels[i], icons.getDrawable(i)));
        }
        icons.recycle();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void displayToastMessage(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private String getIdFromIconName(String name) {
        if (name.equals(getString(R.string.area_book_icon))){
            return Area.ICON_BOOK;
        } else if (name.equals(getString(R.string.area_home_icon))) {
            return Area.ICON_HOME;
        } else if (name.equals(getString(R.string.area_building_icon))) {
            return Area.ICON_BUILDING;
        } else {
            return Area.ICON_WORK;
        }
    }

}
