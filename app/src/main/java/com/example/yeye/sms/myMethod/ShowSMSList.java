package com.example.yeye.sms.myMethod;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.example.yeye.sms.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yeye on 2016/4/16.
 */
public class ShowSMSList {

    public static String getSmsInPhone(Activity activity) {

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse("content://sms");
            ContentResolver cr;
            cr = activity.getContentResolver();
            Cursor cur = cr.query(uri, new String[]{"address", "date", "body", "type"},
                    null, null, null);

            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
                    String body = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                    } else if (intType == 2) {
                        strType = "发送";
                    } else {
                        strType = "null";
                    }

                    smsBuilder.append(strAddress + "--");
                    smsBuilder.append(strType + ": ");
                    smsBuilder.append(body + "\n ");
                    smsBuilder.append("[ ");
                    smsBuilder.append(strDate);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                }
            } else {
                smsBuilder.append("");
            } // end if

        } catch (SQLiteException ex) {
            LogUtil.d("SQLiteException in getSmsInPhone", ex.getMessage());
        }

        return smsBuilder.toString();
    }
}
