package com.HashTagApps.WATool;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.HashTagApps.WATool.activity.AboutActivity;
import com.HashTagApps.WATool.activity.IntroActivity;
import com.HashTagApps.WATool.activity.RSSFeedActivity;
import com.HashTagApps.WATool.adapter.MainAdapter;
import com.HashTagApps.WATool.model.MainItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int STORAGE_PERMISSION_CODE=1;
    ArrayList<MainItem> mainItems;
    MainAdapter mainAdapter;
    RecyclerView mainRecyclerView;
    AdView mAdView;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, IntroActivity.class); // Call the AppIntro java class
            startActivity(intent);
        }


//        startService(new Intent(this, FileObserverService.class));

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        createNotificationChannel();

        mainRecyclerView = findViewById(R.id.main_recycler_view);

        mainItems = new ArrayList<>();

        mainItems.add(new MainItem("Status Saver",
                "Save Image, Video Status From Stories!",
                R.drawable.status_saver, ""));
        mainItems.add(new MainItem("Direct Chat",
                "Send Message Without Saving Unknown Numbers!",
                R.drawable.direct_send, ""));
        mainItems.add(new MainItem("Deleted Message",
                "See Deleted Messages",
                R.drawable.deleted, ""));
        mainItems.add(new MainItem("Scheduler",
                "Schedule Message To send Later",
                R.drawable.scheduled, ""));
        mainItems.add(new MainItem("WhatsApp Gallery",
                "View All Media & Documents At One Place",
                R.drawable.gallary, ""));
        mainItems.add(new MainItem("Backup and Read Chat",
                "Read Backed Up.txt Files in Chat Format",
                R.drawable.backup, ""));
        mainItems.add(new MainItem("Auto Reply",
                "Set Auto Reply",
                R.drawable.autoreplay, ""));
        mainItems.add(new MainItem("Gallery Cleaner",
                "Delete Unnecessary Files & Save Space",
                R.drawable.cleaner, ""));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS},
                        2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, 2);

                }

            }

        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                }
            }
        }

        mainAdapter = new MainAdapter(mainItems,this);
        mainRecyclerView.setLayoutManager((new GridLayoutManager(this,2)));
        mainRecyclerView.setAdapter(mainAdapter);


        if (!isNotificationServiceEnabled()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Allow");
            alertDialog.setMessage("Please allow WA Tools to read notifications inorder to read deleted messages");
            alertDialog.setPositiveButton("sure", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));

                }
            });
            alertDialog.show();

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_us) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (id == R.id.contact) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hashtagappsnetwork.wordpress.com/contact-2/"));
            startActivity(browserIntent);

        }else if(id==R.id.privacy){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hashtagappsnetwork.wordpress.com/privacy-policy-eu-gdpr-policy/"));
            startActivity(browserIntent);
        }else if(id==R.id.offers){
            Intent intent = new Intent(MainActivity.this, RSSFeedActivity.class);
            startActivity(intent);
        }else if(id==R.id.rate){

            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }

        }else if(id==R.id.share){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "WA Tool");
            String shareMessage= "\nDownload Multi Utility App for WhatsApp with following features:\n\n" +
                    "-Status Saver\n\n" +
                    "-View Deleted Message\n\n"+
                    "-Auto Reply\n\n" +
                    "-Schedule Message\n\n" +
                    "-Direct Chat\n\n" +
                    "-Read in Chat Format\n\n" +
                    "-Media Gallery\n\n" +
                    "-Gallery Cleaner\n\n" +
                    "-Download here:\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.HashTagApps.WATool\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));

        }else if(id==R.id.terms){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hashtagappsnetwork.wordpress.com/disclaimer-and-terms-of-use/"));
            startActivity(browserIntent);


        }else if(id==R.id.auto_reply){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.HashTagApps.WAAutoReply"));
            startActivity(browserIntent);
        }else if (id==R.id.direct_chat){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.HashTagApps.WADirectChat"));
            startActivity(browserIntent);
        }else if (id==R.id.deleted_message){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.HashTagApps.WAWhatsDelete"));
            startActivity(browserIntent);
        }else if (id==R.id.scheduler){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.HashTagApps.WAScheduleMessage"));
            startActivity(browserIntent);
        }else if (id==R.id.gallery){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.HashTagApps.WAGallery"));
            startActivity(browserIntent);
        }else if (id==R.id.backup_read){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.HashTagApps.WATextToChat"));
            startActivity(browserIntent);
        }else if (id==R.id.cleaner){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.HashTagApps.WAGalleryCleaner"));
            startActivity(browserIntent);
        }else if (id==R.id.status_saver){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.HashTagApps.WAStatusSaver"));
            startActivity(browserIntent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mainAdapter = new MainAdapter(mainItems, this);
                mainRecyclerView.setLayoutManager((new GridLayoutManager(this,2)));
                mainRecyclerView.setAdapter(mainAdapter);
            }

        }

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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("100", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
