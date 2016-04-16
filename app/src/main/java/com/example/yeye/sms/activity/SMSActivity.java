package com.example.yeye.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yeye.sms.R;
import com.example.yeye.sms.myMethod.Backup;

public class SMSActivity extends AppCompatActivity {

    public Button btnSMSBackUp, btnSMSRegeneration, btnSMSContentList;

    public int userId;

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
                    Backup.doSMSBackup(SMSActivity.this,userId);
                    break;
                case R.id.btn_sms_content_list:
                    SMSListActivity.actionStart(SMSActivity.this);
                    break;
                case R.id.btn_sms_regeneration:
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
