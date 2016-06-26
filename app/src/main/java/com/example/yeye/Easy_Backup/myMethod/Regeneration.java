package com.example.yeye.Easy_Backup.myMethod;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Xml;
import android.widget.Toast;

import com.example.yeye.Easy_Backup.util.ContactsItem;
import com.example.yeye.Easy_Backup.util.LogUtil;
import com.example.yeye.Easy_Backup.util.SmsField;
import com.example.yeye.Easy_Backup.util.SmsItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeye on 2016/1/21.
 */
public class Regeneration {

    private List<ContactsItem> conItems;
    List<SmsItem> smsItems;
    public void doSMSRegeneration(Activity activity, int userId) {

        ContentResolver conResolver;
        conResolver = activity.getContentResolver();
        /**
         * 放一个解析xml文件的模块
         */
        smsItems = getSmsItemsFromXml(activity,  userId);
        LogUtil.d("sqk", "after smsItems");
        for (SmsItem item : smsItems) {

            // 判断短信数据库中是否已包含该条短信，如果有，则不需要恢复
            Cursor cursor = conResolver.query(Uri.parse("content://sms"), new String[]{SmsField.DATE}, SmsField.DATE + "=?",
                    new String[]{item.getDate()}, null);

            if (!cursor.moveToFirst()) {// 没有该条短信

                ContentValues values = new ContentValues();
                values.put(SmsField.ADDRESS, item.getAddress());
                // 如果是空字符串说明原来的值是null，所以这里还原为null存入数据库
                values.put(SmsField.PERSON, item.getPerson().equals("") ? null : item.getPerson());
                values.put(SmsField.DATE, item.getDate());
                values.put(SmsField.PROTOCOL, item.getProtocol().equals("") ? null : item.getProtocol());
                values.put(SmsField.READ, item.getRead());
                values.put(SmsField.STATUS, item.getStatus());
                values.put(SmsField.TYPE, item.getType());
                values.put(SmsField.REPLY_PATH_PRESENT, item.getReply_path_present().equals("") ? null : item.getReply_path_present());
                values.put(SmsField.BODY, item.getBody());
                values.put(SmsField.LOCKED, item.getLocked());
                values.put(SmsField.ERROR_CODE, item.getError_code());
                values.put(SmsField.SEEN, item.getSeen());
                conResolver.insert(Uri.parse("content://sms"), values);
            }
            cursor.close();

        }
        LogUtil.d("短信已恢复份完了", "真的");
       // activity.setTitle("SMS");
        //Toast.makeText(activity, "短信恢复完成。", Toast.LENGTH_LONG).show();
    }

    public List<SmsItem> getSmsItemsFromXml(Activity activity, int userId) {

        SmsItem smsItem = null;
        XmlPullParser parser = Xml.newPullParser();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.SMS";
        String absolutePath = path + "/" + userId + "_SMS.xml";
        LogUtil.d("路径",absolutePath);
        File file = new File(absolutePath);
        if (!file.exists()) {

            Toast.makeText(activity, "message.xml短信备份文件不在sd卡中", Toast.LENGTH_LONG).show();

        }
        try {
            FileInputStream fis = null;

            fis = new FileInputStream(file);

            parser.setInput(fis, "UTF-8");

            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        smsItems = new ArrayList<SmsItem>();
                        break;

                    case XmlPullParser.START_TAG: // 如果遇到开始标记，如<smsItems>,<smsItem>等
                        if ("item".equals(parser.getName())) {
                            smsItem = new SmsItem();

                            smsItem.setAddress(parser.getAttributeValue(0));
                            smsItem.setPerson(parser.getAttributeValue(1));
                            smsItem.setDate(parser.getAttributeValue(2));
                            smsItem.setProtocol(parser.getAttributeValue(3));
                            smsItem.setRead(parser.getAttributeValue(4));
                            smsItem.setStatus(parser.getAttributeValue(5));
                            smsItem.setType(parser.getAttributeValue(6));
                            smsItem.setReply_path_present(parser.getAttributeValue(7));
                            smsItem.setBody(parser.getAttributeValue(8));
                            smsItem.setLocked(parser.getAttributeValue(9));
                            smsItem.setError_code(parser.getAttributeValue(10));
                            smsItem.setSeen(parser.getAttributeValue(11));

                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束标记,如</smsItems>,</smsItem>等
                        if ("item".equals(parser.getName())) {
                            smsItems.add(smsItem);
                           LogUtil.d("从xml文件里拿数据", smsItems.toString());
                            smsItem = null;
                        }
                        break;
                }
                try {
                    event = parser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {

          //  Looper.prepare();
            Toast.makeText(activity, "短信恢复出错", Toast.LENGTH_LONG).show();
          //  Looper.loop();
            e.printStackTrace();

        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
          //  Looper.prepare();
            Toast.makeText(activity, "短信恢复出错", Toast.LENGTH_LONG).show();
          //  Looper.loop();
            e.printStackTrace();

        }
        return smsItems;
    }


    public void doContactsRegeneration(Activity activity,int userId) {

        ContentResolver conResolver;
        conResolver = activity.getContentResolver();
        /**
         * 放一个解析xml文件的模块
         */
        conItems = getConItemsFromXml(activity,  userId);
        LogUtil.d("sqk", "after ContactsItem");
        for (ContactsItem item : conItems) {

            Cursor cursor = conResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                    new String[]{item.getNumber()}, null);

            if (!cursor.moveToFirst()) {

                ContentValues values = new ContentValues();
                long contactId = ContentUris.parseId(conResolver.insert(Uri.parse("content://com.android.contacts/raw_contacts"), values));

                Uri uri = Uri.parse("content://com.android.contacts/data");
                // 添加姓名
                values.put("raw_contact_id", contactId);
                values.put("mimetype", "vnd.android.cursor.item/name");
                values.put("data2",  item.getDisplayName());
                conResolver.insert(uri, values);

                // 添加电话
                values.clear();
                values.put("raw_contact_id", contactId);
                values.put("mimetype", "vnd.android.cursor.item/phone_v2");
                values.put("data2", "2");
                values.put("data1", item.getNumber());
                conResolver.insert(uri, values);

            }
            cursor.close();
            LogUtil.d("联系人备份完了","yes");
        }
    }
    public List<ContactsItem> getConItemsFromXml(Activity activity, int userId) {

        ContactsItem conItem = null;
        XmlPullParser parser = Xml.newPullParser();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.SMS";
        String absolutePath = path + "/" + userId + "_CONTACTS.xml";
        LogUtil.d("路径",absolutePath);
        File file = new File(absolutePath);
        if (!file.exists()) {

           // Looper.prepare();
            Toast.makeText(activity, "联系人备份文件不在sd卡中", Toast.LENGTH_LONG).show();
           // Looper.loop();//退出线程
//			return null;
        }
        try {
            FileInputStream fis = null;

            fis = new FileInputStream(file);

            parser.setInput(fis, "UTF-8");

            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        conItems = new ArrayList<ContactsItem>();
                        break;

                    case XmlPullParser.START_TAG: // 如果遇到开始标记，如<smsItems>,<smsItem>等
                        if ("item".equals(parser.getName())) {
                            conItem = new ContactsItem();

                            conItem.setDisplayName(parser.getAttributeValue(0));
                            conItem.setNumber(parser.getAttributeValue(1));

                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束标记,如</smsItems>,</smsItem>等
                        if ("item".equals(parser.getName())) {
                            conItems.add(conItem);
                            LogUtil.d("从文件里取联系人","正在取");
                            conItem = null;
                        }
                        break;
                }
                try {
                    event = parser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException | XmlPullParserException e) {

//            Looper.prepare();
            Toast.makeText(activity, "联系人恢复出错", Toast.LENGTH_LONG).show();
       //     Looper.loop();
            e.printStackTrace();

        }
        return conItems;
    }
}
