package com.HashTagApps.WATool.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppItem {

    private Drawable appIcon;
    private String appName;
    private Boolean isChecked = false;

    public AppItem(Drawable appIcon, String appName) {
        this.appIcon = appIcon;
        this.appName = appName;
    }


    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

}
