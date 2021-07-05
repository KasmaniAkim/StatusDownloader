package com.HashTagApps.WATool.activity;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.WhatsappAccessibilityService;
import com.HashTagApps.WATool.fragment.NewScheduledFragment;
import com.HashTagApps.WATool.fragment.ScheduledFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ScheduleMessageActivity extends AppCompatActivity  {

    public ViewPager viewPager;
    TabLayout tabLayout;
    NewScheduledFragment newScheduledFragment;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_message);

        SharedPreferences sp = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, IntroActivity.class); // Call the AppIntro java class
            startActivity(intent);
        }

        setTitle("Schedule Message");
        FileAdapter fileAdapter = new FileAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.view_pager);
        newScheduledFragment = new NewScheduledFragment();
        fileAdapter.addFragment(newScheduledFragment);
        fileAdapter.addFragment(new ScheduledFragment());
        viewPager.setAdapter(fileAdapter);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        tabLayout =  findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        createNotificationChannel();



        if (!isAccessibilityOn(ScheduleMessageActivity.this)){

            AlertDialog.Builder builder1 = new AlertDialog.Builder(ScheduleMessageActivity.this);
            builder1.setMessage("In order to send Scheduled Message for you, WA Schedule Message App need to be granted permission in accessibility setting");
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Sure",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent (Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            ScheduleMessageActivity.this.startActivity (intent);
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        int STORAGE_PERMISSION_CODE = 1;
        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mainAdapter = new MainAdapter(mainItems,this);
//                mainRecyclerView.setLayoutManager((new GridLayoutManager(this,2)));
//                mainRecyclerView.setAdapter(mainAdapter);
            }

        }

    }

    private boolean isAccessibilityOn(Context context) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName () + "/" + WhatsappAccessibilityService.class.getCanonicalName ();
        try {
            accessibilityEnabled = Settings.Secure.getInt (context.getApplicationContext ().getContentResolver (), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {  }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter (':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString (context.getApplicationContext ().getContentResolver (), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString (settingValue);
                while (colonSplitter.hasNext ()) {
                    String accessibilityService = colonSplitter.next ();

                    if (accessibilityService.equalsIgnoreCase (service)) {
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


    public class FileAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        FileAdapter(FragmentManager fm) {
            //noinspection deprecation
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "Schedule New Message";
            } else {
                return "Scheduled Message(s)";
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    Uri contactData = data.getData();
                    //noinspection deprecation
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {

                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();

                            newScheduledFragment.phone.setText("");
                            newScheduledFragment.phone.setText(phones.getString(phones.getColumnIndex("data1")));
                            Log.e("tag number", "  = =  = = =  " + phones.getString(phones.getColumnIndex("data1")));
                        }
                    }
                }
            }
        }
    }

    public void notifyy(){

        Intent intent = new Intent(this, ScheduleMessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "100")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Schedule Message")
                .setContentText("One or more message is scheduled")
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.DEFAULT_ALL);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(100, builder.build());
    }
    public void dismissNotifyy(){
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.schedule_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.how_to_use){
            Intent intent =new Intent(ScheduleMessageActivity.this, HowToUse5.class);
            startActivity(intent);
        }else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

