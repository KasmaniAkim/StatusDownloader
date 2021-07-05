package com.HashTagApps.WATool.helperclass;


import android.content.Context;

import com.HashTagApps.WATool.model.AllNotificationItem;
import com.HashTagApps.WATool.model.HistoryFileData;
import com.HashTagApps.WATool.model.MessageFeed;
import com.HashTagApps.WATool.model.NewMessageItem;
import com.HashTagApps.WATool.model.ReplyMessageDataItem;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { NewMessageItem.class, MessageFeed.class, ReplyMessageDataItem.class, HistoryFileData.class, AllNotificationItem.class}, version = 4)
public abstract class DatabaseHelper extends RoomDatabase {

    public abstract UserDao userDao();

    private static volatile DatabaseHelper INSTANCE;

    public static DatabaseHelper getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseHelper.class, "new_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

