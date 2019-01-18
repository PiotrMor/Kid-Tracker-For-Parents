package com.example.android.kidtrackerparent.Parent.Areas;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.kidtrackerparent.BasicClasses.Kid;
import com.example.android.kidtrackerparent.R;

import java.util.ArrayList;

public class KidsSpinnerAdapter extends ArrayAdapter<Kid> {

    private ArrayList<Kid> mChosenKids;
    public static final String TAG = KidsSpinnerAdapter.class.getSimpleName();
    private boolean isJustCreated = true;
    private boolean isChanged = false;
    private int previousPosition = -1;
    private Toast mToast;

    public KidsSpinnerAdapter(Context context, ArrayList<Kid> kids) {
        super(context, 0, kids);
        mChosenKids = new ArrayList<>();
    }

    public ArrayList<Kid> getChosenKids() {
        return mChosenKids;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (!mChosenKids.contains(getItem(position)) && isChanged) {
            mChosenKids.add(getItem(position));
            notifyDataSetChanged();
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_view_kids, parent, false);
        }
        Log.d(TAG, "getCustomView: " + mChosenKids);
        TextView defaultTextView = convertView.findViewById(R.id.tv_kid_spinner_default);
        if (mChosenKids.isEmpty()) {
            defaultTextView.setVisibility(View.VISIBLE);
        } else {
            LinearLayout linearLayout = convertView.findViewById(R.id.ll_view_kids);
            defaultTextView.setVisibility(View.GONE);


            for (Kid kid : mChosenKids) {
                Button kidNameButton = createButtonWithName(kid);
                linearLayout.addView(kidNameButton);
            }
        }
        isChanged = false;
        return convertView;
    }

    private Button createButtonWithName(Kid kid) {
        final KidButton button = new KidButton(getContext(), kid);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        params.setMargins(2,2,2,2);
        button.setLayoutParams(params);
        button.setText(kid.getName());
        button.setPadding(7, 0, 7, 0);
        button.setBackground(getContext().getResources().getDrawable(R.drawable.rounded_button));
        button.getBackground().setColorFilter(new
                PorterDuffColorFilter(0xd3d3d3, PorterDuff.Mode.MULTIPLY));
        button.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.ic_cancel_black_24dp), null);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosenKids.remove(button.getKidReference());
                Log.d(TAG, "setOnClickListener: " + mChosenKids);
                notifyDataSetChanged();
            }
        });
        return button;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomDropDownView(position, convertView, parent);
    }

    private View getCustomDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_kids, parent, false);
        }


        TextView firstLetter = convertView.findViewById(R.id.tv_first_letter);
        TextView name = convertView.findViewById(R.id.tv_kid_name_spinner);
        Kid kid = getItem(position);
        if (kid != null) {
            name.setText(kid.getName());
            firstLetter.setText(kid.getName().substring(0,1));
            // TODO: change a color of first letter background
        }
        isChanged = true;
        return convertView;
    }


    class KidButton extends android.support.v7.widget.AppCompatButton {
        private Kid kidReference;

        public KidButton(Context context, Kid kid) {
            super(context);
            kidReference = kid;
        }

        public Kid getKidReference() {
            return kidReference;
        }

    }


}
