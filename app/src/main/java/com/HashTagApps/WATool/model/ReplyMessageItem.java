package com.HashTagApps.WATool.model;

public class ReplyMessageItem {

    private int id;
    private String readMessage;
    private String replyMessage;
    private int enableMessage;

    public ReplyMessageItem(String readMessage, String replyMessage, int enableMessage) {
        this.readMessage = readMessage;
        this.replyMessage = replyMessage;
        this.enableMessage = enableMessage;
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

    public int getEnableMessage() {
        return enableMessage;
    }
}
