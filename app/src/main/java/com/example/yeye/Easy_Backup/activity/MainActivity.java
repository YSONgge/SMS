package com.example.yeye.Easy_Backup.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yeye.Easy_Backup.ActivityCollector.ActivityCollector;
import com.example.yeye.Easy_Backup.R;
import com.example.yeye.Easy_Backup.sqlite.MySQLiteOpenHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtAccount;
    private Button btnSMS,btnContacts,btnSmsList,btnContactsList;
    public int userId;
    private SQLiteDatabase db;
    private MySQLiteOpenHelper helper;

    private String dbName = "user";
    private int version = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        // TODO: 2016/1/20 userId 尚未设置
        userId=getIntent().getIntExtra("UserId",-1);

        helper = new MySQLiteOpenHelper(MainActivity.this, dbName, null, version);
        db = helper.getWritableDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        txtAccount = (TextView)headerView.findViewById(R.id.txt_account);
        txtAccount.setText(getUserAccount());
        //txtAccount.setText("dfsdsfds");

        btnSMS = (Button)findViewById(R.id.btn_main_sms_backup);
        btnContacts=(Button)findViewById(R.id.btn_main_contacts_backup);
        btnSmsList = (Button)findViewById(R.id.btn_main_sms_content_list);
        btnContactsList = (Button)findViewById(R.id.btn_main_contacts_content_list);

        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsActivity.actionStart(MainActivity.this,userId);
            }
        });
        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSActivity.actionStart(MainActivity.this,userId);
            }
        });
        btnContactsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsListActivity.actionStart(MainActivity.this);
            }
        });
        btnSmsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSListActivity.actionStart(MainActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            ActivityCollector.finishAll();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO: 2016/4/21 还没想好setting用来干啥
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
            ContactsActivity.actionStart(MainActivity.this,userId);
        } else if (id == R.id.nav_sms) {
            SMSActivity.actionStart(MainActivity.this,userId);
        } else if (id == R.id.nav_exit_user) {
                QuitUser();
            ActivityCollector.finishAll();
            LoginActivity.actionStart(MainActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void QuitUser(){
        String delete = "delete from user ";
        db.execSQL(delete);
    }

    public String getUserAccount(){
        String account = null;
        String sql = "select * from user";
        Cursor c = db.rawQuery(sql, null);

        while (c.moveToFirst()) {
             account = c.getString(c.getColumnIndex("account"));
            break;
        }
        c.close();

        return account ;
    }

    public static void actionStart(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }
    public static void actionStart(Context context,int userId) {
        Intent i = new Intent(context, LoginActivity.class);
        i.putExtra("UserId",userId);
        context.startActivity(i);
    }

}
