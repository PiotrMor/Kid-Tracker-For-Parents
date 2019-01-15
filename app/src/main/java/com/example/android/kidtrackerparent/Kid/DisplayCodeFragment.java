package com.example.android.kidtrackerparent.Kid;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.JSONUtils;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayCodeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayCodeFragment extends Fragment {

    public static final String TAG = DisplayCodeFragment.class.getSimpleName();
    private static final String KEY_CODE = "code";
    private static final String KEY_CODE_CREATION = "createdAt";
    private AsyncTask mGetChildCode;

    private OnFragmentInteractionListener mListener;

    private String mCode;
    private String mCreationDate;

    private TextView mCodeTextView;
    private TextView mTimerTextView;

    private View mRootView;


    public DisplayCodeFragment() {
        // Required empty public constructor
    }


    public static DisplayCodeFragment newInstance(String param1, String param2) {
        DisplayCodeFragment fragment = new DisplayCodeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void setReferencesToViews() {
        mCodeTextView = mRootView.findViewById(R.id.tv_display_code);
        mTimerTextView = mRootView.findViewById(R.id.tv_timer);
    }

    private void createBackgroundTask() {
        mGetChildCode = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                String response = BackEndServerUtils.performCall(BackEndServerUtils.SERVER_GET_CHILD_CODE,
                        PreferenceUtils.getSessionCookie(getActivity()),
                        BackEndServerUtils.REQUEST_GET);
                Log.d(TAG, "doInBackground: " + response);
                return response;
            }

            @Override
            protected void onPostExecute(Object o) {
                String response = (String) o;
                setCodeAndTimer(response);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startCountdownTimer();
                    }
                });
            }
        };
    }

    public void setCodeAndTimer(String jsonString){
        mCode = JSONUtils.getValueFromJsonString(jsonString, KEY_CODE);
        mCreationDate = JSONUtils.getValueFromJsonString(jsonString, KEY_CODE_CREATION);


        mCodeTextView.setText(mCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_display_code, container, false);
        createBackgroundTask();
        setReferencesToViews();
        mGetChildCode.execute();

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void startCountdownTimer() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", new Locale("polish"));
        //TODO: check if this works all the time
        try {
            Date date = df.parse(mCreationDate);
            new CountDownTimer(
                    date.getTime() + TimeUnit.MINUTES.toMillis(2) + TimeUnit.HOURS.toMillis(1) +  - Calendar.getInstance().getTimeInMillis() , 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished <= TimeUnit.SECONDS.toMillis(20) && getActivity() != null) {
                        mTimerTextView.setTextColor(getResources().getColor(R.color.red));
                    }
                    mTimerTextView.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1)));
                }

                @Override
                public void onFinish() {
                    createBackgroundTask();
                    if (getActivity() != null) {
                        mTimerTextView.setTextColor(getResources().getColor(R.color.green));
                        mGetChildCode.execute();
                    }
                }
            }.start();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
