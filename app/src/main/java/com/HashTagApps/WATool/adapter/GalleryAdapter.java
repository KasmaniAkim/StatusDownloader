package com.HashTagApps.WATool.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.FileDetailActivity;
import com.HashTagApps.WATool.activity.FileExplorerActivity;
import com.HashTagApps.WATool.activity.WhatsAppGalleryActivity;
import com.HashTagApps.WATool.model.MainItem;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private ArrayList<MainItem> mainItems;
    private WhatsAppGalleryActivity whatsAppGalleryActivity;
    int count;

    public GalleryAdapter(ArrayList<MainItem> mainItems, WhatsAppGalleryActivity whatsAppGalleryActivity) {
        this.mainItems = mainItems;
        this.whatsAppGalleryActivity = whatsAppGalleryActivity;
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


        Glide.with(whatsAppGalleryActivity)
                .load(mainItem.getImageFile())
                .centerCrop()
                .into(viewHolder.itemImageView);

        viewHolder.titleTextView.setText(mainItem.getTitle());

        try {
            count = 0;
            viewHolder.item.setText(getNumberOfFiles(mainItem.getFolderPath())+" Files and Folders Found");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("Documents".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppGalleryActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Documents");
                    intent.putExtra("gallery", true);
                    intent.putExtra("search",true);
                    whatsAppGalleryActivity.startActivity(intent);
                } else if ("Images".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppGalleryActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Images");
                    intent.putExtra("gallery", true);
                    whatsAppGalleryActivity.startActivity(intent);
                } else if ("Videos".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppGalleryActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Video");
                    intent.putExtra("gallery", true);
                    whatsAppGalleryActivity.startActivity(intent);
                } else if ("Audios".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppGalleryActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Audio");
                    intent.putExtra("gallery", true);
                    whatsAppGalleryActivity.startActivity(intent);
                } else if ("Voices".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppGalleryActivity, FileDetailActivity.class);
                    intent.putExtra("child", "WhatsApp Voice Notes");
                    intent.putExtra("gallery", true);
                    whatsAppGalleryActivity.startActivity(intent);
                } else if ("Gifs".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppGalleryActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Animated Gifs");
                    intent.putExtra("gallery", true);
                    whatsAppGalleryActivity.startActivity(intent);
                } else if ("Stickers".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppGalleryActivity, FileDetailActivity.class);
                    intent.putExtra("child", "WhatsApp Stickers");
                    intent.putExtra("gallery", true);
                    whatsAppGalleryActivity.startActivity(intent);
                } else if ("Profile Pictures".equals(mainItem.getTitle())) {
                    Intent intent = new Intent(whatsAppGalleryActivity, FileExplorerActivity.class);
                    intent.putExtra("child", "WhatsApp Profile Pictures");
                    intent.putExtra("gallery", true);
                    whatsAppGalleryActivity.startActivity(intent);
                }
            }
        });



    }

    private File[] children(File file) {
        File[] children = file.listFiles();
        return (children != null) ? children : new File[0];
    }

    @Override
    public int getItemCount() { return mainItems.size(); }

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

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImageView;
        View linearLayout;
        TextView titleTextView, item,size;
        CheckBox checkBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout  = itemView.findViewById(R.id.folder_layout);
            itemImageView = itemView.findViewById(R.id.clear_icon);
            titleTextView = itemView.findViewById(R.id.folder_name);
            item = itemView.findViewById(R.id.item);
            size = itemView.findViewById(R.id.size);
            size.setVisibility(View.GONE);
            checkBox = itemView.findViewById(R.id.checkbox_del);
            checkBox.setVisibility(View.INVISIBLE);

//            itemView.findViewById(R.id.view).setVisibility(View.GONE);
//            itemView.findViewById(R.id.size).setVisibility(View.GONE);
        }
    }
}



