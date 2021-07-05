package com.HashTagApps.WATool.model;

public class MainItem {

    private String title;
    private String subTitle;
    private int imageFile;
    private String folderPath;

    public MainItem(String title, String subTitle, int imageFile, String folderPath) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageFile = imageFile;
        this.folderPath = folderPath;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getImageFile() {
        return imageFile;
    }

    public String getFolderPath() {
        return folderPath;
    }
}
