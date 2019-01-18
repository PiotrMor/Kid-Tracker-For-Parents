package com.example.android.kidtrackerparent.Parent.Rules;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.kidtrackerparent.BasicClasses.Area;
import com.example.android.kidtrackerparent.R;

import java.util.ArrayList;

public class AreaSpinnerAdapter extends ArrayAdapter<Area> {
    public AreaSpinnerAdapter(Context context, ArrayList<Area> list) {
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
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fragment_areas_list_item,
                    parent,
                    false
            );
        }

        ImageView image = convertView.findViewById(R.id.iv_area_icon);
        TextView label = convertView.findViewById(R.id.iv_rule_area_icon);

        Area area = getItem(position);

        if (area != null) {
            image.setImageDrawable(getDrawableIcon(area.getIconId()));
            label.setText(area.getName());
        }

        return convertView;
    }

    private Drawable getDrawableIcon(String iconId) {
        switch (iconId) {
            case Area.ICON_BOOK:
                return getContext().getResources().getDrawable(R.drawable.book);
            case Area.ICON_BUILDING:
                return getContext().getResources().getDrawable(R.drawable.building);
            case Area.ICON_WORK:
                return getContext().getResources().getDrawable(R.drawable.work);
            default:
                return getContext().getResources().getDrawable(R.drawable.home);
        }
    }
}
