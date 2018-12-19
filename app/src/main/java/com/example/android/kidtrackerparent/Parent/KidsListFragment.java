package com.example.android.kidtrackerparent.Parent;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.LoginActivity;
import com.example.android.kidtrackerparent.NetwortUtils.BackEndServerUtils;
import com.example.android.kidtrackerparent.R;
import com.example.android.kidtrackerparent.Utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class KidsListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = KidsListFragment.class.getSimpleName();
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Kid> mKidList = new ArrayList<>();
    private View mRootView;

    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public KidsListFragment() {
    }

    public static KidsListFragment newInstance(int columnCount) {
        KidsListFragment fragment = new KidsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateKidList();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_kids_list, container, false);
        mRootView = view;
        // Set the adapter

        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.kids_recycler_view);
        if (mRecyclerView == null) {
            Log.d(TAG, "onCreateView: przypaulllll");
        }
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mRecyclerView.setAdapter(new KidsRecyclerViewAdapter(mKidList, mListener));
        addFABOnClick();


        return view;
    }

    private void addFABOnClick() {
        FloatingActionButton fab = mRootView.findViewById(R.id.fab_add_kid);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddKidActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void populateKidList() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {

                String jsonString = BackEndServerUtils.performGetCall(BackEndServerUtils.SERVER_GET_CHILDREN, PreferenceUtils.getSessionCookie(getActivity()));
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(new KidsRecyclerViewAdapter(mKidList, mListener));
                    }
                });
                return null;
            }
        }.execute();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Kid item);
    }
}
