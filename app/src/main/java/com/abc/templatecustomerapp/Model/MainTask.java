package com.abc.templatecustomerapp.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.util.Pair;

import com.abc.templatecustomerapp.Network;
import com.abc.templatecustomerapp.Utils.AsyncTaskResult;
import com.abc.templatecustomerapp.Utils.NetworkResponse;
import com.abc.templatecustomerapp.Utils.callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainTask {
    public static void getData(Context context, String endPoint, final callback<String> cb) {
        FetchTask fetchTask =  new FetchTask() {
            @Override
            protected void onPostExecute(AsyncTaskResult<NetworkResponse> asyncTaskResult) {
                if (asyncTaskResult.isError()) {
                    cb.onError(asyncTaskResult.getError());
                } else {
                    NetworkResponse response = asyncTaskResult.getResult();
                    Log.d("res", response.getResponseString());
                    if (response.getResponseCode() == 200) {
                        cb.onSucess(response.getResponseString());
                    } else {
                        cb.onError(new Exception("404"));
                    }
                }
            }
        };
        fetchTask.execute(new Pair<Context, String>(context, endPoint));
    }

    public static class FetchTask extends AsyncTask<Pair<Context, String>, Void, AsyncTaskResult<NetworkResponse>> {

        @Override
        protected AsyncTaskResult<NetworkResponse> doInBackground(Pair<Context, String>... data) {
            Context context = data[0].first;
            String endPoint = data[0].second;
            try {
                NetworkResponse tokenResponse = Network.verifyToken(User.getUserInstance().getToken());
                if (tokenResponse.getResponseCode() != 200) {
                    return  new AsyncTaskResult<>(new Exception("Invalid Token"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                RequestBody body = null;
                if (endPoint.equals("app/getMyOrders")) {
                    JSONObject object = new JSONObject();
                    object.put("userID", String.valueOf(User.getUserInstance().getId()));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    body = RequestBody.create(JSON, object.toString());
                }
                NetworkResponse response = Network.makeCall(endPoint, body, Network.getAppId(context));
                return new AsyncTaskResult<>(response);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return new AsyncTaskResult<>(e);
            }
        }
    }

    public static void placeOrder(Context context, JSONObject object, final callback<String> cb) {
        OrderTask orderTask =  new OrderTask() {
            @Override
            protected void onPostExecute(AsyncTaskResult<NetworkResponse> asyncTaskResult) {
                if (asyncTaskResult.isError()) {
                    cb.onError(asyncTaskResult.getError());
                } else {
                    NetworkResponse response = asyncTaskResult.getResult();
                    Log.d("res", response.getResponseString());
                    if (response.getResponseCode() == 200) {
                        cb.onSucess(response.getResponseString());
                    } else {
                        cb.onError(new Exception("404"));
                    }
                }
            }
        };
        orderTask.execute(new Pair<Context, JSONObject>(context, object));
    }

    public static class OrderTask extends AsyncTask<Pair<Context, JSONObject>, Void, AsyncTaskResult<NetworkResponse>> {

        @Override
        protected AsyncTaskResult<NetworkResponse> doInBackground(Pair<Context, JSONObject>... data) {
            Context context = data[0].first;
            JSONObject object = data[0].second;
            try {
                NetworkResponse tokenResponse = Network.verifyToken(User.getUserInstance().getToken());
                if (tokenResponse.getResponseCode() != 200) {
                    return  new AsyncTaskResult<>(new Exception("Invalid Token"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, object.toString());
                NetworkResponse response = Network.makeCall("app/addOrder", body, Network.getAppId(context));
                return new AsyncTaskResult<>(response);
            } catch (IOException e) {
                e.printStackTrace();
                return new AsyncTaskResult<>(e);
            }
        }
    }
}
