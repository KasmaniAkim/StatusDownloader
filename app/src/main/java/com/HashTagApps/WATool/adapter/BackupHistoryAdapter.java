package com.HashTagApps.WATool.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.BackupActivity;
import com.HashTagApps.WATool.activity.ChatScreenActivity;
import com.HashTagApps.WATool.model.HistoryFileData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BackupHistoryAdapter extends RecyclerView.Adapter<BackupHistoryAdapter.ViewHolder> {

    private ArrayList<HistoryFileData> historyFileDatas;
    private BackupActivity backupActivity;

    public BackupHistoryAdapter(ArrayList<HistoryFileData> historyFileData, BackupActivity backupActivity) {

        this.historyFileDatas = historyFileData;
        this.backupActivity = backupActivity;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.folder_item, viewGroup, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final HistoryFileData historyFileData = historyFileDatas.get(viewHolder.getAdapterPosition());

        File file = new File(historyFileData.getFilePath());

        viewHolder.name.setText(file.getName());

        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Date lastModified = new Date(file.lastModified());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  hh:mm a");
        viewHolder.date.setText(simpleDateFormat.format(lastModified));

        viewHolder.size.setText(format(file.length()));

        viewHolder.icon.setImageResource(R.drawable.ic_file);


        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(backupActivity, ChatScreenActivity.class);
                intent.putExtra("path", historyFileData.getFilePath());
                backupActivity.startActivity(intent);
            }
        });

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
    public int getItemCount() { return historyFileDatas.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,size,date,view;
        ImageView icon;
        LinearLayout linearLayout;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.name);
            size = itemView.findViewById(R.id.size);
            date = itemView.findViewById(R.id.date);
            view = itemView.findViewById(R.id.view);
            icon = itemView.findViewById(R.id.icon);
            linearLayout = itemView.findViewById(R.id.linear);

        }
    }
}