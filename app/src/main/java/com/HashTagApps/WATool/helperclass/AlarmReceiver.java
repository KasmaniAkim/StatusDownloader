package com.HashTagApps.WATool.helperclass;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.HashTagApps.WATool.activity.Sample;

public  class AlarmReceiver extends BroadcastReceiver {

    @SuppressLint("WrongConstant")
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, Sample.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        context.startActivity(i);
    }
}
