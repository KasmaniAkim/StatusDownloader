package com.HashTagApps.WATool.adapter;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.AutoReplay;
import com.HashTagApps.WATool.model.ReplyMessageItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;


public class AutoReplyAdapter  extends RecyclerView.Adapter<AutoReplyAdapter.ViewHolder> {

    private ArrayList<ReplyMessageItem> fileItems;
    private AutoReplay autoReplay;
    private String readMessage = "";

    public AutoReplyAdapter(ArrayList<ReplyMessageItem> fileItems, AutoReplay autoReplay) {

        this.fileItems = fileItems;
        this.autoReplay = autoReplay;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.reply_item, viewGroup, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final ReplyMessageItem fileItem = fileItems.get(viewHolder.getAdapterPosition());

        viewHolder.receiveText.setText(fileItem.getReadMessage());
        viewHolder.sendText.setText(fileItem.getReplyMessage());

        if (fileItem.getEnableMessage() == 1) {

            viewHolder.aSwitch.setChecked(true);
        } else {


            viewHolder.aSwitch.setChecked(false);
        }

        viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              autoReplay.enableMessage(b, fileItem.getReadMessage());
            }
        });

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(autoReplay);
                builder.setCancelable(false);
                builder.setTitle("Confirm Delete");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        readMessage = fileItem.getReadMessage();
                        new BackTask().execute();
                        notifyItemRemoved(viewHolder.getAdapterPosition());
                        autoReplay.dismissNotifyy();
                    }
                });

                builder.setNegativeButton("NO", null);
                builder.show();

            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    public class BackTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            autoReplay.db.userDao().deleteMessage(readMessage);
            return null;
        }
    }

    @Override
    public int getItemCount() { return fileItems.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView receiveText, sendText;
        Switch aSwitch;
        ImageButton button;

        ViewHolder(@NonNull View itemView) {

            super(itemView);
            this.receiveText = itemView.findViewById(R.id.receive_text);
            this.sendText = itemView.findViewById(R.id.send_text);
            this.aSwitch = itemView.findViewById(R.id.switch1);
            this.button = itemView.findViewById(R.id.delete_auto_reply);

        }
    }
}

