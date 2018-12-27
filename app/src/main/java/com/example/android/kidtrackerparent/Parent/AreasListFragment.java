package com.example.android.kidtrackerparent.Parent;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.NetwortUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AreasListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = AreasListFragment.class.getSimpleName();

    private List<Area> mAreaList;

    private RecyclerView mRecyclerView;

    private View mRootView;
    private OnListFragmentInteractionListener mListener;


    public AreasListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_areas_list, container, false);
        mRootView = view;
        populateAreaList();
        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.areas_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
       // mRecyclerView.setAdapter(new AreasRecyclerViewAdapter(mAreaList, mListener));
        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void populateAreaList() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                String jsonString = BackEndServerUtils.performGetCall(BackEndServerUtils.SERVER_GET_AREAS, PreferenceUtils.getSessionCookie(getActivity()));
                mAreaList = new ArrayList<>();
                Log.d(TAG, "doInBackground: " + jsonString);
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        mAreaList.add(new Area(json));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(new AreasRecyclerViewAdapter(mAreaList, mListener));
                        }
                    });
                }
                return null;
            }
        }.execute();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Area area);
    }
}

