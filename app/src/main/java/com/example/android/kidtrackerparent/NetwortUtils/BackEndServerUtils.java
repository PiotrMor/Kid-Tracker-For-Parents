package com.example.android.kidtrackerparent.NetwortUtils;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class BackEndServerUtils {

    public static final String TAG = BackEndServerUtils.class.getSimpleName();

    public static final String SERVER_URL = "https://kid-tracker.herokuapp.com/";
    public static final String SERVER_CURRENT_USER = SERVER_URL + "api/current_user";



    //URL's for parent app
    public static final String SERVER_LOGIN_PARENT = SERVER_URL + "auth/parent/local";
    public static final String SERVER_REGISTER_PARENT = SERVER_URL + "registration/parent";
    public static final String SERVER_GOOGLE_SIGN_IN = SERVER_URL + "auth/parent/google";
    public static final String SERVER_GET_CHILDREN = SERVER_URL + "api/parent/children";
    public static final String SERVER_ADD_CHILDREN = SERVER_URL + "api/parent/children";
    public static final String SERVER_UPDATE_CHILDREN = SERVER_ADD_CHILDREN + "/id";
    public static final String SERVER_GET_AREAS = SERVER_URL + "api/parent/areas";


    //URL's for kid app
    public static final String SERVER_REGISTER_CHILD = SERVER_URL + "registration/child";
    public static final String SERVER_LOGIN_KID = SERVER_URL + "auth/child/local";
    public static final String SERVER_GET_CHILD_CODE = SERVER_URL + "api/child/code";

    public static final String NO_COOKIES = "0 cookies";


    public static ResponseTuple performPostCall(String requestURL,
                                                JSONObject postDataParams, String sessionCookie) {
        String cookie = NO_COOKIES;
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
            if (sessionCookie != null) {
                conn.setRequestProperty("Cookie", sessionCookie);
            }

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(postDataParams.toString());

            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
                if(cookies != null && !cookies.isEmpty()){
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String element : cookies) {
                        stringBuilder.append(element);
                    }
                    cookie = stringBuilder.toString();
                }
                Log.d(TAG, "cookies: " + cookies);
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
        Log.d(TAG, requestURL + " " + response);

        return new ResponseTuple(response, cookie);

    }


    public static ResponseTuple performPostCall(String requestURL,
                                  HashMap<String, String> postDataParams, String sessionCookie) {
        try {
            return performPostCall(requestURL, getPostDataJsonObject(postDataParams), sessionCookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String performGetCall(String requestURl, String cookie) {


        try {
            URL url = new URL(requestURl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
            }
            conn.connect();

            int respondCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            Log.d(TAG, "performGetCall: " + response);
            List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
            Log.d(TAG, "performPostCall: " + cookies);

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

    private static JSONObject getPostDataJsonObject(HashMap<String, String> params) throws UnsupportedEncodingException {
        return new JSONObject(params);
    }
}
