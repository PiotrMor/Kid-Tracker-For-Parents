package com.example.android.kidtrackerparent.BasicClasses;

public class Kid {

    private final String id;
    private String name;

    public Kid(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
