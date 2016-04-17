package com.example.yeye.sms.myMethod;

import android.app.Activity;
import android.widget.Toast;

import com.example.yeye.sms.util.LogUtil;

/**
 * Created by yeye on 2016/4/17.
 */
public class Upload {

    public static void SMSUpload(Activity activity,boolean loadFlag){
        // TODO: 2016/1/21 上传至服务器
        if (loadFlag) {
            Toast.makeText(activity, "还没做呢", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "备份文件损坏，上传失败", Toast.LENGTH_LONG).show();
            LogUtil.e("Backup", "本地备份失败");
        }
    }

    public static void ConUpload(Activity activity,boolean loadFlag){
        // TODO: 2016/4/10 上传服务器
        if (loadFlag) {
            Toast.makeText(activity, "还没做呢", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "备份文件损坏，上传失败", Toast.LENGTH_LONG).show();
            LogUtil.e("Backup", "本地备份失败");
        }
    }

}
