package com.example.android.kidtrackerparent.NetwortUtils;

import android.util.Log;

import com.example.android.kidtrackerparent.Utils.CookieUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class BackEndServerUtils {

    public static final String TAG = BackEndServerUtils.class.getSimpleName();

    public static final String SERVER_URL = "https://kid-tracker.herokuapp.com/";
    public static final String SERVER_LOGIN_AUTH = SERVER_URL + "auth/" + "local";
    public static final String SERVER_REGISTER = SERVER_URL + "api/registration";
    public static final String SERVER_GOOGLE_SIGN_IN = SERVER_URL + "auth/google";
    public static final String SERVER_CURRENT_USER = SERVER_URL + "api/current_user";

    public static final String NO_COOKIES = "0 cookies";





    public static ResponseTuple performPostCall(String requestURL,
                                  HashMap<String, String> postDataParams) {
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
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(getPostDataString(postDataParams));
            Log.d(TAG, "performPostCall: " +getPostDataString(postDataParams));

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


        return new ResponseTuple(response, cookie);
    }

    public static String performGetCall(String requestURl, String cookie) {


        try {
            URL url = new URL(requestURl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (cookie != null) {
                conn.setRequestProperty("Cookie", cookie);
                Log.d(TAG, "performGetCall: " + cookie);
            }
            conn.connect();

            int respondCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
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

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        return new JSONObject(params).toString();
    }
}
