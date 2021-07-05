package com.HashTagApps.WATool.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.ImageFileAdapter;
import com.HashTagApps.WATool.adapter.SavedFileAdapter;
import com.HashTagApps.WATool.adapter.StatusPageAdapter;
import com.HashTagApps.WATool.adapter.VideoFileAdapter;
import com.HashTagApps.WATool.fragment.ImageFragment;
import com.HashTagApps.WATool.fragment.SavedFragment;
import com.HashTagApps.WATool.fragment.VideoFragment;
import com.HashTagApps.WATool.model.FileItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class StatusSaverActivity extends AppCompatActivity {

    private final int STORAGE_PERMISSION_CODE=1;
    ViewPager viewPager;
    StatusPageAdapter adapter;
    TabLayout tabLayout;
    private AdView mAdView;

    public ImageFileAdapter imageFileAdapter;
    public VideoFileAdapter videoFileAdapter;
    public SavedFileAdapter fileAdapter;
    public boolean isSelected = false;
    public MenuItem itemSelect,itemDownload,itemShare,itemDelete,howToUse;

    public ArrayList<FileItem> fileItemArrayList = new ArrayList<>();

    public static boolean imageFragmentVisible = false;
    public static boolean videoFragmentVisible = false;
    public static boolean savedFragmentVisible = false;
    public static boolean savedImageFragmentVisible = false;
    public static boolean savedVideoFragmentVisible = false;

    private ImageFragment imageFragment;
    private VideoFragment videoFragment;
    private SavedFragment savedFragment;

    RelativeLayout NotificationToast;
    TextView enable;

    Boolean temp=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_saver);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        setTitle("Status Saver");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        imageFragment = new ImageFragment();
        videoFragment = new VideoFragment();
        savedFragment = new SavedFragment();

        viewPager = findViewById(R.id.view_pager);
        adapter = new StatusPageAdapter(getSupportFragmentManager());
        adapter.addFragment(imageFragment);
        adapter.addFragment(videoFragment);
        adapter.addFragment(savedFragment);


        tabLayout =  findViewById(R.id.tab_layout);

        NotificationToast = findViewById(R.id.permissionToast);
        CheckStoragePermission();

        if (!temp){
            NotificationToast.setVisibility(View.VISIBLE);
            enable = findViewById(R.id.enable);
            enable.setOnClickListener(view -> CheckStoragePermission());
            tabLayout.setVisibility(View.GONE);
        } else {
            NotificationToast.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }


        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (temp){
            NotificationToast.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationToast.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);

            }

        }

    }

    public void CheckStoragePermission(){
        try {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    }

                }
            }else {
                temp=true;
            }
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_recycle, menu);
        itemSelect = menu.findItem(R.id.select).setVisible(true);
        howToUse = menu.findItem(R.id.how_to_use).setVisible(true);
        itemDownload = menu.findItem(R.id.multiple_download).setVisible(false);
        itemDelete = menu.findItem(R.id.multiple_delete).setVisible(false);
        itemShare = menu.findItem(R.id.multiple_share).setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: this.finish(); break;
            case R.id.how_to_use:
                Intent intent1 = new Intent(StatusSaverActivity.this,HowToUseActivity.class);
                startActivity(intent1); break;

            case R.id.select:
                isSelected = true;
                fileItemArrayList.clear();
                itemDownload.setVisible(true);
                itemDelete.setVisible(true);
                itemShare.setVisible(true);
                itemSelect.setVisible(false);
                howToUse.setVisible(false);
                if (imageFragmentVisible) {
                    imageFileAdapter.setChecked(true);
                } else if (videoFragmentVisible){
                    videoFileAdapter.setChecked(true);
                } else  if (savedFragmentVisible) {
                    itemDownload.setVisible(false);
                    fileAdapter.setChecked(true);
                }
                break;
            case R.id.multiple_download:
                String restoredText =  Environment.getExternalStorageDirectory()+"/WATool Saved Status" ;
                for (FileItem fileItem: fileItemArrayList) {
                    try {
                        copyFile(fileItem.getFile().getAbsolutePath(),fileItem.getFile().getName(),restoredText);
                    } catch (IOException e) {
                        Log.e("Error","download failed"+e.getMessage());
                    }
                }
                Toast.makeText(this, "Saved" + fileItemArrayList.size(), Toast.LENGTH_SHORT).show();
                isSelected = false;
                itemDownload.setVisible(false);
                itemDelete.setVisible(false);
                itemShare.setVisible(false);
                itemSelect.setVisible(true);
                howToUse.setVisible(true);
                if (imageFragmentVisible) {
                    imageFileAdapter.setChecked(false);
                } else if (videoFragmentVisible){
                    videoFileAdapter.setChecked(false);
                } else  if (savedFragmentVisible) {
                    fileAdapter.setChecked(false);
                }
                fileItemArrayList.clear();
                break;
            case R.id.multiple_delete:
                ArrayList<File> files = new ArrayList<>();
                for (FileItem fileItem: fileItemArrayList) {
                    files.add(fileItem.getFile());
                }
                delete(files);
                break;
            case R.id.multiple_share:
                ArrayList<File> list = new ArrayList<>();
                for (FileItem fileItem: fileItemArrayList) {
                    list.add(fileItem.getFile());
                }
                Intent intent = new Intent();
                if (imageFragmentVisible) {
                    intent = shareMultipleImageIntent(list);
                } else if (videoFragmentVisible) {
                    intent = shareMultipleVideoIntent(list);
                } else if (savedFragmentVisible) {
                    if (savedImageFragmentVisible) {
                        intent = shareMultipleImageIntent(list);
                    } else if (savedVideoFragmentVisible) {
                        intent = shareMultipleVideoIntent(list);
                    }
                }
                startActivity(intent);

                isSelected = false;
                itemDownload.setVisible(false);
                itemDelete.setVisible(false);
                itemShare.setVisible(false);
                itemSelect.setVisible(true);
                howToUse.setVisible(true);
                if (imageFragmentVisible) {
                    imageFileAdapter.setChecked(false);
                } else if (videoFragmentVisible){
                    videoFileAdapter.setChecked(false);
                } else  if (savedFragmentVisible) {
                    fileAdapter.setChecked(false);
                }
                fileItemArrayList.clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(isSelected){
            isSelected = false;
            itemShare.setVisible(false);
            itemDownload.setVisible(false);
            itemDelete.setVisible(false);
            itemDelete.setVisible(false);
            itemSelect.setVisible(true);
            howToUse.setVisible(true);
            if (imageFragmentVisible) {
                imageFileAdapter.setChecked(false);
            } else if (videoFragmentVisible){
                videoFileAdapter.setChecked(false);
            } else  if (savedFragmentVisible) {
                fileAdapter.setChecked(false);
            }
        } else {
            super.onBackPressed();
        }
    }

    private void copyFile(String inputPath, String inputFile, String savePath) throws IOException {

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

    private Intent shareMultipleImageIntent(ArrayList<File> list) {

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        ArrayList<Uri> files = new ArrayList<>();

        for (File file : list)
        {
            Uri uri = Uri.parse(file.getAbsolutePath());
            files.add(uri);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);

        return Intent.createChooser(intent, "Share File");

    }

    private Intent shareMultipleVideoIntent(ArrayList<File> list) {

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        ArrayList<Uri> files = new ArrayList<>();

        for (File file : list)
        {
            Uri uri = Uri.parse(file.getAbsolutePath());
            files.add(uri);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);

        return Intent.createChooser(intent, "Share File");

    }

    public void delete(final ArrayList<File> files) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Delete selected items?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                boolean deleted = false;
                for (File file: files) {
                    deleted = file.delete();
                }
                if (deleted) {
                    Toast.makeText(StatusSaverActivity.this, "File Deleted", Toast.LENGTH_SHORT).show();
                    if (imageFragmentVisible) {
                        imageFragment.addView(imageFragment.getFromSdcard());
                    } else if (videoFragmentVisible) {
                        videoFragment.addView(videoFragment.getFromSdcard());
                    } else if (savedFragmentVisible) {
                        if (savedImageFragmentVisible) {
                            savedFragment.addView(savedFragment.getFromSdcard("jpg"), "image");
                        } else if (savedVideoFragmentVisible) {
                            savedFragment.addView(savedFragment.getFromSdcard("mp4"), "video");
                        }
                    }

                }

            }
        });

        builder.setNegativeButton("CANCEL", null);
        builder.show();

    }

}
