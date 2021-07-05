package com.HashTagApps.WATool.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.HashTagApps.WATool.fragment.Disclaimer;
import com.HashTagApps.WATool.fragment.MainFragment;
import com.HashTagApps.WATool.model.AppItem;
import com.HashTagApps.WATool.model.tempItem;
import com.github.paolorotolo.appintro.AppIntro;
import com.HashTagApps.WATool.MainActivity;
import com.HashTagApps.WATool.fragment.AutoReplayFragment;
import com.HashTagApps.WATool.fragment.ChatBackupReadFragment;
import com.HashTagApps.WATool.fragment.DeletedMessageFragment;
import com.HashTagApps.WATool.fragment.DirectChatFragment;
import com.HashTagApps.WATool.fragment.GalleryCleanerFragment;
import com.HashTagApps.WATool.fragment.NotificationReadFragment;
import com.HashTagApps.WATool.fragment.SchedulerFragment;
import com.HashTagApps.WATool.fragment.StatusSaverFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class IntroActivity extends AppIntro {

    public ArrayList<tempItem> appItems =  new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusSaverFragment statusSaverFragment = new StatusSaverFragment();
        DirectChatFragment directChatFragment = new DirectChatFragment();
        NotificationReadFragment notificationReadFragment = new NotificationReadFragment(IntroActivity.this);
        DeletedMessageFragment deletedMessageFragment = new DeletedMessageFragment();
        SchedulerFragment schedulerFragment = new SchedulerFragment();
        GalleryCleanerFragment galleryCleanerFragment = new GalleryCleanerFragment();
        ChatBackupReadFragment chatBackupReadFragment = new ChatBackupReadFragment();
        AutoReplayFragment autoReplay = new AutoReplayFragment();
        MainFragment mainFragment = new MainFragment();
        Disclaimer disclaimer = new Disclaimer();
        addSlide(mainFragment);
        addSlide(statusSaverFragment);
        addSlide(directChatFragment);
        addSlide(deletedMessageFragment);
        addSlide(schedulerFragment);
        addSlide(galleryCleanerFragment);
        addSlide(chatBackupReadFragment);
        addSlide(autoReplay);
        addSlide(notificationReadFragment);
        addSlide(disclaimer);
        setNextArrowColor(Color.parseColor("#673ab7"));
        setColorDoneText(Color.parseColor("#673ab7"));
        setIndicatorColor(Color.parseColor("#673ab7"),Color.parseColor("#DCDCDC"));
        setColorSkipButton(Color.parseColor("#673ab7"));


        setDoneText("Accept");
        skipButton.setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this,MainActivity.class);
            intent.putExtra("AppList",appItems);
            startActivity(intent);
        });

        doneButton.setOnClickListener(view -> {
            Log.e("12345678901","1234567890"+appItems.size());
            Intent intent = new Intent(IntroActivity.this,MainActivity.class);
            intent.putParcelableArrayListExtra("AppList",appItems);

            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(appItems);
            prefsEditor.putString("MyObject", json);
            prefsEditor.apply();



            startActivity(intent);
        });

    }
}
