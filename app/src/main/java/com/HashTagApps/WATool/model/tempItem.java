package com.HashTagApps.WATool.model;

import android.os.Parcel;
import android.os.Parcelable;

public class tempItem implements Parcelable{
    String AppName;
    Boolean isChecked;

    public tempItem(Parcel in) {
        AppName = in.readString();
        byte tmpIsChecked = in.readByte();
        isChecked = tmpIsChecked == 0 ? null : tmpIsChecked == 1;
    }

    public static final Creator<tempItem> CREATOR = new Creator<tempItem>() {
        @Override
        public tempItem createFromParcel(Parcel in) {
            return new tempItem(in);
        }

        @Override
        public tempItem[] newArray(int size) {
            return new tempItem[size];
        }
    };

    public tempItem() {

    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(AppName);
        parcel.writeByte((byte) (isChecked == null ? 0 : isChecked ? 1 : 2));
    }
}
