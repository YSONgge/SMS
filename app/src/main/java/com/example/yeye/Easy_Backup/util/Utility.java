package com.example.yeye.Easy_Backup.util;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yeye on 2016/1/16.
 */
public class Utility {
    public static final String TAG = "Utility";

    public static synchronized boolean handleBooleanResultResponse(String response) {
        String TAG = "handleBooleanResultResponse";
        boolean flag = false;
        if (TextUtils.isEmpty(response)) {
            return flag;
        }
        try {
            LogUtil.d(TAG, response);
            JSONObject jsonObject = new JSONObject(response);
            flag = jsonObject.getBoolean("result");
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.toString());
        }
        return flag;
    }

    public static synchronized boolean handleUploadRequestResponse(String response){
        String TAG = "handleUploadSmsRequestResponse";
        Boolean uploadSmsflag = false;
        if (TextUtils.isEmpty(response)) {
            return uploadSmsflag;
        }
        try {
            LogUtil.d(TAG, response);
            JSONObject jsonObject = new JSONObject(response);
            uploadSmsflag = jsonObject.getBoolean("result");

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.toString());
        }
        return uploadSmsflag;
    }
}
