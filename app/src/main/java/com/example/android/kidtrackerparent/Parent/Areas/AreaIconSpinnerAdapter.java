package com.example.android.kidtrackerparent.Parent.Areas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.kidtrackerparent.R;

import java.util.ArrayList;

public class AreaIconSpinnerAdapter extends ArrayAdapter<Icon> {

    private final static String TAG = AreaIconSpinnerAdapter.class.getSimpleName();

    public AreaIconSpinnerAdapter(Context context, ArrayList<Icon> list) {
        super(context, 0, list);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_area_types_list, parent, false);

        }
        Log.d(TAG, "getCustomView: " + position);
        ImageView image = convertView.findViewById(R.id.iv_area_icon);
        TextView label = convertView.findViewById(R.id.tv_area_name);

        Icon element = getItem(position);

        if (element != null) {
            image.setImageDrawable(element.getDrawable());
            label.setText(element.getName());
        }


        Log.d(TAG, "getCustomView: " + getItem(position));

        return convertView;
    }


}
