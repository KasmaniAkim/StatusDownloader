package com.HashTagApps.WATool.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.ChatActivity;
import com.HashTagApps.WATool.activity.DeletedMessageActivity;
import com.HashTagApps.WATool.model.MessageFeed;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public ArrayList<MessageFeed> messageFeeds;
    private DeletedMessageActivity deletedMessageActivity;

    public MessageAdapter(ArrayList<MessageFeed> messageFeeds, DeletedMessageActivity deletedMessageActivity) {
        this.messageFeeds = messageFeeds;
        this.deletedMessageActivity = deletedMessageActivity;
    }

    public void addMessageFeed(ArrayList<MessageFeed> messageFeeds) {
        this.messageFeeds.clear();
        this.messageFeeds.addAll(messageFeeds);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final MessageFeed newMessageItem = messageFeeds.get(viewHolder.getAdapterPosition());

        final String[] strings = newMessageItem.getUserTitle().split("\\(");
        viewHolder.titleText.setText(strings[0]);

        viewHolder.messageText.setText(newMessageItem.getMessage());

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(deletedMessageActivity, ChatActivity.class);
                intent.putExtra("title", strings[0]);
                intent.putExtra("main_key", newMessageItem.getMainKey());
                deletedMessageActivity.startActivity(intent);
            }
        });

        viewHolder.imageView.setOnClickListener(v -> deletedMessageActivity.confirmDelete(newMessageItem.getMainKey(), viewHolder.getAdapterPosition()));

    }


    @Override
    public int getItemCount() {
        return messageFeeds.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView titleText;
        LinearLayout linearLayout;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            titleText = itemView.findViewById(R.id.name);
            linearLayout = itemView.findViewById(R.id.item_layout);
            imageView = itemView.findViewById(R.id.delete_btn);
        }
    }
}

