package com.HashTagApps.WATool.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "history_file")
public class HistoryFileData {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "file_path")
    private String filePath;

    public HistoryFileData(@NotNull String filePath) {
        this.filePath = filePath;
    }

    @NotNull
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(@NotNull String filePath) {
        this.filePath = filePath;
    }
}
