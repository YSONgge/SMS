package com.example.yeye.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.yeye.sms.R;
import com.example.yeye.sms.myMethod.ShowContactsList;
import com.example.yeye.sms.myMethod.ShowSMSList;

public class ContactsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        ScrollView sv = new ScrollView(this);

        if (ShowSMSList.getSmsInPhone(this).equals("")) {
            setContentView(R.layout.activity_contacts_list);
        } else {
            tv.setText(ShowContactsList.getNumberInPhone(this));
            tv.setTextSize(20);
            sv.addView(tv);
            setContentView(sv);
        }

    }

    public static void actionStart(Context context) {
        Intent i = new Intent(context, ContactsListActivity.class);
        context.startActivity(i);
    }
}
