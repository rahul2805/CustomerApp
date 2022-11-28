package com.abc.templatecustomerapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.abc.templatecustomerapp.Model.User;
import com.abc.templatecustomerapp.Utils.NetworkResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Network {
    public static String SHARE_PREF_NAME = "SHARED_PREF";
    public static String baseUrl = "http://192.168.0.109:8000/";

    public static NetworkResponse makeCall(String endPpoint, RequestBody body, String appId) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(baseUrl + endPpoint);
        if (body != null) {
            requestBuilder.post(body);
        }
        requestBuilder.header("appId", appId);
        if (!endPpoint.equals("main/appLogin") && !endPpoint.equals("main/appRegister")) {
            requestBuilder.header("Authorization", User.getUserInstance().getToken());
        }
        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        return new NetworkResponse(response);
    }

    public static NetworkResponse verifyToken(String token) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(baseUrl + "app/checkToken");
        requestBuilder.header("Authorization", token);
        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        return new NetworkResponse(response);
    }

    public static NetworkResponse Login(String username, String psswd, String appId) throws IOException, JSONException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject object = new JSONObject();
        object.put("username", username);
        object.put("psswd", psswd);
        RequestBody body = RequestBody.create(JSON, object.toString());
        return Network.makeCall("main/appLogin", body, appId);
    }

    public static NetworkResponse Register(JSONObject object, String appId) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, object.toString());
        return Network.makeCall("main/appRegister", body, appId);
    }

    public static void addUserDataToSharedPrefs(Context context, String userData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userData", userData);
        editor.apply();
    }

    public static void updateTokenInSharedPref(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static void clearUserData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("userData");
        editor.apply();
    }

    public static void readAppId(Context context) {
        String res = "";
        try {
            InputStream stream = context.getAssets().open("meta_data.txt");

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            res = new String(buffer);
            JSONObject jsonObject = new JSONObject(res);
            res = jsonObject.getString("appId");
        } catch (IOException | JSONException e) {
            // Handle exceptions here
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("appId", res);
        editor.apply();
    }

    public static String getAppId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREF_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString("appId", "");
    }
}
