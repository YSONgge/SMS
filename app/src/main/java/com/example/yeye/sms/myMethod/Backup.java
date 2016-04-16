package com.example.yeye.sms.myMethod;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Xml;
import android.widget.Toast;

import com.example.yeye.sms.activity.SMSActivity;
import com.example.yeye.sms.util.LogUtil;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by yeye on 2016/1/21.
 */
public class Backup {


    public static void doSMSBackup(Activity activity, int userId) {

        /**
         * 存到本地
         */
        boolean loadFlag = true;
        Uri urisms = Uri.parse("content://sms");
        ContentResolver cr;
        cr = activity.getContentResolver();
        Cursor c = cr.query(urisms, new String[]{"address", "date", "body", "type"},
                null, null, null);
        if (!c.moveToFirst()) {
            LogUtil.e("短信条数", "手机中没有短信");
            Toast.makeText(activity, "没有短信可备份", Toast.LENGTH_LONG).show();
        } else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                /**
                 * 存到sdcard 里
                 */
                XmlSerializer serializer = Xml.newSerializer();

                //File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() +"/sms" + userId + ".xml");
                //File file = new File("/mnt/sdcard/Android/data" + "/sms" + userId + ".xml");
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.SMS";
                File file1 = new File(path);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File file = new File(path + "/sms" + userId + "SMS.xml");
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(file);
                    serializer.setOutput(fos, "UTF-8");
                    serializer.startDocument("UTF-8", true);
                    serializer.startTag(null, "sms");

                    while (c.moveToNext()) {
                        serializer.startTag(null, "address");
                        serializer.text(c.getString(c.getColumnIndex("address")));
                        serializer.endTag(null, "address");

                        serializer.startTag(null, "date");
                        long seconds = c.getLong(c.getColumnIndex("date"));
                        Calendar time = Calendar.getInstance();
                        time.setTimeInMillis(seconds);
                        String date = time.get(Calendar.YEAR) + "-" + time.get(Calendar.MONTH) + "-" + time.get(Calendar.DAY_OF_MONTH) +
                                " " + time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND);
                        serializer.text(date);
                        serializer.endTag(null, "date");

                        serializer.startTag(null, "body");
                        serializer.text(c.getString(c.getColumnIndex("body")));
                        serializer.endTag(null, "body");

                        serializer.startTag(null, "type");
                        serializer.text(c.getString(c.getColumnIndex("type")));
                        serializer.endTag(null, "type");
                    }
                    serializer.endTag(null, "sms");
                    serializer.endDocument();
                    loadFlag = true;
                    Toast.makeText(activity, "（SD）本地备份完成", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    loadFlag = false;
                    LogUtil.e("SMS backUp", e.toString());
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    serializer = null;
                }
            } else {
                /**
                 * 存到手机里
                 */
                XmlSerializer serializer = Xml.newSerializer();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream("sms" + userId + ".xml");
                    serializer.setOutput(fos, "UTF-8");
                    serializer.startDocument("UTF-8", true);

                    serializer.startTag(null, "sms");
                    while (c.moveToNext()) {
                        serializer.startTag(null, "adress");
                        serializer.text(c.getString(c.getColumnIndex("address")));
                        serializer.endTag(null, "adress");

                        serializer.startTag(null, "date");
                        long seconds = c.getLong(c.getColumnIndex("date"));
                        Calendar time = Calendar.getInstance();
                        time.setTimeInMillis(seconds);
                        String date = time.get(Calendar.YEAR) + "-" + time.get(Calendar.MONTH) + "-" + time.get(Calendar.DAY_OF_MONTH) +
                                " " + time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND);
                        serializer.text(date);
                        serializer.endTag(null, "date");

                        serializer.startTag(null, "body");
                        serializer.text(c.getString(c.getColumnIndex("body")));
                        serializer.endTag(null, "body");

                        serializer.startTag(null, "type");
                        serializer.text(c.getString(c.getColumnIndex("type")));
                        serializer.endTag(null, "type");
                    }
                    serializer.endTag(null, "sms");
                    serializer.endDocument();
                    loadFlag = true;
                    Toast.makeText(activity, "手机内存备份完成", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    loadFlag = false;
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    serializer = null;
                }
            }

            // TODO: 2016/1/21 上传至服务器
            if (loadFlag) {

            } else {
                Toast.makeText(activity, "备份文件损坏，上传失败", Toast.LENGTH_LONG).show();
                LogUtil.e("Backup", "本地备份失败");
            }
        }
    }

    public static void doContactsBackup(Activity activity, int userId) {
        boolean loadFlag;
        //Uri urisms = Uri.parse("content://com.android.contacts/contacts");
        ContentResolver cr;
        cr = activity.getContentResolver();
        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if (!c.moveToFirst()) {
            LogUtil.e("联系人条数", "手机中没有联系人");
            Toast.makeText(activity, "没有短信可备份", Toast.LENGTH_LONG).show();
        } else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                /**
                 * 存到sdcard 里
                 */
                XmlSerializer serializer = Xml.newSerializer();

                //File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() +"/sms" + userId + ".xml");
                //File file = new File("/mnt/sdcard/Android/data" + "/sms" + userId + ".xml");
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.SMS";
                File file1 = new File(path);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File file = new File(path + "/sms" + userId + "CONTACTS.xml");
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(file);
                    serializer.setOutput(fos, "UTF-8");
                    serializer.startDocument("UTF-8", true);
                    serializer.startTag(null, "contacts");
                    while (c.moveToNext()) {
                        serializer.startTag(null, "name");
                        serializer.text(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                        serializer.endTag(null, "name");

                        serializer.startTag(null, "number");
                        serializer.text(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        serializer.endTag(null, "number");

                    }
                    serializer.endTag(null, "contacts");
                    serializer.endDocument();
                    loadFlag = true;
                    Toast.makeText(activity, "本地SD备份完成", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    loadFlag = false;
                    //Toast.makeText(activity, "本地备份出现异常", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    serializer = null;
                }
            } else {
                /**
                 * 存到手机里
                 */
                XmlSerializer serializer = Xml.newSerializer();
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream("sms" + userId + ".xml");
                    serializer.setOutput(fos, "UTF-8");
                    serializer.startDocument("UTF-8", true);
                    serializer.startTag(null, "contacts");
                    while (c.moveToNext()) {
                        serializer.startTag(null, "name");
                        serializer.text(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                        serializer.endTag(null, "name");

                        serializer.startTag(null, "number");
                        serializer.text(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        serializer.endTag(null, "number");

                    }
                    serializer.endTag(null, "contacts");
                    serializer.endDocument();
                    loadFlag = true;
                    Toast.makeText(activity, "本地手机备份完成", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    loadFlag = false;
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    serializer = null;
                }
            }
// TODO: 2016/4/10 上传服务器
            if (loadFlag) {

            } else {
                Toast.makeText(activity, "备份文件损坏，上传失败", Toast.LENGTH_LONG).show();
                LogUtil.e("Backup", "本地备份失败");
            }
        }
    }
}