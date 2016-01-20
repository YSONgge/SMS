package com.example.yeye.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yeye.sms.R;

public class SMSActivity extends AppCompatActivity {

    public int userId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        userId = getIntent().getIntExtra("userId", -1);
    }

    public static void actionStart(Context context,int userId) {
        Intent i = new Intent(context, SMSActivity.class);
        i.putExtra("UserId", userId);
        context.startActivity(i);
    }
}
