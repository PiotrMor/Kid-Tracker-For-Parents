package com.example.android.kidtrackerparent.BasicClasses;

import com.example.android.kidtrackerparent.Utils.JSONUtils;

import org.json.JSONObject;

public class Kid {

    private final String id;
    private String name;
    private String email;
    private String color;

    public Kid(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public Kid(JSONObject jsonObject) {
        this.id = JSONUtils.getUserIdFromJson(jsonObject);
        this.name = JSONUtils.getValueFromJson(jsonObject, "name");
        this.email = JSONUtils.getValueFromJson(jsonObject, "email");
        this.color = JSONUtils.getValueFromJson(jsonObject, "iconColor");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
