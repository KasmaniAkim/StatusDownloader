package com.HashTagApps.WATool.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.ChatAdapter;
import com.HashTagApps.WATool.helperclass.DatabaseHelper;
import com.HashTagApps.WATool.model.MainChatItem;
import com.HashTagApps.WATool.model.MessageFeed;
import com.HashTagApps.WATool.model.NewMessageItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity {

    String title;
    String mainKey;
    CompositeDisposable disposables = new CompositeDisposable();
    DatabaseHelper databaseHelper;
    private AdView mAdView;

    ChatAdapter chatAdapter;
    RecyclerView recyclerView;

    public MenuItem itemSelect,itemDelete;
    public boolean isSelect = false;
    public ArrayList<MainChatItem> mainChatItemArrayList;
    ArrayList<MainChatItem> mainChatItems;
    ArrayList<MainChatItem> chatItems;

    Menu menu;
    MaterialSearchView searchView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mainChatItemArrayList = new ArrayList<>();
        mainChatItems = new ArrayList<>();
        chatItems = new ArrayList<>();

        Toast.makeText(this, "Deleted Message is Highlighted", Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.inflateMenu(R.menu.chat_menu);


        recyclerView = findViewById(R.id.chat_recycler_view);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        mainKey = intent.getStringExtra("main_key");
        databaseHelper = DatabaseHelper.getDatabase(this);
        setTitle(title);
        disposables.add(databaseHelper.userDao().getMessage(mainKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageDataItems -> {
                    this.mainChatItems.clear();
                    ArrayList<MainChatItem>  mainChatItems = new ArrayList<>();

                    for (NewMessageItem newMessageItem: messageDataItems) {
                        mainChatItems.add(new MainChatItem(newMessageItem));
                        this.mainChatItems.add(new MainChatItem(newMessageItem));
                    }

                    chatAdapter = new ChatAdapter( mainChatItems, ChatActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                    recyclerView.setAdapter(chatAdapter);
                    recyclerView.scrollToPosition(mainChatItems.size() - 1);


                }, throwable -> {

                }));

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                chatItems = mainChatItems;
                Log.e("tag",chatItems.size() + " = = size = ==  " + mainChatItems.size());
                ArrayList<MainChatItem> temp = new ArrayList<>();
                for(MainChatItem d: chatItems){
                    if(d.getNewMessageItem().getMessage().toLowerCase().contains(newText)){
                        temp.add(d);
                    }
                }
                chatAdapter.addMessageFeed(temp);
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
                Log.e("tag"," = =  = ==  " + mainChatItems.size());
                chatAdapter.addMessageFeed(mainChatItems);
            }
        });

        searchView.setVoiceSearch(false);

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.chat_menu_main, menu);
        itemSelect = menu.findItem(R.id.select).setVisible(true);
        itemDelete = menu.findItem(R.id.multiple_delete).setVisible(false);
        this.menu = menu;
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.select) {

            isSelect = true;
            itemDelete.setVisible(true);
            itemSelect.setVisible(false);
            chatAdapter.setChecked(true);

        } else if (id == R.id.multiple_delete) {
            confirmDelete();
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDelete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Confirm to Delete Messages");
        builder.setPositiveButton("YES", (dialogInterface, i) -> {
            new DeleteBackTask().execute();
            isSelect = false;
            itemDelete.setVisible(false);
            itemSelect.setVisible(true);
            chatAdapter.setChecked(false);
        });

        builder.setNegativeButton("NO", null);
        builder.show();
    }

    @SuppressLint("StaticFieldLeak")
    public class DeleteBackTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            for (MainChatItem mainChatItem: mainChatItemArrayList) {
                databaseHelper.userDao().deleteMessages(mainChatItem.getNewMessageItem().getId());
            }

            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if (isSelect) {
            isSelect = false;
            itemDelete.setVisible(false);
            itemSelect.setVisible(true);
            chatAdapter.setChecked(false);
        } else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
