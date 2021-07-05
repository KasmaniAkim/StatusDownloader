package com.HashTagApps.WATool.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.adapter.FileDetailAdapter;
import com.HashTagApps.WATool.model.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class FileDetailActivity extends AppCompatActivity {

    private ArrayList<FileItem> fileItems;
    private RecyclerView recyclerView;
    public String watsAppPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/media";

    public boolean isSelected = false;
    public boolean sort = false;
    public boolean smallSort = false;
    boolean gallery;
    String title = "";

    public MenuItem itemSelect,itemShare,itemDelete, itemMore;
    public ArrayList<FileItem> mainFileItems = new ArrayList<>();
    FileDetailAdapter fileDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        watsAppPath = watsAppPath + "/" + intent.getStringExtra("child");

        if (watsAppPath.contains("Sticker")) {
            title = "Stickers";
        } else if (watsAppPath.contains("Voice")) {
            title = "Voices";
        }

        setTitle(title);

        gallery = intent.getBooleanExtra("gallery", false);

        recyclerView = findViewById(R.id.recycler_view);
        fileItems = new ArrayList<>();

        getFromSdcard();
        addView(fileItems);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getFromSdcard();
        addView(fileItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        itemSelect = menu.findItem(R.id.select).setVisible(true);
        itemDelete = menu.findItem(R.id.multiple_delete).setVisible(false);
        itemShare = menu.findItem(R.id.multiple_share).setVisible(false);
        itemMore = menu.findItem(R.id.more).setVisible(false);
        if (gallery) {
            itemMore.setVisible(false);
        } else {
            itemMore.setVisible(true);
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
                fileDetailAdapter.setChecked(true);
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
                Intent intent = shareMultipleVideoIntent(list);
                startActivity(intent);

                isSelected = false;
                itemDelete.setVisible(false);
                itemShare.setVisible(false);
                itemSelect.setVisible(true);
                fileDetailAdapter.setChecked(false);
                mainFileItems.clear();
                updateSize();
                break;
            case R.id.more:
                registerForContextMenu(recyclerView);
                openContextMenu(recyclerView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0, 1, 1, "Default").setOnMenuItemClickListener(onEditMenu);
        menu.add(0, 2, 2, "Sort by Large Size").setOnMenuItemClickListener(onEditMenu);
        menu.add(0, 3, 3, "Sort by Small Size").setOnMenuItemClickListener(onEditMenu);

    }

    private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case 1:
                    sort = false;
                    smallSort = false;
                    getFromSdcard();
                    addView(fileItems);
                    break;
                case 2:
                    sort = true;
                    smallSort = false;
                    getFromSdcard();
                    addView(fileItems);
                    break;
                case 3:
                    smallSort = true;
                    sort = false;
                    getFromSdcard();
                    addView(fileItems);
                    break;

            }
            return true;
        }
    };



    private Intent shareMultipleVideoIntent(ArrayList<File> list) {

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
                    Toast.makeText(FileDetailActivity.this, "File Deleted", Toast.LENGTH_SHORT).show();
                    getFromSdcard();
                    addView(fileItems);
                    fileDetailAdapter.setChecked(false);

                }

            }
        });

        builder.setNegativeButton("CANCEL", null);
        builder.show();

    }

    private void getFromSdcard() {
        fileItems.clear();
        getFiles(new File(watsAppPath));
    }

    private void getFiles(File dir) {

        ArrayList<File> files = new ArrayList<>();

        File[] listFile = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (File file : listFile) {
                if (! (file.getName().startsWith(".") || file.getName().startsWith("com.") || file.getName().startsWith("Android"))) {
                    if (!file.isDirectory()) {
                        files.add(file);
                    }
                }
            }
        }

        Collections.reverse(files);

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

        for (File file: files) {
            fileItems.add(new FileItem(file));
        }

    }

    private void addView(ArrayList<FileItem> fileItems) {

        fileDetailAdapter = new FileDetailAdapter(fileItems, this);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fileDetailAdapter);

    }

    @Override
    public void onBackPressed() {
        if(isSelected){
            isSelected = false;
            itemShare.setVisible(false);
            itemDelete.setVisible(false);
            itemDelete.setVisible(false);
            itemSelect.setVisible(true);
            fileDetailAdapter.setChecked(false);
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
