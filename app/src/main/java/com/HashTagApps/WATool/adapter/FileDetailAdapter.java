package com.HashTagApps.WATool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.HashTagApps.WATool.BuildConfig;
import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.FileDetailActivity;
import com.HashTagApps.WATool.model.FileItem;

import java.io.File;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

public class FileDetailAdapter extends RecyclerView.Adapter<FileDetailAdapter.ViewHolder>  {

    private FileDetailActivity fileDetailActivity;
    private ArrayList<FileItem> folderItems;
    private boolean isChecked = false;

    public FileDetailAdapter(ArrayList<FileItem> folderItems, FileDetailActivity fileDetailActivity) {

        this.fileDetailActivity = fileDetailActivity;
        this.folderItems = folderItems;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.folder_item, viewGroup, false);
        return new ViewHolder(listItem);

    }

    @SuppressLint({"CheckResult","SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {


        final FileItem folderItem = folderItems.get(viewHolder.getAdapterPosition());

        viewHolder.name.setText(folderItem.getFile().getName());

        String path = folderItem.getFile().getAbsolutePath();

        RequestOptions options = new RequestOptions();
        options.centerCrop();


        Date lastModified = new Date(folderItem.getFile().lastModified());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY  hh:mm a");
        viewHolder.date.setText(simpleDateFormat.format(lastModified));

        viewHolder.size.setText(format(folderItem.getFile().length()));

        String cachedMimeType = URLConnection.guessContentTypeFromName(folderItem.getFile().getAbsolutePath());

        if ((cachedMimeType != null) && cachedMimeType.startsWith("image/")) {

            Glide.with(fileDetailActivity).load(path).apply(options).into(viewHolder.icon);

        }  else if (folderItem.getFile().getName().contains(".doc")) {

            viewHolder.icon.setImageResource(R.drawable.docs);

        } else if (folderItem.getFile().getName().contains(".xls")) {

            viewHolder.icon.setImageResource(R.drawable.xls);

        } else if (folderItem.getFile().getName().contains(".ppt")) {

            viewHolder.icon.setImageResource(R.drawable.ppt);

        } else if (folderItem.getFile().getName().contains(".pdf")) {

            viewHolder.icon.setImageResource(R.drawable.pdf);

        } else if (folderItem.getFile().getName().contains(".zip")) {

            viewHolder.icon.setImageResource(R.drawable.zip);

        } else if (folderItem.getFile().getName().contains(".rar")) {

            viewHolder.icon.setImageResource(R.drawable.zip);

        } else if ((cachedMimeType != null) && cachedMimeType.startsWith("audio/")) {

            viewHolder.icon.setImageResource(R.drawable.audio);

        } else if (folderItem.getFile().getName().contains(".apk")) {

            PackageInfo packageInfo = fileDetailActivity.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            if(packageInfo != null) {
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                appInfo.sourceDir = path;
                appInfo.publicSourceDir = path;
                Drawable icon1 = appInfo.loadIcon(fileDetailActivity.getPackageManager());

                Glide.with(fileDetailActivity).load(icon1).apply(options).into(viewHolder.icon);

            }

        } else if ((cachedMimeType != null) && cachedMimeType.startsWith("video")) {

            Glide.with(fileDetailActivity).load(path).apply(options).into(viewHolder.icon);

        } else {

            viewHolder.icon.setImageResource(R.drawable.ic_file);

        }

        if (isChecked) {
            viewHolder.checkBox.setChecked(folderItem.isChecked());
            viewHolder.checkBox.setVisibility(View.VISIBLE);

        } else {

            if (folderItem.isChecked()) {
                viewHolder.checkBox.setChecked(!folderItem.isChecked());
                folderItem.setChecked(!folderItem.isChecked());
            }

            viewHolder.checkBox.setVisibility(View.GONE);

        }

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isChecked) {

                    FileItem folderItem = folderItems.get(viewHolder.getAdapterPosition());
                    folderItem.setChecked(!folderItem.isChecked());

                    if (folderItem.isChecked()) {
                        fileDetailActivity.mainFileItems.add(folderItem);
                        fileDetailActivity.updateSize();
                    } else {
                        fileDetailActivity.mainFileItems.remove(folderItem);
                        fileDetailActivity.updateSize();
                    }

                    notifyDataSetChanged();

                } else {
                    String cachedMimeType = URLConnection.guessContentTypeFromName(folderItems.get(viewHolder.getAdapterPosition()).getFile().getAbsolutePath());

                    Intent intent = openFileIntent(uri(fileDetailActivity, folderItems.get(viewHolder.getAdapterPosition()).getFile()), cachedMimeType);

                    if (isResolvable(intent, fileDetailActivity)) {
                        fileDetailActivity.startActivity(intent);
                    } else {
                        Toast.makeText(fileDetailActivity, "Cannot open File", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isChecked = true;
                fileDetailActivity.itemDelete.setVisible(true);
                fileDetailActivity.itemShare.setVisible(true);
                fileDetailActivity.itemSelect.setVisible(false);
                fileDetailActivity.isSelected = true;
                FileItem folderItem = folderItems.get(viewHolder.getAdapterPosition());
                folderItem.setChecked(!folderItem.isChecked());
                fileDetailActivity.mainFileItems.add(folderItem);
                fileDetailActivity.updateSize();
                notifyDataSetChanged();
                return true;
            }
        });

    }

    private boolean isResolvable(Intent intent, Context context) {

        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> resolveInfo = manager.queryIntentActivities(intent, 0);

        return !resolveInfo.isEmpty();

    }

    private Uri uri(Context context, File file) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            try
            {
                return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            }
            catch (Exception e)
            {
                return Uri.fromFile(file);
            }
        }
        else
        {
            return Uri.fromFile(file);
        }

    }

    private Intent openFileIntent(Uri uri, String type) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return intent;
    }

    @Override
    public int getItemCount() {
        return folderItems.size();
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
        notifyDataSetChanged();
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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,size,date,view;
        ImageView icon;
        LinearLayout linearLayout;
        CheckBox checkBox;

        ViewHolder(@NonNull View itemView) {

            super(itemView);

            this.name = itemView.findViewById(R.id.name);
            this.size = itemView.findViewById(R.id.size);
            this.date = itemView.findViewById(R.id.date);
            this.view = itemView.findViewById(R.id.view);
            this.icon = itemView.findViewById(R.id.icon);
            this.linearLayout = itemView.findViewById(R.id.linear);
            this.checkBox = itemView.findViewById(R.id.checkbox);

//            checkBox.setOnClickListener(v -> {
//
//                FileExplorerActivity.itemPath += folderItems.get(getAdapterPosition()).getFile().getAbsolutePath() + "," ;
//                FolderItem folderItem = folderItems.get(getAdapterPosition());
//                folderItem.setPosition(getAdapterPosition());
//                folderItem.setSelected(!folderItem.isSelected());
//
//
//            });

        }

    }

}
