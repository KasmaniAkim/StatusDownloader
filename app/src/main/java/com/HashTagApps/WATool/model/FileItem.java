package com.HashTagApps.WATool.model;

import java.io.File;

public class FileItem {

    private File file;
    private boolean isChecked;

    public FileItem(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
