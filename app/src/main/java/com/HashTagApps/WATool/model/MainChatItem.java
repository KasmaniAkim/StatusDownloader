package com.HashTagApps.WATool.model;

public class MainChatItem {

    private NewMessageItem newMessageItem;
    private boolean isChecked = false;

    public MainChatItem(NewMessageItem newMessageItem) {
        this.newMessageItem = newMessageItem;
    }

    public NewMessageItem getNewMessageItem() {
        return newMessageItem;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
