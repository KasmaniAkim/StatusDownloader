package com.HashTagApps.WATool.fragment;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.ScheduleMessageActivity;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

public class ScheduledFragment extends Fragment {

    private TextView phoneText, messageText,date_time;
    private Switch aSwitch;
    private LinearLayout linearLayout;
    ScheduleMessageActivity scheduleMessageActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduled, container, false);

        phoneText = view.findViewById(R.id.phone_text);
        messageText = view.findViewById(R.id.message_text);
        date_time = view.findViewById(R.id.date_time);
        ImageButton button = view.findViewById(R.id.delete_sheduled);
        aSwitch = view.findViewById(R.id.switch1);
        linearLayout = view.findViewById(R.id.main_layout);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Details", 0);
        @SuppressLint("CommitPrefEdits")
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        String phone = sharedPreferences.getString("phone", "");
        String message = sharedPreferences.getString("message", "");
        String date = sharedPreferences.getString("date", "");
        String time = sharedPreferences.getString("time", "");



        if (sharedPreferences.getBoolean("delete", false)) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }

        phoneText.setText(phone);
        messageText.setText(message);
        date_time.setText(date+" "+time);


        aSwitch.setChecked(sharedPreferences.getBoolean("enable", false));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("enable", b);
                editor.apply();
                if (b){
                    scheduleMessageActivity.notifyy();
                }else {
                    scheduleMessageActivity.dismissNotifyy();
                }
            }
        });

        if (aSwitch.isChecked()){
            scheduleMessageActivity.notifyy();
        }else {
            scheduleMessageActivity.dismissNotifyy();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("delete", true);
                editor.apply();
                linearLayout.setVisibility(View.GONE);
            }
        });

        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        scheduleMessageActivity = (ScheduleMessageActivity) context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Details", 0);

            String phone = sharedPreferences.getString("phone", "");
            String message = sharedPreferences.getString("message", "");
            String date = sharedPreferences.getString("date", "");
            String time = sharedPreferences.getString("time", "");

            if (sharedPreferences.getBoolean("delete", false)) {
                linearLayout.setVisibility(View.GONE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
            }
            phoneText.setText(phone);
            messageText.setText(message);
            date_time.setText(date+" "+time);
            aSwitch.setChecked(sharedPreferences.getBoolean("enable", false));
        }
    }
}
