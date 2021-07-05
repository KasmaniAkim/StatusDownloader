package com.HashTagApps.WATool.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "new_messages")
public class NewMessageItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "user_title")
    private String userTitle;

    @ColumnInfo(name = "sub_title")
    private String subTitle;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "is_deleted")
    private String isDeleted;

    @ColumnInfo(name = "message_time")
    private long messageTime;

    @ColumnInfo(name = "main_key")
    private String mainKey;

    public NewMessageItem(String userTitle, String subTitle,
                          String message, long messageTime,
                          String isDeleted, String mainKey) {
        this.userTitle = userTitle;
        this.subTitle = subTitle;
        this.message = message;
        this.messageTime = messageTime;
        this.isDeleted = isDeleted;
        this.mainKey = mainKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getMessage() {
        return message;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getMainKey() {
        return mainKey;
    }
}
