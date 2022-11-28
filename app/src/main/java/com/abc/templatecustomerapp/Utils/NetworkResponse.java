package com.abc.templatecustomerapp.Utils;

import java.io.IOException;

import okhttp3.Response;

public class NetworkResponse {
    String responseString;
    Integer responseCode;

    public NetworkResponse(Response response) throws IOException {
        this.responseString = response.body().string();
        this.responseCode = response.code();
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseString() {
        return responseString;
    }
}