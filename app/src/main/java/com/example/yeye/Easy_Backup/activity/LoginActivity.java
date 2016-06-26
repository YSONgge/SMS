package com.example.yeye.Easy_Backup.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yeye.Easy_Backup.ActivityCollector.ActivityCollector;
import com.example.yeye.Easy_Backup.R;
import com.example.yeye.Easy_Backup.entity.UserInfoMsg;
import com.example.yeye.Easy_Backup.sqlite.MySQLiteOpenHelper;
import com.example.yeye.Easy_Backup.util.IConst;
import com.example.yeye.Easy_Backup.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.LOCATION_HARDWARE;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    private final String TAG = "LoginActivity";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private MySQLiteOpenHelper helper;
    private SQLiteDatabase db;
    SharedPreferences preferences;
    private String dbName = "user";
    private int version = 1;
    int userId;
    public RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityCollector.addActivity(LoginActivity.this);
        mQueue = Volley.newRequestQueue(LoginActivity.this);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.account);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {//editorInfo.ime_null-软键盘回车
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.actionStart(LoginActivity.this);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        helper = new MySQLiteOpenHelper(LoginActivity.this, dbName, null, version);
        db = helper.getWritableDatabase();
    }
       /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
       /* if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        } else {

            doLogin(email,password);
        }
    }

    public void doLogin(final String email, final String password){

        String url = IConst.SERVLET_ADDR + "Login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // TODO: 2016/1/20 登陆成功后跳转
            @Override
            public void onResponse(String s) {

                UserInfoMsg UserInfo = JSON.parseObject(s, UserInfoMsg.class);
                if (UserInfo.isResult()) {
                    userId = UserInfo.getUser().getUserId();
                    System.out.println("userId = " + userId);
                    //SaveData(email);
                    //saveSharedPreferencesUserId(userId);
                    ActivityCollector.finishAll();
                    MainActivity.actionStart(LoginActivity.this, userId);
                }else{
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                LogUtil.e(TAG, volleyError.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, R.string.http_fail, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("account", email);
                map.put("password", password);
                return map;
            }
        };
        mQueue.add(stringRequest);
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

/*    *//**
     * 保存数据
     *//*
    public void SaveData(String account) {
            String sql = "insert into user values(?,?)";
            db.execSQL(sql, new Object[]{account, mPasswordView.getText()});

    }*/

/*    private void saveSharedPreferencesUserId(int userId) {
        preferences = getSharedPreferences("userId"
                , MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", userId);
        // 提交修改
        *//*
         *apply()与commit()区别：  apply没有返回值而commit返回boolean表明修改是否提交成功 ，
         * apply是将修改数据原子提交到内存, 而后异步真正提交到硬件磁盘, 而commit是同步的提交到硬件磁盘
         * apply方法不会提示任何失败的提示。
         *//*
        editor.apply();
        //editor.commit();
    }*/

    public static void actionStart(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }
}

