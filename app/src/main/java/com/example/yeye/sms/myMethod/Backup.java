package com.example.yeye.sms.myMethod;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Xml;
import android.widget.Toast;

import com.example.yeye.sms.util.ContactsField;
import com.example.yeye.sms.util.LogUtil;
import com.example.yeye.sms.util.SmsField;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yeye on 2016/1/21.
 */
public class Backup {


    public static boolean doSMSBackup(Activity activity, int userId) {

        String address;
        String person;
        String date;
        String protocol;
        String read;
        String status;
        String type;
        String reply_path_present;
        String body;
        String locked;
        String error_code;
        String seen;
        /**
         * 存到本地
         */
        boolean loadFlag = false;
        Uri urisms = Uri.parse("content://sms");
        ContentResolver cr;
        String[] projection = new String[]{SmsField.ADDRESS, SmsField.PERSON, SmsField.DATE, SmsField.PROTOCOL,
                SmsField.READ, SmsField.STATUS, SmsField.TYPE, SmsField.REPLY_PATH_PRESENT,
                SmsField.BODY, SmsField.LOCKED, SmsField.ERROR_CODE, SmsField.SEEN}; // type=1是收件箱，==2是发件箱;read=0表示未读，read=1表示读过，seen=0表示未读，seen=1表示读过

        cr = activity.getContentResolver();
        Cursor cursor = cr.query(urisms, projection, null, null, "_id asc");
        if (!cursor.moveToFirst()) {
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
                File file = new File(path + "/" + userId + "_SMS.xml");
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(file);
                    serializer.setOutput(fos, "UTF-8");
                    serializer.startDocument("UTF-8", true);
                    serializer.startTag(null, "sms");

                    do {
                        // 如果address == null，xml文件中是不会生成该属性的,为了保证解析时，属性能够根据索引一一对应，必须要保证所有的item标记的属性数量和顺序是一致的
                        address = cursor.getString(cursor.getColumnIndex(SmsField.ADDRESS));
                        if (address == null) {
                            address = "";
                        }
                        person = cursor.getString(cursor.getColumnIndex(SmsField.PERSON));
                        if (person == null) {
                            person = "";
                        }
                        date = cursor.getString(cursor.getColumnIndex(SmsField.DATE));
                        if (date == null) {
                            date = "";
                        }
                        protocol = cursor.getString(cursor.getColumnIndex(SmsField.PROTOCOL));
                        if (protocol == null) {// 为了便于xml解析
                            protocol = "";
                        }
                        read = cursor.getString(cursor.getColumnIndex(SmsField.READ));
                        if (read == null) {
                            read = "";
                        }
                        status = cursor.getString(cursor.getColumnIndex(SmsField.STATUS));
                        if (status == null) {
                            status = "";
                        }
                        type = cursor.getString(cursor.getColumnIndex(SmsField.TYPE));
                        if (type == null) {
                            type = "";
                        }
                        reply_path_present = cursor.getString(cursor.getColumnIndex(SmsField.REPLY_PATH_PRESENT));
                        if (reply_path_present == null) {// 为了便于XML解析
                            reply_path_present = "";
                        }
                        body = cursor.getString(cursor.getColumnIndex(SmsField.BODY));
                        if (body == null) {
                            body = "";
                        }
                        locked = cursor.getString(cursor.getColumnIndex(SmsField.LOCKED));
                        if (locked == null) {
                            locked = "";
                        }
                        error_code = cursor.getString(cursor.getColumnIndex(SmsField.ERROR_CODE));
                        if (error_code == null) {
                            error_code = "";
                        }
                        seen = cursor.getString(cursor.getColumnIndex(SmsField.SEEN));
                        if (seen == null) {
                            seen = "";
                        }
                        // 生成xml子标记
                        // 开始标记
                        serializer.startTag(null, "item");
                        // 加入属性
                        serializer.attribute(null, SmsField.ADDRESS, address);
                        serializer.attribute(null, SmsField.PERSON, person);
                        serializer.attribute(null, SmsField.DATE, date);
                        serializer.attribute(null, SmsField.PROTOCOL, protocol);
                        serializer.attribute(null, SmsField.READ, read);
                        serializer.attribute(null, SmsField.STATUS, status);
                        serializer.attribute(null, SmsField.TYPE, type);
                        serializer.attribute(null, SmsField.REPLY_PATH_PRESENT, reply_path_present);
                        serializer.attribute(null, SmsField.BODY, body);
                        serializer.attribute(null, SmsField.LOCKED, locked);
                        serializer.attribute(null, SmsField.ERROR_CODE, error_code);
                        serializer.attribute(null, SmsField.SEEN, seen);
                        // 结束标记
                        serializer.endTag(null, "item");

                    } while (cursor.moveToNext());
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
                // TODO: 2016/4/17 修改xml格式 
                XmlSerializer serializer = Xml.newSerializer();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(userId + "_SMS.xml");
                    serializer.setOutput(fos, "UTF-8");
                    serializer.startDocument("UTF-8", true);

                    serializer.startTag(null, "sms");
                    do {
                        // 如果address == null，xml文件中是不会生成该属性的,为了保证解析时，属性能够根据索引一一对应，必须要保证所有的item标记的属性数量和顺序是一致的
                        address = cursor.getString(cursor.getColumnIndex(SmsField.ADDRESS));
                        if (address == null) {
                            address = "";
                        }
                        person = cursor.getString(cursor.getColumnIndex(SmsField.PERSON));
                        if (person == null) {
                            person = "";
                        }
                        date = cursor.getString(cursor.getColumnIndex(SmsField.DATE));
                        if (date == null) {
                            date = "";
                        }
                        protocol = cursor.getString(cursor.getColumnIndex(SmsField.PROTOCOL));
                        if (protocol == null) {// 为了便于xml解析
                            protocol = "";
                        }
                        read = cursor.getString(cursor.getColumnIndex(SmsField.READ));
                        if (read == null) {
                            read = "";
                        }
                        status = cursor.getString(cursor.getColumnIndex(SmsField.STATUS));
                        if (status == null) {
                            status = "";
                        }
                        type = cursor.getString(cursor.getColumnIndex(SmsField.TYPE));
                        if (type == null) {
                            type = "";
                        }
                        reply_path_present = cursor.getString(cursor.getColumnIndex(SmsField.REPLY_PATH_PRESENT));
                        if (reply_path_present == null) {// 为了便于XML解析
                            reply_path_present = "";
                        }
                        body = cursor.getString(cursor.getColumnIndex(SmsField.BODY));
                        if (body == null) {
                            body = "";
                        }
                        locked = cursor.getString(cursor.getColumnIndex(SmsField.LOCKED));
                        if (locked == null) {
                            locked = "";
                        }
                        error_code = cursor.getString(cursor.getColumnIndex(SmsField.ERROR_CODE));
                        if (error_code == null) {
                            error_code = "";
                        }
                        seen = cursor.getString(cursor.getColumnIndex(SmsField.SEEN));
                        if (seen == null) {
                            seen = "";
                        }
                        // 生成xml子标记
                        // 开始标记
                        serializer.startTag(null, "item");
                        // 加入属性
                        serializer.attribute(null, SmsField.ADDRESS, address);
                        serializer.attribute(null, SmsField.PERSON, person);
                        serializer.attribute(null, SmsField.DATE, date);
                        serializer.attribute(null, SmsField.PROTOCOL, protocol);
                        serializer.attribute(null, SmsField.READ, read);
                        serializer.attribute(null, SmsField.STATUS, status);
                        serializer.attribute(null, SmsField.TYPE, type);
                        serializer.attribute(null, SmsField.REPLY_PATH_PRESENT, reply_path_present);
                        serializer.attribute(null, SmsField.BODY, body);
                        serializer.attribute(null, SmsField.LOCKED, locked);
                        serializer.attribute(null, SmsField.ERROR_CODE, error_code);
                        serializer.attribute(null, SmsField.SEEN, seen);
                        // 结束标记
                        serializer.endTag(null, "item");

                    } while (cursor.moveToNext());
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

        }
        return loadFlag;
    }

    public static boolean doContactsBackup(Activity activity, int userId) {
        boolean loadFlag = false;
        String displayName;
        String number;
        //Uri urisms = Uri.parse("content://com.android.contacts/contacts");
        ContentResolver cr;
        cr = activity.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);

        if (!cursor.moveToFirst()) {
            LogUtil.e("联系人条数", "手机中没有联系人");
            Toast.makeText(activity, "没有短信可备份", Toast.LENGTH_LONG).show();
        } else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                /**
                 * 存到sdcard 里
                 */
                XmlSerializer serializer = Xml.newSerializer();

                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.SMS";
                File file1 = new File(path);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File file = new File(path + "/" + userId + "_CONTACTS.xml");
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(file);
                    serializer.setOutput(fos, "UTF-8");
                    serializer.startDocument("UTF-8", true);
                    serializer.startTag(null, "contacts");

                    do {
                        displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        if (displayName == null) {
                            displayName = "";
                        }
                        number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (number == null) {
                            displayName = "";
                        }
                        serializer.startTag(null, "item");
                        // 加入属性
                        serializer.attribute(null, ContactsField.DISPLAY_NAME, displayName);
                        serializer.attribute(null, ContactsField.NUMBER, number);
                        // 结束标记
                        serializer.endTag(null, "item");

                    } while (cursor.moveToNext());
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
                    fos = new FileOutputStream(userId + "_CONTACTS.xml");
                    serializer.setOutput(fos, "UTF-8");
                    serializer.startDocument("UTF-8", true);
                    serializer.startTag(null, "contacts");
                    do {
                        displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        if (displayName == null) {
                            displayName = "";
                        }
                        number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (number == null) {
                            displayName = "";
                        }
                        serializer.startTag(null, "item");
                        // 加入属性
                        serializer.attribute(null, ContactsField.DISPLAY_NAME, displayName);
                        serializer.attribute(null, ContactsField.NUMBER, number);
                        // 结束标记
                        serializer.endTag(null, "item");

                    } while (cursor.moveToNext());
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

        }
        return loadFlag;
    }
}
