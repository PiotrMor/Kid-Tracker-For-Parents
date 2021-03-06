package com.example.android.kidtrackerparent.NetworkUtils;

public class ResponseTuple {
    private final String response;
    private final String cookie;

    public ResponseTuple(String response, String cookie) {
        this.response = response;
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public String getResponse() {
        return response;
    }
}
