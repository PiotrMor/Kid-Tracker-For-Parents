package com.example.android.kidtrackerparent.Parent.Rules;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.kidtrackerparent.AsyncTasks.AddRuleAsync;
import com.example.android.kidtrackerparent.AsyncTasks.AreaListFromServerAsync;
import com.example.android.kidtrackerparent.AsyncTasks.AsyncResponse;
import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.BasicClasses.Rule;
import com.example.android.kidtrackerparent.Parent.ParentMainActivity;
import com.example.android.kidtrackerparent.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class AddRuleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, AsyncResponse<ArrayList<Area>> {

    public final static String TAG = AddRuleActivity.class.getSimpleName();

    private TextView mStartDateLabelTextView;
    private TextView mEndDateLabelTextView;
    private TextView mStartTimeLabelTextView;
    private TextView mEndTimeLabelTextView;

    private TextView mStartDateTextView;
    private TextView mEndDateTextView;
    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView;

    private Spinner mAreaSpinner;
    private Spinner mRepetitionSpinner;
    private View mSelectedView;
    private Button mAddRuleButton;

    private Calendar mCurrentTime;

    private Toast mToast;
    private Kid mKid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        Date date = new Date();
        obtainKidReferenceFromIntent();
        mCurrentTime = new GregorianCalendar();
        mCurrentTime.setTime(date);
        setReferencesToViews();

        setDateViewsOnClickListeners();
        setTimeViewsOnClickListeners();

        populateAreaSpinner();
        populateRepetitionSpinner();
        setAddRuleButtonOnClick();
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

    private void obtainKidReferenceFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(ParentMainActivity.INTENT_EXTRA_KEY_KID)) {
            mKid = (Kid) intent.getSerializableExtra(ParentMainActivity.INTENT_EXTRA_KEY_KID);
        }
    }

    private void populateRepetitionSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rules_repetition, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRepetitionSpinner.setAdapter(adapter);
    }

    private void populateAreaSpinner() {
        mAreaSpinner.setScaleY(0.8f);

        AreaListFromServerAsync task = new AreaListFromServerAsync(this);
        task.execute(this);
    }

    private void setTimeViewsOnClickListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedView = v;
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddRuleActivity.this,
                        AddRuleActivity.this,
                        mCurrentTime.get(Calendar.HOUR_OF_DAY),
                        mCurrentTime.get(Calendar.MINUTE),
                        true
                );
                timePickerDialog.show();
            }
        };
        mStartTimeTextView.setOnClickListener(listener);
        mEndTimeTextView.setOnClickListener(listener);
    }

    private void setDateViewsOnClickListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedView = v;
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddRuleActivity.this,
                        AddRuleActivity.this,
                        mCurrentTime.get(Calendar.YEAR),
                        mCurrentTime.get(Calendar.MONTH),
                        mCurrentTime.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        };
        mStartDateTextView.setOnClickListener(listener);
        mEndDateTextView.setOnClickListener(listener);
    }

    private void setReferencesToViews() {
        mStartDateLabelTextView = findViewById(R.id.tv_start_date_label);
        mEndDateLabelTextView = findViewById(R.id.tv_end_date_label);
        mStartTimeLabelTextView = findViewById(R.id.tv_start_time_label);
        mEndTimeLabelTextView = findViewById(R.id.tv_end_time_label);

        mStartDateTextView = findViewById(R.id.tv_start_date);
        mEndDateTextView = findViewById(R.id.tv_end_date);
        mStartTimeTextView = findViewById(R.id.tv_start_time);
        mEndTimeTextView = findViewById(R.id.tv_end_time);

        mAreaSpinner = findViewById(R.id.spinner_areas);
        mRepetitionSpinner = findViewById(R.id.spinner_repetition);
        mAddRuleButton = findViewById(R.id.button_add_rule);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String monthString, dayString;
        if (++month < 10) {
            monthString = "0" + month;
        } else {
            monthString = "" + month;
        }
        if (dayOfMonth < 10) {
            dayString = "0" + dayOfMonth;
        } else {
            dayString = "" + dayOfMonth;
        }
        String date = String.format(Locale.getDefault(), "%s-%s-%d", dayString, monthString, year);
        ((TextView) mSelectedView).setText(date);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour, min;
        if (hourOfDay < 10) {
            hour = "0" + hourOfDay;
        } else {
            hour = "" + hourOfDay;
        }

        if (minute < 10) {
            min = "0" + minute;
        } else {
            min = "" + minute;
        }


        String time = String.format(Locale.getDefault(), "%s:%s", hour, min);
        ((TextView) mSelectedView).setText(time);
    }

    //Response from async task
    @Override
    public void onSuccess(ArrayList<Area> areaList) {
        areaList = removeOtherKidsAreas(areaList);
        AreaSpinnerAdapter adapter = new AreaSpinnerAdapter(this, areaList);
        mAreaSpinner.setAdapter(adapter);
    }

    private ArrayList<Area> removeOtherKidsAreas(ArrayList<Area> areaList) {
        ArrayList<Area> newAreaList = new ArrayList<>();
        for (int i = 0; i < areaList.size(); i++) {
            Area area = areaList.get(i);
            if (doesKidBelongToArea(area.getKids())){
                newAreaList.add(area);
            }
        }
        return newAreaList;
    }

    private boolean doesKidBelongToArea(List<Kid> kidList) {
        for (Kid kid :kidList) {
            if (kid.getId().equals(mKid.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onFailure() {

    }

    private void setAddRuleButtonOnClick() {
        mAddRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddRuleButton.setClickable(false);
                if (canRuleBeCreated()) {
                    try {
                        AddRuleAsync asyncTask = new AddRuleAsync(AddRuleActivity.this, new AsyncResponse<String>() {
                            @Override
                            public void onSuccess(String item) {
                                finish();
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                        asyncTask.execute(createPostParams());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAddRuleButton.setClickable(true);

            }
        });
    }

    private boolean canRuleBeCreated() {
        if (mStartDateTextView.getText().equals(getString(R.string.rule_default_date))) {
            showToastMessage(getString(R.string.error_start_date_not_choosen));
            return false;
        } else if (mEndDateTextView.getText().equals(getString(R.string.rule_default_date))) {
            showToastMessage(getString(R.string.error_end_date_not_choosen));
            return false;
        } else if (mStartTimeTextView.getText().equals(getString(R.string.rule_default_time))) {
            showToastMessage(getString(R.string.error_start_time_not_choosen));
            return false;
        } else if (mEndTimeTextView.getText().equals(getString(R.string.rule_default_time))) {
            showToastMessage(getString(R.string.error_end_time_not_choosen));
            return false;
        } else if (!areInputDatesCorrect()) {
            showToastMessage(getString(R.string.error_wrong_dates));
            return false;
        } else if (!areInputTimesCorrect()) {
            showToastMessage(getString(R.string.error_wrong_times));
        }

        return true;
    }

    //1st argument - day, 2nd - month, 3rd - year
    private boolean areInputDatesCorrect() {
        String[] startDate = mStartDateTextView.getText().toString().split("-");
        int startDay = Integer.parseInt(startDate[0]);
        int startMonth = Integer.parseInt(startDate[1]);
        int startYear = Integer.parseInt(startDate[2]);

        String[] endDate = mEndDateTextView.getText().toString().split("-");
        int endDay = Integer.parseInt(endDate[0]);
        int endMonth = Integer.parseInt(endDate[1]);
        int endYear = Integer.parseInt(endDate[2]);

        if (startYear > endYear) {
            return false;
        } else if (startYear == endYear && startMonth > endMonth) {
            return false;
        } else return startYear != endYear || startMonth != endMonth || startDay <= endDay;
    }

    //1st argument - hours, 2nd - minutes
    private boolean areInputTimesCorrect() {
        String[] startTime = mStartTimeTextView.getText().toString().split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int startMinute = Integer.parseInt(startTime[1]);
        String[] endTime = mEndTimeTextView.getText().toString().split(":");
        int endHour = Integer.parseInt(endTime[0]);
        int endMinute = Integer.parseInt(endTime[1]);

        if (startHour > endHour) {
            return false;
        } else return startHour != endHour || startMinute < endMinute;
    }


    private void showToastMessage(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();
    }

    private JSONObject createPostParams() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("startDate", parseDateToServerFormat(mStartDateTextView.getText().toString()));
        params.put("endDate", parseDateToServerFormat(mEndDateTextView.getText().toString()));
        params.put("startTime", mStartTimeTextView.getText());
        params.put("endTime", mEndTimeTextView.getText());
        params.put("repetition", getRepetition());
        params.put("childId", mKid.getId());
        params.put("areaId", getSelectedAreaId());
        return params;
    }

    private String getSelectedAreaId() {
        return ((Area)mAreaSpinner.getSelectedItem()).getId();
    }

    private String getRepetition() {
        String repetitionLabel = mRepetitionSpinner.getSelectedItem().toString();
        if (repetitionLabel.equals(getString(R.string.rules_repetition_daily))) {
            return Rule.REPETITION_DAILY;
        } else if (repetitionLabel.equals(getString(R.string.rules_repetition_monthly))) {
            return Rule.REPETITION_MONTHLY;
        } else if (repetitionLabel.equals(getString(R.string.rules_repetition_none))) {
            return Rule.REPETITION_NONE;
        } else if (repetitionLabel.equals(getString(R.string.rules_repetition_weekends))) {
            return Rule.REPETITION_WEEKENDS;
        } else if (repetitionLabel.equals(getString(R.string.rules_repetition_weekly))) {
            return Rule.REPETITION_WEEKLY;
        } else if (repetitionLabel.equals(getString(R.string.rules_repetition_work_days))) {
            return Rule.REPETITION_WORKDAYS;
        } else {
            return Rule.REPETITION_YEARLY;
        }
    }

    private String parseDateToServerFormat(String date) {
        String[] arr = date.split("-");
        return String.format(Locale.getDefault(), "%s-%s-%s", arr[2], arr[1], arr[0]);
    }


}
