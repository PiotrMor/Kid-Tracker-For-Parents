package com.example.android.kidtrackerparent.Parent;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.Parent.KidsListFragment.OnListFragmentInteractionListener;
import com.example.android.kidtrackerparent.R;

import java.util.List;


public class KidsRecyclerViewAdapter extends RecyclerView.Adapter<KidsRecyclerViewAdapter.ViewHolder> {

    private final List<Kid> mKids;
    private final OnListFragmentInteractionListener mListener;

    public final static String TAG = KidsRecyclerViewAdapter.class.getSimpleName();

    public KidsRecyclerViewAdapter(List<Kid> items, OnListFragmentInteractionListener listener) {
        mKids = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_kids_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mKid = mKids.get(position);
        holder.mKidNameTextView.setText(mKids.get(position).getName());
        holder.mKidIconTextView.setText(mKids.get(position).getName().substring(0,1));
        holder.mKidIconTextView.getBackground().setColorFilter(Color.parseColor(mKids.get(position).getColor())
                ,PorterDuff.Mode.SRC_ATOP);

        String lastLocation = mKids.get(position).getLastLocationTime();
        if (lastLocation != null) {
            holder.mLocationTimeTextView.setText(lastLocation);
        } else {
            holder.mLocationTimeTextView.setText(R.string.no_last_location);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mKid);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mKidNameTextView;
        final TextView mKidIconTextView;
        final TextView mLocationTimeTextView;
        Kid mKid;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mKidNameTextView = view.findViewById(R.id.tv_kid_name);
            mKidIconTextView = view.findViewById(R.id.tv_kid_icon);
            mLocationTimeTextView = view.findViewById(R.id.tv_last_location_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mKidNameTextView.getText() + "'";
        }
    }
}
