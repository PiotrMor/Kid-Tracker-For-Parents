package com.example.android.kidtrackerparent.Parent.Areas;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.NetworkUtils.BackEndServerUtils;
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
    private SwipeRefreshLayout mSwipeRefresh;
    private View mRootView;
    private OnListFragmentInteractionListener mListener;


    public AreasListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_areas_list, container, false);
        mRootView = view;
        addRefreshOnSwap();
        Context context = view.getContext();

        mRecyclerView = view.findViewById(R.id.areas_recycler_view);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        populateAreaList();
        addFABOnClick();
        return  view;
    }

    private void addFABOnClick() {
        FloatingActionButton fab = mRootView.findViewById(R.id.fab_add_area);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectCustomAreaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addRefreshOnSwap() {
        mSwipeRefresh = mRootView.findViewById(R.id.srl_areas_list);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateAreaList();
            }
        });
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
                if (getActivity() == null) {
                    return null;
                }

                String jsonString = BackEndServerUtils.performCall(BackEndServerUtils.SERVER_GET_AREAS,
                        PreferenceUtils.getSessionCookie(getActivity()),
                        BackEndServerUtils.REQUEST_GET);
                mAreaList = new ArrayList<>();
                Log.d(TAG, "doInBackground: " + jsonString);
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        mAreaList.add(new Area(json));
                        Log.d(TAG, "doInBackground: " + new Area(json));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(new AreasRecyclerViewAdapter(getActivity(), mAreaList, mListener));
                            if (mSwipeRefresh != null) {
                                mSwipeRefresh.setRefreshing(false);
                            }
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

