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
import com.HashTagApps.WATool.activity.ImageDetailActivity;
import com.HashTagApps.WATool.activity.StatusSaverActivity;
import com.HashTagApps.WATool.model.FileItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageFileAdapter extends RecyclerView.Adapter<ImageFileAdapter.ViewHolder> {

    private ArrayList<FileItem> fileItems;
    private StatusSaverActivity statusSaverActivity;
    private boolean isChecked = false;

    public ImageFileAdapter(ArrayList<FileItem> fileItems, StatusSaverActivity statusSaverActivity) {

        this.fileItems = fileItems;
        this.statusSaverActivity = statusSaverActivity;

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

        final String path = fileItem.getFile().getAbsolutePath();
        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(statusSaverActivity)
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

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isChecked) {

                    FileItem folderItem = fileItems.get(viewHolder.getAdapterPosition());
                    folderItem.setChecked(!folderItem.isChecked());

                    if (folderItem.isChecked()) {
                        statusSaverActivity.fileItemArrayList.add(folderItem);
                    } else {
                        statusSaverActivity.fileItemArrayList.remove(folderItem);
                    }

                    notifyDataSetChanged();

                } else {

                    Intent intent = new Intent(statusSaverActivity, ImageDetailActivity.class);
                    intent.putExtra("position",viewHolder.getAdapterPosition());
                    intent.putExtra("type","status");
                    intent.putExtra("path",fileItem.getFile().getParent());
                    statusSaverActivity.startActivity(intent);

                }
            }
        });

        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isChecked = true;
                statusSaverActivity.itemDownload.setVisible(true);
                statusSaverActivity.itemDelete.setVisible(true);
                statusSaverActivity.itemShare.setVisible(true);
                statusSaverActivity.itemSelect.setVisible(false);
                statusSaverActivity.howToUse.setVisible(false);
                statusSaverActivity.isSelected = true;
                FileItem folderItem = fileItems.get(viewHolder.getAdapterPosition());
                folderItem.setChecked(!folderItem.isChecked());
                statusSaverActivity.fileItemArrayList.add(folderItem);
                notifyDataSetChanged();
                return true;
            }
        });
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return fileItems.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView icon,play;
        LinearLayout linearLayout;
        CheckBox checkBox;
        TextView textView;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            this.icon = itemView.findViewById(R.id.image_item_icon);

            this.linearLayout = itemView.findViewById(R.id.image_layout);
            this.checkBox = itemView.findViewById(R.id.checkbox);
            this.play = itemView.findViewById(R.id.play);
            this.textView=itemView.findViewById(R.id.size_image);
            textView.setVisibility(View.GONE);

            checkBox.setVisibility(View.GONE);


        }
    }
}
