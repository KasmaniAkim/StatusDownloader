package com.HashTagApps.WATool.model;

import androidx.room.ColumnInfo;

public class AllNotificationItem2 {

    private int id;

    private String packageName;

    private String packageTitle;

    private String notificationText;

    private String notificationDate;
    private boolean isChecked = false;

    public AllNotificationItem2(int id, String packageName, String packageTitle, String notificationText, String notificationDate) {
        this.id = id;
        this.packageName = packageName;
        this.packageTitle = packageTitle;
        this.notificationText = notificationText;
        this.notificationDate = notificationDate;
    }

    public AllNotificationItem2(AllNotificationItem allNotificationItem) {
        this.id = allNotificationItem.getId();
        this.packageName = allNotificationItem.getPackageName();
        this.packageTitle = allNotificationItem.getPackageTitle();
        this.notificationText = allNotificationItem.getNotificationText();
        this.notificationDate = allNotificationItem.getNotificationDate();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
