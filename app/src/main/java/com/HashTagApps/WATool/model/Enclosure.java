package com.HashTagApps.WATool.model;

public class Enclosure {

    public String link;
    public String thumbnail;

    public Enclosure(String link, String thumbnail) {
        this.link = link;
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
