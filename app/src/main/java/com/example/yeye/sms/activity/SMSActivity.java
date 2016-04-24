package com.example.yeye.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yeye.sms.R;
import com.example.yeye.sms.myMethod.Backup;
import com.example.yeye.sms.myMethod.Download;
import com.example.yeye.sms.myMethod.Regeneration;
import com.example.yeye.sms.myMethod.Upload;

public class SMSActivity extends AppCompatActivity {

    public Button btnSMSBackUp, btnSMSRegeneration, btnSMSContentList;

    public int userId;
    public boolean loadFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        userId = getIntent().getIntExtra("userId", -1);
        btnSMSBackUp = (Button) findViewById(R.id.btn_sms_backUp);
        btnSMSRegeneration = (Button) findViewById(R.id.btn_sms_regeneration);
        btnSMSContentList = (Button) findViewById(R.id.btn_sms_content_list);

        OnClickListener listener = new OnClickListener();
        btnSMSBackUp.setOnClickListener(listener);
        btnSMSContentList.setOnClickListener(listener);
        btnSMSRegeneration.setOnClickListener(listener);
    }
    public class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_sms_backUp:
                    loadFlag = Backup.doSMSBackup(SMSActivity.this,userId);
                    new Upload().Upload(SMSActivity.this,loadFlag,userId + "_SMS.xml");
                    break;
                case R.id.btn_sms_content_list:
                    SMSListActivity.actionStart(SMSActivity.this);
                    break;
                case R.id.btn_sms_regeneration:
                    new Download().DownloadFlie(SMSActivity.this,userId + "_SMS");
                    Regeneration regeneration = new Regeneration();
                    regeneration.doSMSRegeneration(SMSActivity.this, userId);

                    break;
            }
        }
    }


    public static void actionStart(Context context, int userId) {
        Intent i = new Intent(context, SMSActivity.class);
        i.putExtra("UserId", userId);
        context.startActivity(i);
    }
}
