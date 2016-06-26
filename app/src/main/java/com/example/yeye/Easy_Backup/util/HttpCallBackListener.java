package com.example.yeye.Easy_Backup.util;

/**
 * Created by yeye on 2016/1/16.
 */
public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
