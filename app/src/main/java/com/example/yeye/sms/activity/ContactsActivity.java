package com.example.yeye.sms.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yeye.sms.R;
import com.example.yeye.sms.myMethod.Backup;
import com.example.yeye.sms.myMethod.Download;
import com.example.yeye.sms.myMethod.Regeneration;
import com.example.yeye.sms.myMethod.ShowContactsList;
import com.example.yeye.sms.myMethod.Upload;
import com.example.yeye.sms.util.HttpUtil;
import com.example.yeye.sms.util.IConst;
import com.example.yeye.sms.util.Utility;

import java.util.List;
import java.util.Map;

public class ContactsActivity extends AppCompatActivity {

    public int userId;
    public boolean loadFlag;
    public Button btnConBackUp, btnConRegeneration, btnConContentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        userId = getIntent().getIntExtra("userId", -1);

        btnConBackUp = (Button) findViewById(R.id.btn_contacts_backup);
        btnConRegeneration = (Button) findViewById(R.id.btn_contacts_regeneration);
        btnConContentList = (Button) findViewById(R.id.btn_contacts_content_list);

        OnClickListener listener = new OnClickListener();
        btnConBackUp.setOnClickListener(listener);
        btnConRegeneration.setOnClickListener(listener);
        btnConContentList.setOnClickListener(listener);
    }

    public class OnClickListener implements View.OnClickListener {
       // final ProgressDialog progressDialog = new ProgressDialog(ContactsActivity.this);
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_contacts_backup:
                    /*
                     progressDialog
                     */

                   /* progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    progressDialog.setTitle("开始备份，请耐心等待...");*/

                    loadFlag = Backup.doContactsBackup(ContactsActivity.this, userId);
                    new Upload().Upload(ContactsActivity.this, loadFlag, userId + "_CONTACTS.xml");
                  //  progressDialog.dismiss();
                    break;
                case R.id.btn_contacts_content_list:
                    ContactsListActivity.actionStart(ContactsActivity.this);
                    break;
                case R.id.btn_contacts_regeneration:
                   /* progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    progressDialog.setTitle("备份文件时间较长，请耐心等待...");*/
                    new Download().DownloadFlie(ContactsActivity.this, userId + "_CONTACTS.xml");
                    Regeneration regeneration = new Regeneration();
                    regeneration.doContactsRegeneration(ContactsActivity.this, userId);
                    //progressDialog.dismiss();
                    break;
                        }
                    }
            }

        public static void actionStart(Context context, int userId) {
            Intent i = new Intent(context, ContactsActivity.class);
            i.putExtra("UserId", userId);
            context.startActivity(i);
        }
    }
