package com.HashTagApps.WATool.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.otherAppsNotificationAdapter;
import com.HashTagApps.WATool.helperclass.DatabaseHelper;
import com.HashTagApps.WATool.model.AllNotificationItem;
import com.HashTagApps.WATool.model.AllNotificationItem2;
import com.HashTagApps.WATool.model.tempItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class otherAppNotification extends AppCompatActivity {

    public DatabaseHelper db;
    public CompositeDisposable disposables = new CompositeDisposable();
    public ArrayList<AllNotificationItem2> tempitems = new ArrayList<>();
    public boolean isSelected=false;
    otherAppsNotificationAdapter otherAppsNotificationAdapter;
    RecyclerView recyclerView;
    ArrayList<AllNotificationItem2> items;
    ArrayList<tempItem> appItems;
    public MenuItem itemSelect,itemDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_app_notification);
        db = DatabaseHelper.getDatabase(this);
        recyclerView =findViewById(R.id.recycler);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("Other App Notifications");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("MyObject", "");
        Type type = new TypeToken<List<tempItem>>(){}.getType();

        appItems = gson.fromJson(json,type);


        getNotificatons();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_notification_menu, menu);
        itemSelect = menu.findItem(R.id.select).setVisible(true);
        itemDelete = menu.findItem(R.id.delete).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }else if (item.getItemId() == R.id.select){
            isSelected = true;
            itemDelete.setVisible(true);
            itemSelect.setVisible(false);
            tempitems.clear();
        }else if (item.getItemId() == R.id.delete){
            for (AllNotificationItem2 fileItem: tempitems) {
                items.remove(fileItem);
                otherAppsNotificationAdapter.notifyDataSetChanged();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    public void getNotificatons(){

        disposables.add(db.userDao().getNotifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AllNotificationItem>>() {
                    @Override
                    public void accept(List<AllNotificationItem> allNotificationItems) throws Exception {
                        items = new ArrayList<>();
                        for (AllNotificationItem allNotificationItem: allNotificationItems) {
                            items.add(new AllNotificationItem2(allNotificationItem));
                        }
                        Log.e("qqqqqqqqqq","qqqqqqqq"+items.size());
                        Collections.reverse(items);
                        for (int i = 0; i < items.size(); i++) {

                            for (int j = 0; j < appItems.size(); j++) {

                                if (appItems.get(j).getAppName().equals(items.get(i).getPackageName())) {
                                    otherAppsNotificationAdapter = new otherAppsNotificationAdapter(items, otherAppNotification.this);
                                    RecyclerView recyclerView = otherAppNotification.this.findViewById(R.id.recycler);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(otherAppNotification.this));
                                    recyclerView.setAdapter(otherAppsNotificationAdapter);
                                }
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }
}
