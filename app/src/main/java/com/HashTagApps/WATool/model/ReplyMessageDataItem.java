package com.HashTagApps.WATool.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reply_messages")
public class ReplyMessageDataItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "read_message")
    private String readMessage;

    @ColumnInfo(name = "reply_message")
    private String replyMessage;

    @ColumnInfo(name = "contact")
    private String specificContact;

    @ColumnInfo(name = "ignore")
    private String ignoreContact;

    @ColumnInfo(name = "reply_enable")
    private int replyEnable;

    public ReplyMessageDataItem(String readMessage, String replyMessage, String specificContact, String ignoreContact,int replyEnable) {
        this.readMessage = readMessage;
        this.replyMessage = replyMessage;
        this.specificContact = specificContact;
        this.ignoreContact = ignoreContact;
        this.replyEnable = replyEnable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReadMessage() {
        return readMessage;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public String getSpecificContact() {
        return specificContact;
    }

    public String getIgnoreContact() {
        return ignoreContact;
    }

    public int getReplyEnable() {
        return replyEnable;
    }
}
