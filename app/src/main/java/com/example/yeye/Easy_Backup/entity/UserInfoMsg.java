package com.example.yeye.Easy_Backup.entity;

/**
 * Created by yeye on 2016/1/18.
 */
public class UserInfoMsg {
    boolean result;
    User user;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
