package com.example.yeye.Easy_Backup.entity;

/**
 * Created by yeye on 2016/1/16.
 */
public class User {
    private int userId;
    private String account;
    private String password;

    public User(int userId, String account, String password) {
        this.userId = userId;
        this.account = account;
        this.password = password;
    }
     public User(){

     }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
