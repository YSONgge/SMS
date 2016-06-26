package com.example.yeye.Easy_Backup.myMethod;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import com.example.yeye.Easy_Backup.util.IConst;
import com.example.yeye.Easy_Backup.util.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by yeye on 2016/4/23.
 */
public class Download {
    private static final String TAG = "下载文件";

    public void DownloadFlie(final Activity activity,String type){
        // TODO: 2016/4/23 测试上传下载
           DownloadFile(activity, type);
    }
    public void DownloadFile(final Activity activity,String type)
    {
        String Servlet = "DownloadServlet";

        final String userIdType =type;

        String url = IConst.SERVLET_ADDR + Servlet + "?userIdType="+ userIdType ;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.SMS", type + ".xml")//
                {

                    @Override
                    public void inProgress(float progress, long total) {

                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtil.e(TAG, "onError :" + e.getMessage());
                        Toast.makeText(activity,"文件下载失败，请检查网络后重新恢复",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(File file) {
                        Toast.makeText(activity, "文件下载完成，准备备份中...", Toast.LENGTH_LONG).show();
                        if (userIdType.contains("SMS")){
                            Toast.makeText(activity,"短信恢复活动结束",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(activity,"联系人恢复活动结束",Toast.LENGTH_LONG).show();
                        }

                        LogUtil.d(TAG, "onResponse :" + file.getAbsolutePath());
                    }
                });
    }


}
