package com.example.android.kidtrackerparent.Parent.Areas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.R;

import java.util.List;

public class AreasRecyclerViewAdapter extends RecyclerView.Adapter<AreasRecyclerViewAdapter.ViewHolder> {

    private final List<Area> mAreas;
    private final Context mContext;
    private AreasListFragment.OnListFragmentInteractionListener mListener;


    public AreasRecyclerViewAdapter(Context context, List<Area> list, AreasListFragment.OnListFragmentInteractionListener listener) {
        mAreas = list;
        mListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_areas_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.mArea = mAreas.get(i);
        viewHolder.mAreaName.setText(mAreas.get(i).getName());
        viewHolder.mAreaIcon.setImageDrawable(getDrawableIcon(viewHolder.mArea.getIconId()));
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onListFragmentInteraction(viewHolder.mArea);
                }
            }
        });
    }

    private Drawable getDrawableIcon(String iconId) {
        switch (iconId) {
            case Area.ICON_BOOK:
                return mContext.getResources().getDrawable(R.drawable.book);
            case Area.ICON_BUILDING:
                return mContext.getResources().getDrawable(R.drawable.building);
            case Area.ICON_WORK:
                return mContext.getResources().getDrawable(R.drawable.work);
            default:
                return mContext.getResources().getDrawable(R.drawable.home);
        }
    }

    @Override
    public int getItemCount() {
        return mAreas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final TextView mAreaName;
        final ImageView mAreaIcon;
        Area mArea;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mAreaName = itemView.findViewById(R.id.iv_rule_area_icon);
            mAreaIcon = itemView.findViewById(R.id.iv_area_icon);
        }
    }
}
