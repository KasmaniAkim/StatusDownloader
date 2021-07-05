package com.HashTagApps.WATool.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.HashTagApps.WATool.model.AppItem;
import com.bumptech.glide.Glide;
import com.HashTagApps.WATool.MainActivity;
import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.AutoReplay;
import com.HashTagApps.WATool.activity.BackupActivity;
import com.HashTagApps.WATool.activity.DeletedMessageActivity;
import com.HashTagApps.WATool.activity.DirectChatActivity;
import com.HashTagApps.WATool.activity.ScheduleMessageActivity;
import com.HashTagApps.WATool.activity.StatusSaverActivity;
import com.HashTagApps.WATool.activity.WhatsAppCleanerActivity;
import com.HashTagApps.WATool.activity.WhatsAppGalleryActivity;
import com.HashTagApps.WATool.model.MainItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<MainItem> mainItems;
    private MainActivity mainActivity;
//    ArrayList<AppItem> appItems;

    public MainAdapter(ArrayList<MainItem> mainItems, MainActivity mainActivity) {
        this.mainItems = mainItems;
        this.mainActivity = mainActivity;
//        this.appItems = appItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.main_item, viewGroup, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final MainItem mainItem = mainItems.get(viewHolder.getAdapterPosition());

        Glide.with(mainActivity)
                .load(mainItem.getImageFile())
                .centerCrop()
                .into(viewHolder.itemImageView);

        viewHolder.titleTextView.setText(mainItem.getTitle());

        viewHolder.subTitleText.setText(mainItem.getSubTitle());

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mainItem.getTitle()) {
                    case "Status Saver": {
                        Intent intent = new Intent(mainActivity, StatusSaverActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    }
                    case "Direct Chat": {
                        Intent intent = new Intent(mainActivity, DirectChatActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    }
                    case "Deleted Message": {
                        Intent intent = new Intent(mainActivity, DeletedMessageActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    }
                    case "Scheduler": {
                        Intent intent = new Intent(mainActivity, ScheduleMessageActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    }
                    case "WhatsApp Gallery": {
                        Intent intent = new Intent(mainActivity, WhatsAppGalleryActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    }
                    case "Backup and Read Chat" : {
                        Intent intent = new Intent(mainActivity, BackupActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    }
                    case "Auto Reply" : {
                        Intent intent = new Intent(mainActivity, AutoReplay.class);
                        mainActivity.startActivity(intent);
                        break;
                    }
                    case "Gallery Cleaner" : {
                        Intent intent = new Intent(mainActivity, WhatsAppCleanerActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() { return mainItems.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImageView;
        LinearLayout linearLayout;
        TextView titleTextView, subTitleText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout  = itemView.findViewById(R.id.main_item_view);
            itemImageView = itemView.findViewById(R.id.item_image);
            titleTextView = itemView.findViewById(R.id.main_title);
            subTitleText = itemView.findViewById(R.id.sub_title);
        }
    }
}

