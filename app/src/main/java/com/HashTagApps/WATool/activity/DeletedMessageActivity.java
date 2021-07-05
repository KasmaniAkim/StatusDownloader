package com.HashTagApps.WATool.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.MessageAdapter;
import com.HashTagApps.WATool.helperclass.DatabaseHelper;
import com.HashTagApps.WATool.model.AppItem;
import com.HashTagApps.WATool.model.MessageFeed;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class DeletedMessageActivity extends AppCompatActivity {

    CompositeDisposable disposables = new CompositeDisposable();

    RecyclerView messageRecyclerView;
    ArrayList<MessageFeed> messageFeeds;
    ArrayList<MessageFeed> messageFeedArrayList;
    DatabaseHelper db;
    MessageAdapter messageAdapter;

    MaterialSearchView searchView;
    Toolbar toolbar;
    RelativeLayout NotificationToast;
    TextView enable;
//    ArrayList<AppItem> appItems;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_message);
        setTitle("Deleted Message");
        db = DatabaseHelper.getDatabase(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.inflateMenu(R.menu.chat_menu);


        messageRecyclerView = findViewById(R.id.recyclerView);

        messageFeeds = new ArrayList<>();
        messageFeedArrayList = new ArrayList<>();
        NotificationToast = findViewById(R.id.permissionToast);

        if (!isNotificationServiceEnabled()){

            NotificationToast.setVisibility(View.VISIBLE);
            enable = findViewById(R.id.enable);
            enable.setOnClickListener(view -> {
                startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
            });
        }else{
            NotificationToast.setVisibility(View.GONE);
        }
        if (!isNotificationServiceEnabled()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Allow");
            alertDialog.setMessage("Please allow MyApplication to read notifications inorder to read deleted messages");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                alertDialog.setPositiveButton("sure", (dialog, which) -> startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)));
            }
            alertDialog.show();

        }

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                messageFeedArrayList = messageFeeds;
                Log.e("tag", " = = = = =  " + messageFeeds.size());
                ArrayList<MessageFeed> temp = new ArrayList<>();
                for(MessageFeed d: messageFeedArrayList){
                    if(d.getUserTitle().toLowerCase().contains(newText)){
                        temp.add(d);
                    }
                }
                messageAdapter.addMessageFeed(temp);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                messageAdapter.addMessageFeed(messageFeeds);
            }
        });

        searchView.setVoiceSearch(false);

        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNotificationServiceEnabled()){

            NotificationToast.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }else if (id == R.id.how_to_use){
            Intent intent = new Intent(DeletedMessageActivity.this, HowToUseActivity2.class);
            startActivity(intent);
        } else if (id == R.id.disclaimer){
            Intent intent = new Intent(DeletedMessageActivity.this, Disclaimer.class);
            startActivity(intent);
        } else if (id == R.id.other){
            Intent intent = new Intent(DeletedMessageActivity.this, otherAppNotification.class);
//            intent.putParcelableArrayListExtra("Applist",appItems);
            startActivity(intent);
        }else if (id == R.id.settings){
            Intent intent = new Intent(DeletedMessageActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        disposables.add(db.userDao().getAllMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newMessageItems -> {

                    messageFeeds.clear();

                    for (MessageFeed newMessageItem: newMessageItems) {
                        if(!newMessageItem.getUserTitle().equals("")){
                            messageFeeds.add(newMessageItem);
                        }
                    }

                    Collections.reverse(newMessageItems);
                    messageAdapter = new MessageAdapter((ArrayList<MessageFeed>) newMessageItems,DeletedMessageActivity.this);
                    messageRecyclerView.setLayoutManager(new LinearLayoutManager(DeletedMessageActivity.this));
                    messageRecyclerView.setAdapter(messageAdapter);

                    disposables.dispose();

                }, throwable -> Log.e("tag","----=== size  = " + throwable.getMessage())));

    }

    public void confirmDelete(String key, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Confirm to Delete Chat");
        builder.setPositiveButton("YES", (dialogInterface, i) -> {
            new DeleteBackTask(key).execute();
            messageFeeds.remove(position);
            messageAdapter.messageFeeds.remove(position);
            messageAdapter.notifyItemRemoved(position);
        });

        builder.setNegativeButton("NO", null);
        builder.show();
    }

    @SuppressLint("StaticFieldLeak")
    public class DeleteBackTask extends AsyncTask<Void, Void, Void> {

        String key;

        DeleteBackTask(String key) {
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            db.userDao().deleteChat(key);
            db.userDao().deleteChatMessages(key);

            return null;
        }
    }


}
