package com.HashTagApps.WATool.activity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.HashTagApps.WATool.R;


import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Sample extends AppCompatActivity {

    String phone;
    String message;
    SharedPreferences sharedPreferences;
    CountDownTimer counter;
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        final TextView hello = findViewById(R.id.hello);
        TextView dateTime = findViewById(R.id.date_time);
        TextView mobileNumber = findViewById(R.id.mobile_number);
        TextView messageTv =findViewById(R.id.message);
        Button cancel = findViewById(R.id.cancel);
        Button sendNow = findViewById(R.id.send_now);


        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa",
                Locale.ENGLISH);
        String var = dateFormat.format(date);

        dateTime.setText(var);
        sharedPreferences = getSharedPreferences("Details", 0);

        phone = sharedPreferences.getString("phone", "");
        message = sharedPreferences.getString("message", "");

        mobileNumber.setText(phone);
        messageTv.setText(message);

        counter = new CountDownTimer(10000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                hello.setText(millisUntilFinished / 1000 + "");
            }

            public void onFinish() {
                sendToWhatsApp();
                try {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

                    @SuppressLint("InvalidWakeLockTag")
                    PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                            PowerManager.FULL_WAKE_LOCK| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                    screenLock.acquire();
                    screenLock.release();
                    Sample.this.setTurnScreenOn(true);
                    final Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                }catch (Exception e){
                    Log.e("-----------","----------------"+e.getMessage());
                }
            }

        }.start();
        sendNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.cancel();
                sendToWhatsApp();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sample.this.finish();
                counter.cancel();

            }
        });
    }
    public void sendToWhatsApp(){
        PackageManager packageManager = getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (!phone.equals("")) {
            try {
                String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                    @SuppressLint("CommitPrefEdits")
                    SharedPreferences.Editor editor = sharedPreferences.edit();


                    editor.putString("phone","");
                    editor.putString("message","");
                    editor.putBoolean("enable", false);
                    editor.apply();
                    notifyMessageSend();
                    Sample.this.finish();
                }
            } catch (Exception e) {
                Log.e("tag", "== =  = = =" + e.getMessage());
            }
        }
    }

    private void notifyMessageSend() {

        Intent intent = new Intent(this, ScheduleMessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "100")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Scheduled Message")
                .setContentText("Your Schedule message was sent")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.DEFAULT_ALL);

        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(100, builder.build());
    }
}
