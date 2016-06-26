package com.example.yeye.Easy_Backup.myMethod;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.provider.ContactsContract;

import com.example.yeye.Easy_Backup.util.LogUtil;

/**
 * Created by yeye on 2016/4/16.
 */
public class ShowContactsList {
    public static String getNumberInPhone(Activity activity) {

        StringBuilder smsBuilder = new StringBuilder();

        try {
            ContentResolver cr;
            cr = activity.getContentResolver();
            Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cur.moveToFirst()) {
                smsBuilder.append(" ");
                int index_Name = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int index_Number = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                do {
                    String name = cur.getString(index_Name);
                    String number = cur.getString(index_Number);

                        for (;name.length()<5;){
                            name = name +"    ";
                        }

                    smsBuilder.append(name + "  --  ");
                    smsBuilder.append(number + "\n\n ");

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
