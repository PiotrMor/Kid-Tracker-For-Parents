package com.example.android.kidtrackerparent.NetwortUtils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class BackEndServerUtils {

    public static final String TAG = BackEndServerUtils.class.getSimpleName();

    public static final String BACKEND_SERVER_URL = "https://kid-tracker.herokuapp.com/";
    public static final String BACKEND_SERVER_LOGIN_AUTH = BACKEND_SERVER_URL + "auth/" + "local";

    public static String performPostCall(String requestURL,
                                  HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            JSONObject auth = new JSONObject();
            auth.put("email", "admin@gmail.com");
            auth.put("password", "admin");

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(auth.toString());
            Log.d(TAG, "performPostCall: " +getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
                Log.d(TAG, "performPostCall: " + cookies);
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
