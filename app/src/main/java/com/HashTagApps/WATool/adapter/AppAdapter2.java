package com.HashTagApps.WATool.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.SettingActivity;
import com.HashTagApps.WATool.model.AppItem;
import com.HashTagApps.WATool.model.tempItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AppAdapter2 extends RecyclerView.Adapter<AppAdapter2.ViewHolder> {

    private ArrayList<AppItem> appItems;
    private SettingActivity introActivity;

    public AppAdapter2(ArrayList<AppItem> appItems, SettingActivity introActivity) {

        this.appItems = appItems;
        this.introActivity = introActivity;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.app_item, viewGroup, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final AppItem appItem = appItems.get(viewHolder.getAdapterPosition());

        Glide.with(introActivity)
                .load(appItem.getAppIcon())
                .centerCrop()
                .into(viewHolder.imageView);

        viewHolder.appTitle.setText(appItem.getAppName());
        final tempItem tempItem = new tempItem();
        tempItem.setAppName(appItem.getAppName());


        viewHolder.checkBox.setChecked(appItem.getChecked());
        viewHolder.checkBox.setTag(i);

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appItem.getChecked()) {
                    appItem.setChecked(false);
                    tempItem.setChecked(false);
                    introActivity.appItems.remove(tempItem);
                } else {
                    appItem.setChecked(true);
                    tempItem.setChecked(true);
                    introActivity.appItems.add(tempItem);
                }
            }

        });
    }

    @Override
    public int getItemCount() { return appItems.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        TextView appTitle;
        CheckBox checkBox;
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            this.relativeLayout = itemView.findViewById(R.id.app_layout);
            this.appTitle = itemView.findViewById(R.id.app_name);
            this.checkBox = itemView.findViewById(R.id.checkbox);
            this.imageView = itemView.findViewById(R.id.app_icon);

        }
    }
}



