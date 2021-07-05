package com.HashTagApps.WATool.model;

public class MessageItem {

    private String packageName;
    private String title;
    private String messages;
    private String mainKey;

    public MessageItem(String packageName, String title, String messages, String mainKey) {

        this.packageName = packageName;
        this.title = title;
        this.messages = messages;
        this.mainKey = mainKey;

    }

    public String getPackageName() {
        return packageName;
    }

    public String getTitle() {
        return title;
    }

    public String getMessages() {
        return messages;
    }

    public String getMainKey() {
        return mainKey;
    }
}
