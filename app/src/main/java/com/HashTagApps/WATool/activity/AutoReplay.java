package com.HashTagApps.WATool.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.AutoReplyAdapter;
import com.HashTagApps.WATool.helperclass.DatabaseHelper;
import com.HashTagApps.WATool.model.ReplyMessageDataItem;
import com.HashTagApps.WATool.model.ReplyMessageItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class AutoReplay extends AppCompatActivity {

    public CompositeDisposable disposables = new CompositeDisposable();
    private RecyclerView recyclerView;
    public DatabaseHelper db;
    private AdView mAdView;
    FloatingActionButton floatingActionButton;

    public boolean enableMessage = false;
    public String message = "";
    RelativeLayout NotificationToast;
    TextView enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_replay);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("Auto Reply");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        NotificationToast = findViewById(R.id.permissionToast);
        floatingActionButton = findViewById(R.id.add_button);

        if (!isNotificationServiceEnabled()){

            NotificationToast.setVisibility(View.VISIBLE);
            enable = findViewById(R.id.enable);
            enable.setOnClickListener(view -> {
                startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
            });
            floatingActionButton.setVisibility(View.GONE);
        }else{
            NotificationToast.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        if (!isNotificationServiceEnabled()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Allow");
            alertDialog.setMessage("Please allow WA Tools to read notifications inorder to read deleted messages");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                alertDialog.setPositiveButton("sure", (dialog, which) -> startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)));
            }
            alertDialog.show();

        }

        recyclerView = findViewById(R.id.reply_recycler_view);
        db = DatabaseHelper.getDatabase(this);

        disposables.add(db.userDao().getReplyMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(replyMessageDataItems -> {

                    ArrayList<ReplyMessageItem> replyMessageItems = new ArrayList<>();

                    for (ReplyMessageDataItem replyMessageDataItem: replyMessageDataItems) {


                        if (replyMessageDataItem.getReplyEnable()==1){
                            notifyy();
                        }else {
                            dismissNotifyy();
                        }
                        replyMessageItems.add(new ReplyMessageItem(replyMessageDataItem.getReadMessage(),
                                replyMessageDataItem.getReplyMessage(), replyMessageDataItem.getReplyEnable()));
                    }


                    AutoReplyAdapter autoReplyAdapter = new AutoReplyAdapter(replyMessageItems, AutoReplay.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AutoReplay.this));
                    recyclerView.setAdapter(autoReplyAdapter);
                }, throwable -> {

                }));

        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(AutoReplay.this,AddReplyActivity.class);
            startActivity(intent);
        });

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
    public void dismissNotifyy(){
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(100);
    }
    public void notifyy(){

        Intent intent = new Intent(this, AutoReplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "100")
                .setSmallIcon(R.drawable.direct_send)
                .setContentTitle("Auto Reply On")
                .setContentText("Auto Reply by WA Tools is Active")
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.DEFAULT_ALL);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(100, builder.build());
    }

    public void enableMessage(boolean b, String readMessage) {
        enableMessage = b;
        message = readMessage;
        new BackTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNotificationServiceEnabled()){

            NotificationToast.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.auto_reply, menu);
        return true;
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }else if (id==R.id.how_to_use){
            Intent intent =new Intent(AutoReplay.this,HowToUseActivity3.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    public class BackTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            if (enableMessage) {
                db.userDao().enableReply(1, message);
            } else {
                db.userDao().enableReply(0, message);
            }

            return null;
        }
    }

}
