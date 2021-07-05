package com.HashTagApps.WATool.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.AppAdapter2;
import com.HashTagApps.WATool.model.AppItem;
import com.HashTagApps.WATool.model.tempItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity {

    public ArrayList<tempItem> appItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        RecyclerView recyclerView = findViewById(R.id.app_recycler_view);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        ArrayList<AppItem> appItems2 = new ArrayList<>();

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> applications = pm.getInstalledApplications(flags);
        for (ApplicationInfo appInfo : applications) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
                String appname = pm.getApplicationLabel(appInfo).toString();
                if (!appname.equals("")) {
                    if ("WhatsApp".equals(appname) || "Telegram".contains(appname)
                            || "Hike".equals(appname) || "Viber".equals(appname)
                            || "Instagram".equals(appname) || "Facebook".equals(appname)
                            || "Messenger".equals(appname) || "Imo".equals(appname)
                            || "Line".equals(appname) || "Sharechat".equals(appname)
                            || "Twitter".equals(appname) || "Linkedin".equals(appname)
                            || "Snapchat".equals(appname) || "Tumblr".equals(appname)
                            || "Tinder".equals(appname)) {

                        try {
                            appItems2.add(new AppItem(getPackageManager().getApplicationIcon(appInfo.packageName), appname));
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e("tag", " =  = error =  = =  = " + e.getMessage());

                        }
                    }
                }
            }
        }


        AppAdapter2 appAdapter = new AppAdapter2(appItems2,SettingActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(SettingActivity.this));
        recyclerView.setAdapter(appAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }else if (id == R.id.save) {
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(appItems);
            prefsEditor.putString("MyObject", json);
            prefsEditor.apply();
        }
        return super.onOptionsItemSelected(item);
    }



}