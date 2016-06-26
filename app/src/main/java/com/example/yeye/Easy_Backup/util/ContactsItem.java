package com.example.yeye.Easy_Backup.util;

/**
 * Created by yeye on 2016/4/20.
 */
public class ContactsItem {
    String displayName;
    String number;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ContactsItem{" +
                "displayName='" + displayName + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
