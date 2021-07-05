package com.HashTagApps.WATool.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.model.FileItem;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private Boolean isFabOpen = false;
    VideoView videoView;
    String videoPath;
    private FloatingActionMenu fab;
    private FloatingActionButton downloadFab, shareFab, deleteFab;
    ArrayList<FileItem> fileItems;
    private String type = "";
    int position=0;
    private AdView mAdView;

    private boolean sort = false;
    private boolean smallSort = false;
    private boolean reverse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("Video");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        fileItems = new ArrayList<>();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        String folder = intent.getStringExtra("folder");

        sort = intent.getBooleanExtra("sort", false);
        smallSort = intent.getBooleanExtra("small_sort", false);
        reverse = intent.getBooleanExtra("reverse", false);

        getFromSdcard(folder);
        position = intent.getIntExtra("position", 0);
        videoPath = fileItems.get(position).getFile().getAbsolutePath();

        videoView = findViewById(R.id.videoView);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        Uri uri = Uri.parse(videoPath);
        videoView.setMediaController(mc);
        videoView.setVideoURI(uri);
        videoView.start();

        fab = findViewById(R.id.fab);
        downloadFab = findViewById(R.id.download_fab);
        shareFab = findViewById(R.id.share_fab);
        deleteFab = findViewById(R.id.delete_fab);
        fab.setOnClickListener(this);
        downloadFab.setOnClickListener(this);
        shareFab.setOnClickListener(this);
        deleteFab.setOnClickListener(this);

        if(!type.equals("status")){
            downloadFab.setVisibility(View.GONE);
        }

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

    public void getFromSdcard(String folder) {

        fileItems.clear();

        File mainFile= new File(folder);

        ArrayList<File> files = new ArrayList<>();

        File[] listFile = mainFile.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (File file : listFile) {
                if (! (file.getName().startsWith("com.") || file.getName().startsWith("Android") || file.getName().contains(".nomedia"))) {
                    if (!file.isDirectory()) {
                        files.add(file);
                    }
                }
            }
        }

        if (reverse) {
            Collections.reverse(files);
        }

        if (sort) {
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File t, File t1) {
                    return (int) (t.length() - t1.length());
                }
            });
            Collections.reverse(files);
        }

        if (smallSort) {
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File t, File t1) {
                    return (int) (t.length() - t1.length());
                }
            });
        }

        for (File aListFile : files) {

            String mimeType = URLConnection.guessContentTypeFromName(aListFile.getAbsolutePath());
            Log.e("tag",mimeType + "=  = = = = " + aListFile.getName());
            if (mimeType.startsWith("video")) {

                fileItems.add(new FileItem(aListFile));

            }
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.download_fab:
                try {
                    copyFile(fileItems.get(position).getFile().getAbsolutePath(),
                            fileItems.get(position).getFile().getName());
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.share_fab:
                Uri uri = Uri.parse(fileItems.get(position).getFile().getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                break;
            case R.id.delete_fab:
                delete(fileItems.get(position).getFile());
                break;
        }
    }
    private void copyFile(String inputPath, String inputFile) throws IOException {
        //noinspection deprecation
        String savePath =  Environment.getExternalStorageDirectory()+"/WATool Saved Status" ;
        FileChannel inputChannel = null,outputChannel = null;

        File dir = new File (savePath);
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        try {
            outputChannel = new FileOutputStream(dir.getAbsolutePath()+"/"+inputFile).getChannel();
            inputChannel = new FileInputStream(inputPath).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();


        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }

    public void delete(final File file) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Delete selected items?");
        builder.setPositiveButton("DELETE", (dialogInterface, i) -> {

            //noinspection ResultOfMethodCallIgnored
            file.delete();
        });

        builder.setNegativeButton("CANCEL", null);
        builder.show();

    }
}
