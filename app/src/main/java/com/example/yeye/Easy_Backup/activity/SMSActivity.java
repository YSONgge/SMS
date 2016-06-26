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

public class SMSActivity extends AppCompatActivity {

    public Button btnSMSBackUp, btnSMSRegeneration, btnSMSContentList;

    public int userId;
    public boolean loadFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        userId = getIntent().getIntExtra("UserId", -1);
        SMSActivity.this.setTitle("短信备份");
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
                    if(loadFlag){
                        new Upload().Upload(SMSActivity.this,userId + "_SMS.xml");

                    }else{
                        Toast.makeText(SMSActivity.this, "备份文件损坏，上传失败", Toast.LENGTH_LONG).show();
                        LogUtil.e("Backup", "本地备份失败");
                    }
                    //new Upload().Upload(SMSActivity.this,loadFlag,userId + "_SMS.xml");
                    break;
                case R.id.btn_sms_content_list:
                    SMSListActivity.actionStart(SMSActivity.this);
                    break;
                case R.id.btn_sms_regeneration:
                    new Download().DownloadFlie(SMSActivity.this, userId + "_SMS");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                Thread.sleep(2000);
                                LogUtil.i("do regeneration", "sms");
                                new Regeneration().doSMSRegeneration(SMSActivity.this, userId);
                               // Toast.makeText(SMSActivity.this,"短信恢复活动结束",Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                   /* SMSActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(SMSActivity.this,"短信恢复活动结束",Toast.LENGTH_LONG).show();

                        }
                    });*/

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
