package com.example.android.kidtrackerparent.Parent.Areas;

import android.graphics.drawable.Drawable;

public class Icon {

    private String name;
    private Drawable drawable;

    public Icon(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        if (name == null) {
            return "chuj";
        }
        return name;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
