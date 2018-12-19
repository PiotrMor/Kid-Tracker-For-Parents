package com.example.android.kidtrackerparent.Parent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.Parent.KidsListFragment.OnListFragmentInteractionListener;
import com.example.android.kidtrackerparent.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class KidsRecyclerViewAdapter extends RecyclerView.Adapter<KidsRecyclerViewAdapter.ViewHolder> {

    private final List<Kid> mKids;
    private final OnListFragmentInteractionListener mListener;

    public KidsRecyclerViewAdapter(List<Kid> items, OnListFragmentInteractionListener listener) {
        mKids = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mKid = mKids.get(position);
        holder.mChildName.setText(mKids.get(position).getName());

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
        public final View mView;
        public final TextView mChildName;
        public Kid mKid;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mChildName = view.findViewById(R.id.tv_kid_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mChildName.getText() + "'";
        }
    }
}
