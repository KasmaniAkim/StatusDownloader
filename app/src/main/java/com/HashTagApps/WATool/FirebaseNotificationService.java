package com.HashTagApps.WATool;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.HashTagApps.WATool.activity.RSSFeedActivity;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.core.app.NotificationCompat;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseNotificationService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(" = = =  = = ", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {

            String title = remoteMessage.getData().get("main_title");
            String message = remoteMessage.getData().get("message");

            if (title != null && title.equals("rssLink")) {

                Intent intent = new Intent(this, RSSFeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                buildNotification(message, intent);
            }

        }

    }

    private void buildNotification(String message, Intent intent) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"100")
                .setContentTitle(message)
                .setSmallIcon(R.drawable.icon)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {       // For Oreo and greater than it, we required Notification Channel.
            CharSequence name = "My New Channel";                   // The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("100",name, importance); //Create Notification Channel
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        Objects.requireNonNull(notificationManager).notify(100 /* ID of notification */, notificationBuilder.build());

    }

}
