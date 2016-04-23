package com.example.yeye.sms.myMethod;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.yeye.sms.R;
import com.example.yeye.sms.util.HttpCallBackListener;
import com.example.yeye.sms.util.HttpUtil;
import com.example.yeye.sms.util.IConst;
import com.example.yeye.sms.util.LogUtil;
import com.example.yeye.sms.util.Utility;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by yeye on 2016/4/17.
 */
public class Upload  {

    public void Upload(final Activity activity,boolean loadFlag,String type){
        // TODO: 2016/4/23 测试上传
        if (loadFlag) {

            postFile(activity,type);

        } else {
            Toast.makeText(activity, "备份文件损坏，上传失败", Toast.LENGTH_LONG).show();
            LogUtil.e("Backup", "本地备份失败");
        }
    }

    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request)
        {
            super.onBefore(request);

        }

        @Override
        public void onAfter()
        {
            super.onAfter();
        }

        @Override
        public void onError(Call call, Exception e)
        {
            LogUtil.e("上传失败", e.toString() );
            e.printStackTrace();
            //Toast.makeText()
        }

        @Override
        public void onResponse(String response)
        {
            //mTv.setText("onResponse:" + response);
           LogUtil.d("上传文件返回的response",response);
           // Toast.makeText()
        }

    }
    public void postFile(Activity activity,String type)
    {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.SMS", type);
        if (!file.exists())
        {
            Toast.makeText(activity, "备份文件路径不存在，请重新备份", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = IConst.SERVLET_ADDR + "FileServlet";
        OkHttpUtils
                .postFile()
                .url(url)
                .file(file)
                .build()
                .execute(new MyStringCallback());

    }
}
