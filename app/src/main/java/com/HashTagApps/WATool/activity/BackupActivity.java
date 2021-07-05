package com.HashTagApps.WATool.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.fragment.BackupFragment;
import com.HashTagApps.WATool.fragment.BackupHistoryFragment;
import com.HashTagApps.WATool.fragment.DefaultListFragment;
import com.HashTagApps.WATool.helperclass.DatabaseHelper;
import com.HashTagApps.WATool.uriconvert.PickiT;
import com.HashTagApps.WATool.uriconvert.PickiTCallbacks;
import com.HashTagApps.WATool.uriconvert.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class BackupActivity extends AppCompatActivity implements PickiTCallbacks {

    public DatabaseHelper db;
    public CompositeDisposable disposables = new CompositeDisposable();
    private AdView mAdView;
    public String path = "";
    PickiT pickiT;
    BackupFragment backupFragment;
    BackupHistoryFragment backupHistoryFragment;



    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("Backup and Read Chat");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = DatabaseHelper.getDatabase(this);


        pickiT = new PickiT(this, this);

        backupFragment = new BackupFragment();
        backupHistoryFragment = new BackupHistoryFragment();

        MainFileAdapter mainFileAdapter = new MainFileAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.view_pager);
        mainFileAdapter.addFragment(backupFragment);
        mainFileAdapter.addFragment(backupHistoryFragment);
        viewPager.setAdapter(mainFileAdapter);

        tabLayout =  findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.auto_reply, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }else if (id==R.id.how_to_use){
            Intent intent = new Intent(BackupActivity.this,HowToUseActivity4.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
            }
        }
    }

    ProgressBar mProgressBar;
    TextView percentText;
    private AlertDialog mdialog;

    @Override
    public void PickiTonStartListener() {

        final AlertDialog.Builder mPro = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        @SuppressLint("InflateParams") final View mPView = LayoutInflater.from(this).inflate(R.layout.dailog_layout, null);
        percentText = mPView.findViewById(R.id.percentText);

        percentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickiT.cancelTask();
                if (mdialog != null && mdialog.isShowing()) {
                    mdialog.cancel();
                }
            }
        });

        mProgressBar = mPView.findViewById(R.id.mProgressBar);
        mProgressBar.setMax(100);
        mPro.setView(mPView);
        mdialog = mPro.create();
        mdialog.show();

    }

    private void showLongToast(final String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

        String progressPlusPercent = progress + "%";
        percentText.setText(progressPlusPercent);
        mProgressBar.setProgress(progress);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        if (mdialog != null && mdialog.isShowing()) {
            mdialog.cancel();
        }

        //  Check if it was a Drive/local/unknown provider file and display a Toast
        if (wasDriveFile){
            showLongToast("Drive file was selected");
        }else if (wasUnknownProvider){
            showLongToast("File was selected from unknown provider");
        }else {
            showLongToast("Local file was selected");
        }

        //  Chick if it was successful
        if (wasSuccessful) {
            //  Set returned path to TextView
            this.path = path;
            backupFragment.convertButton.setBackgroundResource(R.drawable.custom_button_green);
            Toast.makeText(this, "File Selected", Toast.LENGTH_SHORT).show();

        }else {
            showLongToast("Error, please see the log..");
        }
    }


    public class MainFileAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        MainFileAdapter(FragmentManager fm) {
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
                return "New Chat";
            } else {
                return "History";
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

}
