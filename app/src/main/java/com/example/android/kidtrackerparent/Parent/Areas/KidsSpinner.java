package com.example.android.kidtrackerparent.Parent.Areas;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class KidsSpinner extends android.support.v7.widget.AppCompatSpinner {

    private int lastSelected = 0;
    public KidsSpinner(Context context) {
        super(context);
    }

    public KidsSpinner(Context context, AttributeSet attrs)
    { super(context, attrs); }

    public KidsSpinner(Context context, AttributeSet attrs, int defStyle)
    { super(context, attrs, defStyle); }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(this.lastSelected == this.getSelectedItemPosition() && getOnItemSelectedListener() != null)
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), this.getSelectedItemPosition(), getSelectedItemId());
        if(!changed)
            lastSelected = this.getSelectedItemPosition();

        super.onLayout(changed, l, t, r, b);
    }
}
