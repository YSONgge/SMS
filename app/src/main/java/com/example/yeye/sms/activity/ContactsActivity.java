package com.example.yeye.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yeye.sms.R;
import com.example.yeye.sms.myMethod.Backup;
import com.example.yeye.sms.myMethod.Regeneration;
import com.example.yeye.sms.myMethod.ShowContactsList;
import com.example.yeye.sms.myMethod.Upload;

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

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_contacts_backup:
                    loadFlag = Backup.doContactsBackup(ContactsActivity.this, userId);
                    Upload.ConUpload(ContactsActivity.this, loadFlag);
                    break;
                case R.id.btn_contacts_content_list:
                    ContactsListActivity.actionStart(ContactsActivity.this);
                    break;
                case R.id.btn_contacts_regeneration:
                    Regeneration regeneration = new Regeneration();
                    regeneration.doContactsRegeneration(ContactsActivity.this,userId);
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
