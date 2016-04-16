package com.example.yeye.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.yeye.sms.ActivityCollector.ActivityCollector;
import com.example.yeye.sms.R;
import com.example.yeye.sms.myMethod.ShowSMSList;

public class SMSListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TextView tv = new TextView(this);
        ScrollView sv = new ScrollView(this);

        if(ShowSMSList.getSmsInPhone(this).equals("")){
            setContentView(R.layout.activity_smslist);
        }else {
            tv.setText(ShowSMSList.getSmsInPhone(this));
            sv.addView(tv);
            setContentView(sv);
        }

    }

    public static void actionStart(Context context) {
        Intent i = new Intent(context, SMSListActivity.class);
       // i.putExtra("UserId", userId);
        context.startActivity(i);
    }
}
