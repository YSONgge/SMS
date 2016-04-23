package com.example.yeye.sms.myMethod;

import android.app.Activity;
import android.widget.Toast;

import com.example.yeye.sms.R;
import com.example.yeye.sms.util.HttpCallBackListener;
import com.example.yeye.sms.util.HttpUtil;
import com.example.yeye.sms.util.IConst;
import com.example.yeye.sms.util.LogUtil;
import com.example.yeye.sms.util.Utility;

/**
 * Created by yeye on 2016/4/17.
 */
public class Upload {

    public static void SMSUpload(final Activity activity,boolean loadFlag,String type){
        // TODO: 2016/1/21 上传至服务器
        if (loadFlag) {
           /*
            servlet
             */
            String url = IConst.SERVLET_ADDR + "FileServlet";

            HttpUtil.sendHttpRequest(url, "POST", 1, new HttpCallBackListener() {
                @Override
                public void onFinish(final String response) {
                    Boolean uploadSuccess = Utility.handleUploadRequestResponse(response);
                    if (uploadSuccess) {

                    } else {
                        Toast.makeText(activity, R.string.uploadFail, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(activity, R.string.network_is_bad, Toast.LENGTH_LONG).show();
                }
            });

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
