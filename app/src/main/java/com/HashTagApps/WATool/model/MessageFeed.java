package com.HashTagApps.WATool.model;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_feed")
public class MessageFeed {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "main_key")
    private String mainKey;

    @ColumnInfo(name = "user_title")
    private String userTitle;

    @ColumnInfo(name = "message")
    private String message;

    public MessageFeed(@NotNull String mainKey, String userTitle, String message) {
        this.mainKey = mainKey;
        this.userTitle = userTitle;
        this.message = message;
    }

    @NotNull
    public String getMainKey() {
        return mainKey;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public String getMessage() {
        return message;
    }
}
