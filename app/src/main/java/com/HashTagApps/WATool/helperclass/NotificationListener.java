package com.HashTagApps.WATool.helperclass;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.DeletedMessageActivity;
import com.HashTagApps.WATool.model.AllNotificationItem;
import com.HashTagApps.WATool.model.MessageFeed;
import com.HashTagApps.WATool.model.NewMessageItem;
import com.HashTagApps.WATool.model.ReplyMessageDataItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationListener extends NotificationListenerService {

    SharedPreferences sharedpreferences;
    DatabaseHelper db;
    String notification_text;
    String package_title;
    String mainKey;

    static Notification notification;
    static Bundle bundle;
    static ArrayList<RemoteInput> remoteInputs;
    String package_title2;
    String notification_text2;


    String readMessage = "";

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {

        String pack = sbn.getPackageName();

        String title="";
        String text="";
        String currentDateTimeString = "";

        try {
            Bundle extras = sbn.getNotification().extras;
            title = extras.getString("android.title");
            text = extras.getCharSequence("android.text").toString();
            currentDateTimeString = getDateAndTime();
        }catch (Exception e){
            Log.e("akimakimakim","akimakim"+e.getMessage());
        }




        String PackageName = sbn.getPackageName();
        String package_title = sbn.getNotification().extras.getString("android.title");
        String notification_text = sbn.getNotification().extras.getString("android.text");

        db = DatabaseHelper.getDatabase(this);

        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (PackageName.equals("com.whatsapp")) {

            if (package_title != null && !package_title.equals("WhatsApp")) {


                if (notification_text != null && !notification_text.contains("new messages")) {

//


                    if (notification_text.equals("This message was deleted")) {

                        addNotification(package_title);

                        final CompositeDisposable disposables = new CompositeDisposable();

                        String[] separated = sbn.getKey().split("\\|");
                        mainKey = separated[3];

                        disposables.add(db.userDao().getMessage(mainKey)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(newMessageItems -> {

                                    if (newMessageItems.size() > 0) {
                                        NewMessageItem newMessageItem = newMessageItems.get(newMessageItems.size() - 1);
                                        newMessageItem.setIsDeleted("true");
                                        new DeleteBackTask(newMessageItem).execute();
                                    }
                                    if (!disposables.isDisposed()) {
                                        disposables.dispose();
                                    }
                                }))
                        ;

                    }

                    if(!notification_text.equals("This message was deleted")) {

                        editor.putString("Package Name", PackageName);
                        editor.putString("Package Title", package_title);
                        editor.putString("Notification Text", notification_text);
                        editor.apply();
                        this.package_title = package_title;
                        this.notification_text = notification_text;

                        long postTime = sbn.getPostTime();
                        String[] separated = sbn.getKey().split("\\|");
                        mainKey = separated[3];

                        int lastIndex = Objects.requireNonNull(package_title).lastIndexOf(":");
                        String subTitle = "";
                        if (lastIndex >= 0) {
                            String s1 = package_title.substring(0, lastIndex);
                            subTitle = package_title.substring(lastIndex + 1);
                            package_title = s1;
                        }

                        NewMessageItem newMessageItem = new NewMessageItem(package_title, subTitle, notification_text, postTime, "false", mainKey);

                        new BackTask(newMessageItem).execute();

                    }
                }
            }
        }else {
            switch (pack) {
                case "com.instagram.android": {
                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Instagram");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();

                    break;
                }
                case "org.telegram.messenger": {
                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Telegram");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();
                    break;
                }
                case "com.hike.chat.stickers": {
                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Hike");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();
                    break;
                }
                case "com.viber.voip": {
                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Viber");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();
                    break;
                }
                case "com.facebook.katana": {
                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Facebook");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();
                    break;
                }
                case "jp.naver.line.android": {
                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Line");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();
                    break;
                }
                case "com.imo.android.imoim": {
                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Imo");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();
                    break;
                }
                case "com.facebook.orca": {
                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Messenger");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();
                    break;
                }
                case "in.mohalla.sharechat": {
                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Sharechat");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();
                    break;
                }
                case "com.twitter.android": {

                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Twitter");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();

                    break;
                }
                case "com.linkdin.android": {

                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Linkdin");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();

                    break;
                }
                case "com.snapchat.android": {

                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("SnapChat");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();

                    break;
                }
                case "com.tumblr": {

                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Tumblr");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();

                    break;
                }
                case "com.tinder": {

                    AllNotificationItem allNotificationItem = new AllNotificationItem();
                    allNotificationItem.setPackageTitle(title);
                    allNotificationItem.setPackageName("Tinder");
                    allNotificationItem.setNotificationText(text);
                    allNotificationItem.setNotificationDate(currentDateTimeString);

                    new BackTastNotification(allNotificationItem).execute();

                    break;
                }
            }
        }

        if (!sbn.getKey().contains("null")) {

            package_title2 = sbn.getNotification().extras.getString("android.title");
            notification_text2 = sbn.getNotification().extras.getString("android.text");

            db = DatabaseHelper.getDatabase(this);

            if (Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners").contains(getPackageName()) && !sbn.isOngoing()
                    && sbn.getPackageName().equals("com.whatsapp")) {
                new Thread(() -> {
                    try {
                        notification = sbn.getNotification();//Latest Notification of WhatsApp
                        if (notification != null) {
                            bundle = notification.extras;
                            remoteInputs = getRemoteInputs(notification);
                            if (remoteInputs != null && remoteInputs.size() > 0) {
                                Object title1 = bundle.get("android.title");//Chat Title
                                Object text1 = bundle.get("android.text");//Chat Text

                                if (title1 != null && text1 != null) {
                                    notification = sbn.getNotification();
                                    readMessage = text1.toString();
                                    new BackTaskReply().execute();
                                }
                            }
                        }
                    } catch (Exception e){
                        notification = null;
                        Log.e("tag","==  = = =  error = = = = " + e.getMessage());
                    }
                }).start();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification(String package_title) {

        Intent intent = new Intent(this, DeletedMessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("title", package_title);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "100")
                .setSmallIcon(R.drawable.direct_send)
                .setContentTitle("New Message Deleted")
                .setContentText(package_title + " has Deleted a Message(s)")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.DEFAULT_ALL);

        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(100, builder.build());

    }

    private String getDateAndTime() {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return df.format(c.getTime());
    }
    private ArrayList<RemoteInput> getRemoteInputs(Notification notification) {
        ArrayList<RemoteInput> remoteInputs = new ArrayList<>();
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender(notification);
        for (NotificationCompat.Action act : wearableExtender.getActions()) {
            if (act != null && act.getRemoteInputs() != null) {
                remoteInputs.addAll(Arrays.asList(act.getRemoteInputs()));
            }
        }
        return remoteInputs;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendMsg(Notification notification, String msg) {
        RemoteInput[] allRemoteInputs = new RemoteInput[remoteInputs.size()];
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Iterator it = remoteInputs.iterator();
        int i = 0;
        while (it.hasNext()) {
            allRemoteInputs[i] = (RemoteInput) it.next();
            bundle.putCharSequence(allRemoteInputs[i].getResultKey(), msg);//This work, apart from Hangouts as probably they need additional parameter (notification_tag?)
            i++;
        }
        RemoteInput.addResultsToIntent(allRemoteInputs, localIntent, bundle);
        try {
            notifyMessageSend();
            ((Objects.requireNonNull(replyAction(notification)))).actionIntent.send(this, 0, localIntent);

        } catch (PendingIntent.CanceledException e) {
            Log.e("tag", "replyToLastNotification error: " + e.getLocalizedMessage());
        }
    }

    private void notifyMessageSend() {

        Intent intent = new Intent(this, getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "100")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Auto reply")
                .setContentText("Your auto reply message was sent.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.DEFAULT_ALL);

        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(100, builder.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private NotificationCompat.Action replyAction(Notification notification) {
        NotificationCompat.Action action;
        for (NotificationCompat.Action action2 : new NotificationCompat.WearableExtender(notification).getActions()) {
            if (isAllowFreeFormInput(action2)) {

                return action2;
            }
        }
        if (!(notification.actions == null)) {
            for (int i = 0; i < NotificationCompat.getActionCount(notification); i++) {
                action = NotificationCompat.getAction(notification, i);
                if (isAllowFreeFormInput(action)) {
                    return action;
                }
            }
        }
        return null;
    }

    private boolean isAllowFreeFormInput(NotificationCompat.Action action) {
        if (action.getRemoteInputs() == null) {
            return false;
        }
        for (RemoteInput allowFreeFormInput : action.getRemoteInputs()) {
            if (allowFreeFormInput.getAllowFreeFormInput()) {
                return true;
            }
        }
        return false;
    }


    @SuppressLint("StaticFieldLeak")
    public class BackTastNotification extends AsyncTask<Void, Void, Void>{

        AllNotificationItem allNotificationItem;

        BackTastNotification(AllNotificationItem allNotificationItem) {
            this.allNotificationItem = allNotificationItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.userDao().insertNewNotification(allNotificationItem);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class BackTaskReply extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            for (ReplyMessageDataItem replyMessageDataItem: db.userDao().getAllReplyMessages()) {
                if (replyMessageDataItem.getReadMessage().equals(readMessage.toLowerCase())) {
                    if (replyMessageDataItem.getReplyEnable() == 1) {
                        String[] ignore = replyMessageDataItem.getIgnoreContact().split(",");
                        if (!Arrays.toString(ignore).contains(package_title2)) {
                            if (replyMessageDataItem.getSpecificContact().equals("All")) {
                                sendMsg(notification, db.userDao().getReplyMessage(replyMessageDataItem.getReadMessage()));
                            } else {
                                String[] name = replyMessageDataItem.getSpecificContact().split(",");
                                if (Arrays.toString(name).contains(package_title2)) {
                                    sendMsg(notification, db.userDao().getReplyMessage(replyMessageDataItem.getReadMessage()));
                                }
                            }
                        }
                        break;
                    }
                }
            }

            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class DeleteBackTask extends  AsyncTask<Void, Void, Void> {

        NewMessageItem newMessageItem;

        DeleteBackTask(NewMessageItem newMessageItem) {
            this.newMessageItem = newMessageItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.userDao().insertNewMessageData(newMessageItem);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class BackTask extends AsyncTask<Void, Void, Void> {

        NewMessageItem newMessageItem;

        BackTask(NewMessageItem newMessageItem) {
            this.newMessageItem = newMessageItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String lastMessage = new SharedPrefUtil(NotificationListener.this).getString(mainKey);

            if (lastMessage != null) {
                if (!lastMessage.equals(newMessageItem.getMessage())) {
                    db.userDao().insertNewMessageData(newMessageItem);

                    MessageFeed messageFeed = new MessageFeed(newMessageItem.getMainKey(), newMessageItem.getUserTitle(), newMessageItem.getMessage());

                    db.userDao().insertMessageFeed(messageFeed);

                    new SharedPrefUtil(NotificationListener.this).saveString(mainKey, notification_text);
                }
            } else {
                db.userDao().insertNewMessageData(newMessageItem);

                MessageFeed messageFeed = new MessageFeed(newMessageItem.getMainKey(), newMessageItem.getUserTitle(), newMessageItem.getMessage());

                db.userDao().insertMessageFeed(messageFeed);

                new SharedPrefUtil(NotificationListener.this).saveString(mainKey, notification_text);
            }


            SharedPreferences sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Package Name", "");
            editor.putString("Package Title", "");
            editor.putString("Notification Text", "");
            editor.apply();

            return null;
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
