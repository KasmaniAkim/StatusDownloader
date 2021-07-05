package com.HashTagApps.WATool.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.GalleryAdapter;
import com.HashTagApps.WATool.model.MainItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class WhatsAppGalleryActivity extends AppCompatActivity {


    private int STORAGE_PERMISSION_CODE=1;
    RecyclerView recyclerView;
    ArrayList<MainItem> mainItems;
    GalleryAdapter galleryAdapter;
    private AdView mAdView;
    Boolean temp=false;
    RelativeLayout NotificationToast;
    TextView enable;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_gallary);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("WhatsApp Gallery");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.gallery_recycler_view);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(getStorageSize());
        TextView textView = findViewById(R.id.per_text);
        textView.setText(getStorageSize()+"%");
        TextView details = findViewById(R.id.details);
        StatFs statFs = new StatFs("/data");
        long   TotalSize  = ( (long) statFs.getBlockCount() * (long) statFs.getBlockSize());

        long   Free   = (statFs.getAvailableBlocks() * (long) statFs.getBlockSize());
        long   Busy   = TotalSize - Free;

        details.setText(format(Busy)+"/"+format(TotalSize));

        TextView size = findViewById(R.id.size);
        size.setText(format(getWhatsAppData()));
        TextView numbers = findViewById(R.id.number_of_files);
        String whatsAppPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media";
        try {

            numbers.setText(getNumberOfFiles(whatsAppPath)+" Files found");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        setTitle("Gallery");

        recyclerView = findViewById(R.id.gallery_recycler_view);

        NotificationToast = findViewById(R.id.permissionToast);
        checkStoragePermission();

        if (!temp) {
            NotificationToast.setVisibility(View.VISIBLE);
            enable = findViewById(R.id.enable);
            enable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    checkStoragePermission();
                }
            });
        }else{
            NotificationToast.setVisibility(View.GONE);
        }


        mainItems = new ArrayList<>();

        mainItems = new ArrayList<>();

        whatsAppPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp";

        mainItems.add(new MainItem("Images", "", R.drawable.icons8image, whatsAppPath+"/media/WhatsApp Images"));
        mainItems.add(new MainItem("Videos", "", R.drawable.icons8video, whatsAppPath+"/media/WhatsApp Video"));
        mainItems.add(new MainItem("Audios", "", R.drawable.audio, whatsAppPath+"/media/WhatsApp Audio"));
        mainItems.add(new MainItem("Documents", "", R.drawable.docs, whatsAppPath+"/media/WhatsApp Documents"));
        mainItems.add(new MainItem("Voices", "", R.drawable.voices, whatsAppPath+"/media/WhatsApp Voice Notes"));
        mainItems.add(new MainItem("Gifs", "", R.drawable.gif100, whatsAppPath+"/media/WhatsApp Animated Gifs"));


        fillData();

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void checkStoragePermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {



            Log.e("00000","11 called");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("00000","" +
                        "22 called");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        }else {
            temp=true;
        }
    }
    public int getStorageSize(){
        StatFs statFs = new StatFs("/data");
        long   TotalSize  = ( (long) statFs.getBlockCount() * (long) statFs.getBlockSize());

        long   Free   = (statFs.getAvailableBlocks() * (long) statFs.getBlockSize());
        long   Busy   = TotalSize - Free;

        long temp = Busy*100;
        return (int) (temp/TotalSize);
    }
    private String format(long originalSize) {
        String label = "B";
        double size = originalSize;

        if (size > 1024)
        {
            size /= 1024;
            label = "KB";
        }

        if (size > 1024)
        {
            size /= 1024;
            label = "MB";
        }

        if (size > 1024)
        {
            size /= 1024;
            label = "GB";
        }

        if (size % 1 == 0)
        {
            return String.format(Locale.getDefault(), "%d %s", (long) size, label);
        }
        else
        {
            return String.format(Locale.getDefault(), "%.1f %s", size, label);
        }
    }

    public long getWhatsAppData(){
        String whatsAppPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media";
        File file = new File(whatsAppPath);
        if (!file.exists())
            return 0;
        if (!file.isDirectory())
            return file.length();
        final List<File> dirs = new LinkedList<>();
        dirs.add(file);
        long result = 0;
        while (!dirs.isEmpty()) {
            final File dir = dirs.remove(0);
            if (!dir.exists())
                continue;
            final File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length == 0)
                continue;
            for (final File child : listFiles) {
                result += child.length();
                if (child.isDirectory())
                    dirs.add(child);
            }
        }
        return result;
    }

    private int getNumberOfFiles(String Path) throws FileNotFoundException {


        File f = new File(Path);
        File[] files = f.listFiles();

        if (files != null)
            for (int i = 0; i < files.length; i++) {
                count++;
                File file = files[i];

                if (file.isDirectory()) {
                    getNumberOfFiles(file.getAbsolutePath());
                }
            }
        return count;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationToast.setVisibility(View.GONE);
                fillData();
            }

        }

    }

    private void fillData() {
        galleryAdapter = new GalleryAdapter(mainItems,this);
        recyclerView.setLayoutManager((new GridLayoutManager(this, 2)));
        recyclerView.setAdapter(galleryAdapter);
    }

}
