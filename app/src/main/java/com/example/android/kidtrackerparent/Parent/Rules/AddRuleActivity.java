package com.example.android.kidtrackerparent.Parent.Rules;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.kidtrackerparent.AsyncTasks.AreaListFromServerAsync;
import com.example.android.kidtrackerparent.AsyncTasks.AsyncResponse;
import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.R;

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

    private Calendar mCurrentTime;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        Date date = new Date();
        mCurrentTime = new GregorianCalendar();
        mCurrentTime.setTime(date);
        setReferencesToViews();

        setDateViewsOnClickListeners();
        setTimeViewsOnClickListeners();

        populateAreaSpinner();
        populateRepetitionSpinner();
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
                        mCurrentTime.get(Calendar.MONTH) + 1,
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
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format(Locale.getDefault(), "%d-%d-%d", dayOfMonth, month, year);
        ((TextView) mSelectedView).setText(date);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = String.format(Locale.getDefault(), "%d:%d", hourOfDay, minute);
        ((TextView) mSelectedView).setText(time);
    }

    //Response from async task
    @Override
    public void onSuccess(ArrayList<Area> areaList) {
        AreaSpinnerAdapter adapter = new AreaSpinnerAdapter(this, areaList);
        mAreaSpinner.setAdapter(adapter);
    }

    @Override
    public void onFailure() {

    }
}
