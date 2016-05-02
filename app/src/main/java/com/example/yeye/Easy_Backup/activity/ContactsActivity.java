package com.example.yeye.Easy_Backup.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yeye.Easy_Backup.R;
import com.example.yeye.Easy_Backup.myMethod.Backup;
import com.example.yeye.Easy_Backup.myMethod.Download;
import com.example.yeye.Easy_Backup.myMethod.Regeneration;
import com.example.yeye.Easy_Backup.myMethod.Upload;

public class ContactsActivity extends AppCompatActivity {

    public int userId;
    public boolean loadFlag;
    public Button btnConBackUp, btnConRegeneration, btnConContentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        userId = getIntent().getIntExtra("userId", -1);

        ContactsActivity.this.setTitle("通讯录备份");
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
                  /*  progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.setTitle("备份文件时间较长，请耐心等待...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();*/

                    new Download().DownloadFlie(ContactsActivity.this, userId + "_CONTACTS");
                   // Regeneration regeneration = new Regeneration();
                    //regeneration.doContactsRegeneration(ContactsActivity.this, userId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    new Regeneration().doContactsRegeneration(ContactsActivity.this,userId);
                   // progressDialog.show();
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
