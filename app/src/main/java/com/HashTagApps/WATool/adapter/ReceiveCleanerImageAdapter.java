package com.HashTagApps.WATool.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.FileExplorerActivity;
import com.HashTagApps.WATool.activity.ImageDetailActivity;
import com.HashTagApps.WATool.activity.VideoPlayerActivity;
import com.HashTagApps.WATool.model.FileItem;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReceiveCleanerImageAdapter extends RecyclerView.Adapter<ReceiveCleanerImageAdapter.ViewHolder> {

    private ArrayList<FileItem> fileItems;
    private FileExplorerActivity fileExplorerActivity;
    private boolean isChecked = false;
    private boolean visible = true;

    public ReceiveCleanerImageAdapter(ArrayList<FileItem> fileItems, FileExplorerActivity fileExplorerActivity) {

        this.fileItems = fileItems;
        this.fileExplorerActivity = fileExplorerActivity;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_view, viewGroup, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final FileItem fileItem = fileItems.get(viewHolder.getAdapterPosition());


        viewHolder.size.setText(format(fileItem.getFile().length()));


        final String path = fileItem.getFile().getAbsolutePath();
        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(fileExplorerActivity)
                .load(path)
                .centerCrop()
                .apply(options)
                .into(viewHolder.icon);

        if(isChecked){

            viewHolder.checkBox.setChecked(fileItem.isChecked());
            viewHolder.checkBox.setVisibility(View.VISIBLE);

        } else {

            if (fileItem.isChecked()) {
                viewHolder.checkBox.setChecked(!fileItem.isChecked());
                fileItem.setChecked(!fileItem.isChecked());
            }
            viewHolder.checkBox.setVisibility(View.GONE);

        }

        if (visible) {
            viewHolder.size.setVisibility(View.VISIBLE);
        } else {
            viewHolder.size.setVisibility(View.GONE);
        }

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isChecked) {

                    FileItem folderItem = fileItems.get(viewHolder.getAdapterPosition());
                    folderItem.setChecked(!folderItem.isChecked());

                    if (folderItem.isChecked()) {
                        fileExplorerActivity.mainFileItems.add(folderItem);
                        fileExplorerActivity.updateSize();
                    } else {
                        fileExplorerActivity.mainFileItems.remove(folderItem);
                        fileExplorerActivity.updateSize();
                    }

                    notifyDataSetChanged();

                } else {

                    String mimeType = URLConnection.guessContentTypeFromName(fileItem.getFile().getAbsolutePath());
                    if (mimeType.startsWith("image")) {
                        Intent intent = new Intent(fileExplorerActivity, ImageDetailActivity.class);
                        intent.putExtra("position", viewHolder.getAdapterPosition());
                        intent.putExtra("type", "saved");
                        intent.putExtra("reverse", true);
                        intent.putExtra("sort", fileExplorerActivity.sort);
                        intent.putExtra("small_sort", fileExplorerActivity.smallSort);
                        intent.putExtra("latest",fileExplorerActivity.latestFirst);
                        intent.putExtra("oldest",fileExplorerActivity.oldestFirst);
                        intent.putExtra("path", fileItem.getFile().getParent());
                        fileExplorerActivity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(fileExplorerActivity, VideoPlayerActivity.class);
                        intent.putExtra("position", viewHolder.getAdapterPosition());
                        intent.putExtra("type", "saved");
                        intent.putExtra("reverse", true);
                        intent.putExtra("sort", fileExplorerActivity.sort);
                        intent.putExtra("small_sort", fileExplorerActivity.smallSort);
                        intent.putExtra("latest",fileExplorerActivity.latestFirst);
                        intent.putExtra("oldest",fileExplorerActivity.oldestFirst);
                        intent.putExtra("folder", fileItem.getFile().getParent());
                        fileExplorerActivity.startActivity(intent);
                    }

                }
            }
        });

        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isChecked = true;
                fileExplorerActivity.itemDelete.setVisible(true);
                fileExplorerActivity.itemShare.setVisible(true);
                fileExplorerActivity.itemSelect.setVisible(false);
                fileExplorerActivity.isSelected = true;
                FileItem folderItem = fileItems.get(viewHolder.getAdapterPosition());
                folderItem.setChecked(!folderItem.isChecked());
                fileExplorerActivity.mainFileItems.add(folderItem);
                fileExplorerActivity.updateSize();
                notifyDataSetChanged();
                return true;
            }
        });
    }

    public void isVisible(boolean visible) {
        this.visible = visible;
        notifyDataSetChanged();
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return fileItems.size(); }

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

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView icon,play;
        LinearLayout linearLayout;
        CheckBox checkBox;
        TextView size;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            this.icon = itemView.findViewById(R.id.image_item_icon);
            this.linearLayout = itemView.findViewById(R.id.image_layout);
            this.checkBox = itemView.findViewById(R.id.checkbox);
            this.play = itemView.findViewById(R.id.play);
            this.size = itemView.findViewById(R.id.size_image);

            checkBox.setVisibility(View.GONE);

        }
    }
}

