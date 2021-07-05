package com.HashTagApps.WATool.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.ReceiveCleanerImageAdapter;
import com.HashTagApps.WATool.adapter.ReceiveFileAdapter;
import com.HashTagApps.WATool.adapter.SendCleanerImageAdapter;
import com.HashTagApps.WATool.adapter.SendFileAdapter;
import com.HashTagApps.WATool.fragment.ReceiveFragment;
import com.HashTagApps.WATool.fragment.SendFragment;
import com.HashTagApps.WATool.model.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class FileExplorerActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    private AdView mAdView;
    SearchView searchView;
    public boolean search;

    public ReceiveCleanerImageAdapter receiveCleanerImageAdapter;
    public ReceiveFileAdapter receiveFileAdapter;

    public SendCleanerImageAdapter sendCleanerImageAdapter;
    public SendFileAdapter sendFileAdapter;

    public MenuItem itemSelect,itemShare,itemDelete, itemMore;
    public String watsAppPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/media";

    public boolean isSelected = false;
    public boolean sort = false;
    public boolean smallSort = false;
    public boolean sentSort = false;
    public boolean sentSmallSort = false;
    public boolean gallery;
    public boolean latestFirst = false;
    public boolean oldestFirst = false;
    public boolean sentLatestFirst = false;
    public boolean sentOldestFirst = false;

    public ArrayList<FileItem> mainFileItems = new ArrayList<>();

    public ArrayList<FileItem> receiveFileItems = new ArrayList<>();
    public ArrayList<FileItem> sendFileItems = new ArrayList<>();

    public static boolean receiveFragmentVisible = false;
    public static boolean sendFragmentVisible = false;

    ReceiveFragment receiveFragment;
    SendFragment sendFragment;
    public String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        watsAppPath = watsAppPath + "/" + intent.getStringExtra("child");

        if (watsAppPath.contains("Images")) {
            title = "Images";
        } else if (watsAppPath.contains("Video")) {
            title = "Videos";
        } else if (watsAppPath.contains("Documents")) {
            title = "Docs";
        } else if (watsAppPath.contains("Audio")) {
            title = "Audios";
        } else if (watsAppPath.contains("Animated")) {
            title = "Animation Gifs";
        }

        setTitle(title);
        gallery = intent.getBooleanExtra("gallery", false);
        search = intent.getBooleanExtra("search",false);

        MainFileAdapter mainFileAdapter = new MainFileAdapter(getSupportFragmentManager());

        receiveFragment = new ReceiveFragment();
        sendFragment = new SendFragment();
        viewPager = findViewById(R.id.view_pager);
        mainFileAdapter.addFragment(receiveFragment);
        mainFileAdapter.addFragment(sendFragment);
        viewPager.setAdapter(mainFileAdapter);

        tabLayout =  findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        itemSelect = menu.findItem(R.id.select).setVisible(true);
        itemDelete = menu.findItem(R.id.multiple_delete).setVisible(false);
        itemShare = menu.findItem(R.id.multiple_share).setVisible(false);
        itemMore = menu.findItem(R.id.more).setVisible(false);
        itemMore.setVisible(true);

        if (search){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            menu.findItem(R.id.action_search).setVisible(true);
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);


            SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
            searchAutoComplete.setHintTextColor(getColor(R.color.white));
            searchAutoComplete.setTextColor(getColor(R.color.white));


            ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
            searchIcon.setImageDrawable(ContextCompat.getDrawable(FileExplorerActivity.this,R.drawable.search));

            searchView.setVisibility(View.VISIBLE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (receiveFragmentVisible){
                        receiveFileAdapter.getFilter().filter(query);
                    }else if (sendFragmentVisible){
                        sendFileAdapter.getFilter().filter(query);
                    }

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (receiveFragmentVisible){
                        receiveFileAdapter.getFilter().filter(newText);
                    }else if (sendFragmentVisible){
                        sendFileAdapter.getFilter().filter(newText);
                    }
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.select:
                isSelected = true;
                itemDelete.setVisible(true);
                itemShare.setVisible(true);
                itemSelect.setVisible(false);
                mainFileItems.clear();
                updateSize();
                if (receiveFragmentVisible) {
                    if (watsAppPath.contains("Images") || watsAppPath.contains("Video") ) {
                        receiveCleanerImageAdapter.setChecked(true);
                    } else {
                        receiveFileAdapter.setChecked(true);
                    }
                } else if (sendFragmentVisible){
                    if (watsAppPath.contains("Images") || watsAppPath.contains("Video") ) {
                        sendCleanerImageAdapter.setChecked(true);
                    } else {
                        sendFileAdapter.setChecked(true);
                    }
                }
                break;
            case R.id.multiple_delete:
                ArrayList<File> files = new ArrayList<>();
                for (FileItem fileItem: mainFileItems) {
                    files.add(fileItem.getFile());
                }
                delete(files);
                break;
            case R.id.multiple_share:
                ArrayList<File> list = new ArrayList<>();
                for (FileItem fileItem: mainFileItems) {
                    list.add(fileItem.getFile());
                }
                Intent intent = shareMultipleFileIntent(list);
                startActivity(intent);

                isSelected = false;
                itemDelete.setVisible(false);
                itemShare.setVisible(false);
                itemSelect.setVisible(true);
                if (receiveFragmentVisible) {
                    if (watsAppPath.contains("Images") || watsAppPath.contains("Video") ) {
                        receiveCleanerImageAdapter.setChecked(false);
                    } else {
                        receiveFileAdapter.setChecked(false);
                    }
                } else if (sendFragmentVisible){
                    if (watsAppPath.contains("Images") || watsAppPath.contains("Video") ) {
                        sendCleanerImageAdapter.setChecked(false);
                    } else {
                        sendFileAdapter.setChecked(false);
                    }
                }
                mainFileItems.clear();
                updateSize();
                break;
            case R.id.more:
                registerForContextMenu(viewPager);
                openContextMenu(viewPager);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (gallery){
            menu.add(0, 1, 1, "Sort by Latest").setOnMenuItemClickListener(onEditMenu);
            menu.add(0, 2, 2, "Sort by Oldest").setOnMenuItemClickListener(onEditMenu);
        }else {

            menu.add(0, 1, 1, "Default").setOnMenuItemClickListener(onEditMenu);
            menu.add(0, 2, 2, "Sort by Large Size").setOnMenuItemClickListener(onEditMenu);
            menu.add(0, 3, 3, "Sort by Small Size").setOnMenuItemClickListener(onEditMenu);
            menu.add(0, 4, 4, "Sort by Latest").setOnMenuItemClickListener(onEditMenu);
            menu.add(0, 5, 5, "Sort by Oldest").setOnMenuItemClickListener(onEditMenu);

        }
    }

    private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case 1:
                    if (receiveFragmentVisible) {
                        latestFirst = true;
                        oldestFirst = false;
                        receiveFragment.getFromSdcard();
                        receiveFragment.addView(receiveFileItems);
                    }else if (sendFragmentVisible){
                        sentLatestFirst = true;
                        sentOldestFirst = false;
                        sendFragment.getFromSdcard();
                        sendFragment.addView(sendFileItems);
                    }
                    break;
                case 2:
                    if (receiveFragmentVisible) {
                        latestFirst = false;
                        oldestFirst = true;
                        receiveFragment.getFromSdcard();
                        receiveFragment.addView(receiveFileItems);
                    }else if (sendFragmentVisible){
                        sentLatestFirst = false;
                        sentOldestFirst = true;
                        sendFragment.getFromSdcard();
                        sendFragment.addView(sendFileItems);
                    }

                    break;
                case 3:
                    if (receiveFragmentVisible) {
                        sort = false;
                        smallSort = false;
                        receiveFragment.getFromSdcard();
                        receiveFragment.addView(receiveFileItems);
                    } else if (sendFragmentVisible) {
                        sentSort = false;
                        sentSmallSort = false;
                        sendFragment.getFromSdcard();
                        sendFragment.addView(sendFileItems);
                    }
                    break;
                case 4:
                    if (receiveFragmentVisible) {
                        sort = true;
                        smallSort = false;
                        receiveFragment.getFromSdcard();
                        receiveFragment.addView(receiveFileItems);
                    } else if (sendFragmentVisible) {
                        sentSort = true;
                        sentSmallSort = false;
                        sendFragment.getFromSdcard();
                        sendFragment.addView(sendFileItems);
                    }
                    break;
                case 5:
                    if (receiveFragmentVisible) {
                        smallSort = true;
                        sort = false;
                        receiveFragment.getFromSdcard();
                        receiveFragment.addView(receiveFileItems);
                    } else if (sendFragmentVisible) {
                        sentSmallSort = true;
                        sentSort = false;
                        sendFragment.getFromSdcard();
                        sendFragment.addView(sendFileItems);
                    }
                    break;

            }
            return true;
        }
    };


    private Intent shareMultipleFileIntent(ArrayList<File> list) {

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
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
                    Toast.makeText(FileExplorerActivity.this, "File Deleted", Toast.LENGTH_SHORT).show();
                    isSelected = false;
                    itemShare.setVisible(false);
                    itemDelete.setVisible(false);
                    itemDelete.setVisible(false);
                    itemSelect.setVisible(true);
                    if (receiveFragmentVisible) {
                        receiveFragment.getFromSdcard();
                        receiveFragment.addView(receiveFileItems);
                    } else if (sendFragmentVisible){
                        sendFragment.getFromSdcard();
                        sendFragment.addView(sendFileItems);
                    }
                }

            }
        });

        builder.setNegativeButton("CANCEL", null);
        builder.show();

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
                return "Received";
            } else {
                return "Send";
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
    public void onBackPressed() {

        if(isSelected){
            isSelected = false;
            itemShare.setVisible(false);
            itemDelete.setVisible(false);
            itemDelete.setVisible(false);
            itemSelect.setVisible(true);
            if (receiveFragmentVisible) {
                if (watsAppPath.contains("Images") || watsAppPath.contains("Video") ) {
                    receiveCleanerImageAdapter.setChecked(false);
                } else {
                    receiveFileAdapter.setChecked(false);
                }
            } else if (sendFragmentVisible){
                if (watsAppPath.contains("Images") || watsAppPath.contains("Video") ) {
                    sendCleanerImageAdapter.setChecked(false);
                } else {
                    sendFileAdapter.setChecked(false);
                }
            }
            mainFileItems.clear();
            updateSize();
        } else {
            super.onBackPressed();
        }
    }

    public void updateSize() {
        if (mainFileItems.isEmpty()) {
            setTitle(title);
        } else {
            setTitle(format(getFolderSize(mainFileItems)));
        }
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

    private long getFolderSize(ArrayList<FileItem> fileItems) {
        long size = 0;
        for (FileItem fileItem :fileItems) {
            size += fileItem.getFile().length();
        }
        return size;
    }

}
