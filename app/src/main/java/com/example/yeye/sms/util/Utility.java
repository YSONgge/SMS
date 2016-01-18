package com.example.yeye.sms.util;

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
}
