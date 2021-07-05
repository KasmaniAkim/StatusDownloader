package com.HashTagApps.WATool.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.otherAppNotification;
import com.HashTagApps.WATool.model.AllNotificationItem2;

import java.util.ArrayList;

public class otherAppsNotificationAdapter extends RecyclerView.Adapter<otherAppsNotificationAdapter.ViewHolder> {
    private ArrayList<AllNotificationItem2> items;
    private com.HashTagApps.WATool.activity.otherAppNotification otherAppNotification;
    private boolean isChecked = false;

    public otherAppsNotificationAdapter(ArrayList<AllNotificationItem2> items, otherAppNotification otherAppNotification) {
        this.otherAppNotification = otherAppNotification;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.othernotiitem, parent, false);



        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final AllNotificationItem2 allNotificationItem = items.get(holder.getAdapterPosition());
        Log.e("qqqqqqq","qqqqqq"+allNotificationItem.getNotificationText());

        holder.title.setText(allNotificationItem.getPackageName());
        holder.subtitle.setText(allNotificationItem.getPackageTitle());
        holder.temp.setText(allNotificationItem.getNotificationText());
        holder.date.setText(allNotificationItem.getNotificationDate());

        if (isChecked) {
            holder.checkBox.setChecked(allNotificationItem.isChecked());
            holder.checkBox.setVisibility(View.VISIBLE);

        } else {

            if (allNotificationItem.isChecked()) {
                holder.checkBox.setChecked(!allNotificationItem.isChecked());
                allNotificationItem.setChecked(!allNotificationItem.isChecked());
            }

            holder.checkBox.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isChecked) {

                    AllNotificationItem2 folderItem = items.get(holder.getAdapterPosition());
                    folderItem.setChecked(!folderItem.isChecked());

                    if (folderItem.isChecked()) {
                        otherAppNotification.tempitems.add(folderItem);
                    } else {
                        otherAppNotification.tempitems.remove(folderItem);
                    }

                    notifyDataSetChanged();

                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isChecked = true;
                otherAppNotification.itemDelete.setVisible(true);
                otherAppNotification.itemSelect.setVisible(false);
                otherAppNotification.isSelected = true;
                AllNotificationItem2 folderItem = items.get(holder.getAdapterPosition());
                folderItem.setChecked(!folderItem.isChecked());
                otherAppNotification.tempitems.add(folderItem);
                notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        TextView subtitle,temp,date;
        CheckBox checkBox;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView =  itemView.findViewById(R.id.clear_icon);
            title = itemView.findViewById(R.id.folder_name);
            subtitle = itemView.findViewById(R.id.size);
            temp = itemView.findViewById(R.id.item);
            checkBox = itemView.findViewById(R.id.checkbox_del);
            date = itemView.findViewById(R.id.date_time);

            imageView.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            date.setVisibility(View.VISIBLE);

        }
    }
}
