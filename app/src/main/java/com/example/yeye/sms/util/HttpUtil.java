package com.example.yeye.sms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yeye on 2016/1/16.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address, final String method, final String data, final HttpCallBackListener listener) {
        LogUtil.d("address", address);
        LogUtil.d("PostData", data == null ? "" : data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                PrintWriter pw = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if ("POST".equals(method.toUpperCase())) {
                        pw = new PrintWriter(connection.getOutputStream());
                        pw.write(data);
                        pw.flush();
                    }
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("HTTPUtil", e.toString());
                    if (listener != null)
                        listener.onError(e);
                } finally {
                    close(connection, pw, reader);
                }
            }
        }).start();
    }
    private static void close(HttpURLConnection connection, PrintWriter pw, BufferedReader reader) {
        try {
            if (pw != null) {
                pw.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e("HttpUtil", e.toString());
        }
    }
}
