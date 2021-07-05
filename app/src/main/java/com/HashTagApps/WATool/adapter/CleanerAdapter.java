package com.HashTagApps.WATool.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.FileDetailActivity;
import com.HashTagApps.WATool.activity.FileExplorerActivity;
import com.HashTagApps.WATool.activity.WhatsAppCleanerActivity;
import com.HashTagApps.WATool.model.MainItem;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CleanerAdapter extends RecyclerView.Adapter<CleanerAdapter.ViewHolder> {

    private ArrayList<MainItem> mainItems;
    private WhatsAppCleanerActivity whatsAppCleanerActivity;
    int count;

    public CleanerAdapter(ArrayList<MainItem> mainItems, WhatsAppCleanerActivity whatsAppCleanerActivity) {
        this.mainItems = mainItems;
        this.whatsAppCleanerActivity = whatsAppCleanerActivity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.cleaner_item, viewGroup, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final MainItem mainItem = mainItems.get(viewHolder.getAdapterPosition());

        Glide.with(whatsAppCleanerActivity)
                .load(mainItem.getImageFile())
                .centerCrop()
                .into(viewHolder.itemImageView);

        viewHolder.titleTextView.setText(mainItem.getTitle());

        viewHolder.size.setText(format(getFolderSize(new File(mainItem.getFolderPath())))+"");

        try {
            count = 0;
            viewHolder.item.setText(getNumberOfFiles(mainItem.getFolderPath())+" Files");
        } catch (FileNotFoundException e) {
            Log.e("qqqqqqq","qqqqqqqqqqq"+e.getMessage());
        }

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ("Databases".equals(mainItem.getTitle())) {

                    Intent intent = new Intent(whatsAppCleanerActivity, FileDetailActivity.class);
                    intent.putExtra("child", "Database");
                    intent.putExtra("gallery", false);
                    whatsAppCleanerActivity.startActivity(intent);

                } else if ("Documents".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppCleanerActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Documents");
                    intent.putExtra("gallery", false);
                    intent.putExtra("search",true);
                    whatsAppCleanerActivity.startActivity(intent);
                } else if ("Images".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppCleanerActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Images");
                    intent.putExtra("gallery", false);
                    whatsAppCleanerActivity.startActivity(intent);
                } else if ("Videos".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppCleanerActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Video");
                    intent.putExtra("gallery", false);
                    whatsAppCleanerActivity.startActivity(intent);
                } else if ("Audios".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppCleanerActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Audio");
                    intent.putExtra("gallery", false);
                    whatsAppCleanerActivity.startActivity(intent);
                } else if ("Voices".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppCleanerActivity, FileDetailActivity.class);
                    intent.putExtra("child", "WhatsApp Voice Notes");
                    intent.putExtra("gallery", false);
                    whatsAppCleanerActivity.startActivity(intent);
                } else if ("Gifs".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppCleanerActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Animated Gifs");
                    intent.putExtra("gallery", false);
                    whatsAppCleanerActivity.startActivity(intent);
                } else if ("Stickers".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppCleanerActivity, FileDetailActivity.class);
                    intent.putExtra("child", "WhatsApp Stickers");
                    intent.putExtra("gallery", false);
                    whatsAppCleanerActivity.startActivity(intent);
                }
            }
        });

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    whatsAppCleanerActivity.deleteButton.setVisibility(View.VISIBLE);
                    whatsAppCleanerActivity.files.add(new File(mainItem.getFolderPath()));
                } else {
                    whatsAppCleanerActivity.files.remove(new File(mainItem.getFolderPath()));
                    if (whatsAppCleanerActivity.files.size() == 0) {
                        whatsAppCleanerActivity.deleteButton.setVisibility(View.GONE);
                    }
                }
            }
        });
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

    private long getFolderSize(File dir) {
        long size = 0;
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile()) {
                System.out.println(file.getName() + " " + file.length());
                size += file.length();
            }
            else
                size += getFolderSize(file);
        }
        return size;
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

    @Override
    public int getItemCount() { return mainItems.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImageView;
        LinearLayout linearLayout;
        TextView titleTextView, size, item;
        CheckBox checkBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout  = itemView.findViewById(R.id.folder_layout);
            itemImageView = itemView.findViewById(R.id.clear_icon);
            titleTextView = itemView.findViewById(R.id.folder_name);
            size = itemView.findViewById(R.id.size);
            item = itemView.findViewById(R.id.item);
            checkBox = itemView.findViewById(R.id.checkbox_del);
        }
    }
}


