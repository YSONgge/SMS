package com.example.yeye.Easy_Backup.myMethod;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.yeye.Easy_Backup.util.IConst;
import com.example.yeye.Easy_Backup.util.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by yeye on 2016/4/17.
 */
public class Upload {

    public void Upload(final Activity activity, String type) {
        // TODO: 2016/4/23 测试上传
        postFile(activity, type);
    }

    public void postFile(final Activity activity, String type) {
        String Servlet = "UploadServlet";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.SMS", type);
        if (!file.exists()) {
            Toast.makeText(activity, "备份文件路径不存在，请重新备份", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = IConst.SERVLET_ADDR + Servlet;
        OkHttpUtils.post()
                .addFile("mFile", type, file)
                .url(url)
                //.file(file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtil.e("上传失败", e.toString());
                        e.printStackTrace();
                        Toast.makeText(activity, "文件上传失败，请检查网络后再试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtil.d("上传文件返回的response", response);
                        if (response.contains("OK")) {
                            Toast.makeText(activity, "文件上传成功！", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "文件上传服务器出现问题，请稍后再试", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }
}
