package com.HashTagApps.WATool.helperclass;

import com.HashTagApps.WATool.model.AllNotificationItem;
import com.HashTagApps.WATool.model.HistoryFileData;
import com.HashTagApps.WATool.model.MessageFeed;
import com.HashTagApps.WATool.model.NewMessageItem;
import com.HashTagApps.WATool.model.ReplyMessageDataItem;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Flowable;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistoryData(HistoryFileData historyFileData);

    @Query("SELECT * FROM history_file")
    Flowable<List<HistoryFileData>> getHistoryFiles();

    @Query("SELECT * from message_feed ")
    Flowable<List<MessageFeed>> getAllMessages();

    @Query("Select * from new_messages where main_key = :mainKey")
    Flowable<List<NewMessageItem>> getMessage(String mainKey);

    @Insert
    void insertReplyMessage(ReplyMessageDataItem replyMessageDataItem);

    @Query("SELECT * FROM reply_messages WHERE read_message= :read_message")
    boolean checkMessageText(String read_message);

    @Query("SELECT * FROM reply_messages")
    List<ReplyMessageDataItem> getAllReplyMessages();

    @Query("SELECT reply_message FROM reply_messages WHERE read_message= :read_message")
    String getReplyMessage(String read_message);

    @Query("SELECT * FROM reply_messages")
    Flowable<List<ReplyMessageDataItem>> getReplyMessages();

    @Query("DELETE FROM reply_messages WHERE read_message = :readMessage")
    void deleteMessage(String readMessage);

    @Query("UPDATE reply_messages SET reply_enable = :replyEnable WHERE read_message = :readMessage")
    void enableReply(int replyEnable, String readMessage);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewMessageData(NewMessageItem newMessageItem);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewNotification(AllNotificationItem allNotificationItem);

    @Query("SELECT * FROM new_notification")
    Flowable<List<AllNotificationItem>> getNotifications();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessageFeed(MessageFeed messageFeed);

    @Query("DELETE FROM new_messages WHERE id = :id")
    void deleteMessages(int id);

    @Query("DELETE FROM new_messages WHERE main_key = :key")
    void deleteChatMessages(String key);

    @Query("DELETE FROM message_feed WHERE main_key = :key")
    void deleteChat(String key);

}

