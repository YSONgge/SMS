package com.example.yeye.Easy_Backup.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yeye.Easy_Backup.R;
import com.example.yeye.Easy_Backup.myMethod.Backup;
import com.example.yeye.Easy_Backup.myMethod.Download;
import com.example.yeye.Easy_Backup.myMethod.Regeneration;
import com.example.yeye.Easy_Backup.myMethod.Upload;
import com.example.yeye.Easy_Backup.util.LogUtil;

public class ContactsActivity extends AppCompatActivity {

    public int userId;
    public boolean loadFlag;
    public Button btnConBackUp, btnConRegeneration, btnConContentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        userId = getIntent().getIntExtra("UserId", -1);

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

                    loadFlag = Backup.doContactsBackup(ContactsActivity.this, userId);
                    if(loadFlag){
                        new Upload().Upload(ContactsActivity.this, userId + "_CONTACTS.xml");
                    }else{
                        //Toast.makeText(ContactsActivity.this, "备份文件损坏，上传失败", Toast.LENGTH_LONG).show();
                        LogUtil.e("Backup", "本地备份失败");
                    }

                  //  progressDialog.dismiss();
                    break;
                case R.id.btn_contacts_content_list:
                    ContactsListActivity.actionStart(ContactsActivity.this);
                    break;
                case R.id.btn_contacts_regeneration:

                    LogUtil.i("down userid", userId + "");
                    new Download().DownloadFlie(ContactsActivity.this, userId + "_CONTACTS");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                Thread.sleep(2000);
                                LogUtil.i("do regeneration", "contacts");
                                new Regeneration().doContactsRegeneration(ContactsActivity.this, userId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


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
