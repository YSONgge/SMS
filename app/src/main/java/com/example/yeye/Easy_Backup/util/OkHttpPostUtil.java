package com.example.yeye.Easy_Backup.util;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yeye on 2016/1/18.
 */
public class OkHttpPostUtil {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");



    public static String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
       if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
