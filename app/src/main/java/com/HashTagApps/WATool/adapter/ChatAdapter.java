package com.HashTagApps.WATool.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.ChatActivity;
import com.HashTagApps.WATool.model.MainChatItem;
import com.HashTagApps.WATool.model.NewMessageItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    private ArrayList<MainChatItem> newMessageItems;
    private ChatActivity chatActivity;
    private boolean isChecked = false;

    public ChatAdapter(ArrayList<MainChatItem> newMessageItems, ChatActivity chatActivity) {
        this.newMessageItems = newMessageItems;
        this.chatActivity = chatActivity;
    }

    public void addMessageFeed(ArrayList<MainChatItem> messageFeeds) {
        newMessageItems.clear();
        newMessageItems.addAll(messageFeeds);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        MainChatItem mainChatItem = newMessageItems.get(viewHolder.getAdapterPosition());
        NewMessageItem newMessageItem = newMessageItems.get(viewHolder.getAdapterPosition()).getNewMessageItem();

        if (newMessageItem.getIsDeleted().equals("true")){
            viewHolder.linearLayout.setBackgroundResource(R.drawable.rectangle_primary);
            viewHolder.messageText.setTextColor(Color.parseColor("#000000"));
            viewHolder.dateText.setTextColor(Color.parseColor("#000000"));
        }else {
            viewHolder.linearLayout.setBackgroundResource(R.drawable.rectangle2);
        }

        viewHolder.messageText.setText(newMessageItem.getMessage());
        if (newMessageItem.getSubTitle().equals("")) {
            viewHolder.nameText.setVisibility(View.GONE);
        } else {
            viewHolder.nameText.setVisibility(View.VISIBLE);
        }
        viewHolder.nameText.setText(newMessageItem.getSubTitle());

        Date date = new Date();
        date.setTime(newMessageItem.getMessageTime());
        @SuppressLint("SimpleDateFormat")
        String formattedDate=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(date);

        viewHolder.dateText.setText(formattedDate);

        if (isChecked) {
            viewHolder.checkBox.setChecked(mainChatItem.isChecked());
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            if (mainChatItem.isChecked()) {
                viewHolder.checkBox.setChecked(!mainChatItem.isChecked());
                mainChatItem.setChecked(!mainChatItem.isChecked());
            }
            viewHolder.checkBox.setVisibility(View.GONE);
        }

        viewHolder.checkBox.setOnClickListener(v -> {

            MainChatItem folderItem = newMessageItems.get(viewHolder.getAdapterPosition());
            folderItem.setChecked(!folderItem.isChecked());

            if (folderItem.isChecked()) {
                chatActivity.mainChatItemArrayList.add(folderItem);
            } else {
                chatActivity.mainChatItemArrayList.remove(folderItem);
            }

            notifyDataSetChanged();
        });

        viewHolder.linearLayout.setOnLongClickListener(view -> {
            MainChatItem folderItem = newMessageItems.get(viewHolder.getAdapterPosition());
            folderItem.setChecked(!folderItem.isChecked());

            if (folderItem.isChecked()) {
                chatActivity.mainChatItemArrayList.add(folderItem);
            } else {
                chatActivity.mainChatItemArrayList.remove(folderItem);
            }

            chatActivity.isSelect = true;
            chatActivity.itemDelete.setVisible(true);
            chatActivity.itemSelect.setVisible(false);
            setChecked(true);

            return false;
        });

        viewHolder.messageText.setOnClickListener(view -> {
            ClipboardManager clipMan = (ClipboardManager) chatActivity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipMan != null) {
                ClipData clip = ClipData.newPlainText("text label",newMessageItem.getMessage());
                clipMan.setPrimaryClip(clip);
                Toast.makeText(chatActivity, "Message copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return newMessageItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, dateText,nameText;
        CheckBox checkBox;
        LinearLayout linearLayout;
        ViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            nameText = itemView.findViewById(R.id.name_text);
            linearLayout = itemView.findViewById(R.id.chat_item);
            dateText = itemView.findViewById(R.id.date_tex);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}


