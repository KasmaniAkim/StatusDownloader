package com.HashTagApps.WATool.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.ScheduleMessageActivity;
import com.HashTagApps.WATool.dialog.DatePickerFragment;
import com.HashTagApps.WATool.dialog.TimePickerFragment;

import com.HashTagApps.WATool.helperclass.AlarmReceiver;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

public class NewScheduledFragment extends Fragment {

    private ScheduleMessageActivity scheduleMessageActivity;
    private Calendar calendar;

    public EditText phone;
    private EditText message;
    public TextView date1,time;
    public int mYear,mMonth, mDay;
    public int hour, min;
    CountryCodePicker countryCodePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_sheduled, container, false);

        phone = view.findViewById(R.id.phn);
        message = view.findViewById(R.id.message);
        date1 = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        countryCodePicker = view.findViewById(R.id.cpp);

        SharedPreferences sharedPreferences = Objects.requireNonNull(scheduleMessageActivity).getSharedPreferences("Details", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(NewScheduledFragment.this);
                newFragment.show(scheduleMessageActivity.getSupportFragmentManager(), "datePicker");
            }
        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment(NewScheduledFragment.this);
                newFragment.show(scheduleMessageActivity.getSupportFragmentManager(), "timePicker");
            }
        });

        calendar = Calendar.getInstance(TimeZone.getDefault());

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        mYear= calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay= calendar.get(Calendar.DAY_OF_MONTH);
        int fMonth = mMonth+1;
        date1.setText(mDay+"-"+fMonth+"-"+mYear);
        time.setText(hour+" : "+min);

        view.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (!phone.getText().toString().isEmpty() && !message.getText().toString().isEmpty()) {

                    countryCodePicker.registerCarrierNumberEditText(phone);
                    String phn = countryCodePicker.getFullNumber();
                    String phoneNumber = phn.replace("+","");


                    editor.putString("phone", phoneNumber);
                    String main_mes = message.getText().toString()+"°";
                    editor.putString("message", main_mes);
                    editor.putBoolean("enable", true);
                    editor.putBoolean("delete", false);
                    editor.putString("date",date1.getText().toString());
                    editor.putString("time",time.getText().toString());
                    editor.apply();


                    Intent intent = new Intent(scheduleMessageActivity, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(scheduleMessageActivity, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) scheduleMessageActivity.getSystemService(ALARM_SERVICE);
                    if (alarmManager != null) {
                        calendar.set(Calendar.YEAR,mYear);
                        calendar.set(Calendar.MONTH,mMonth);
                        calendar.set(Calendar.DAY_OF_MONTH,mDay);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, min);
                        Toast.makeText(scheduleMessageActivity, "The message is being scheduled", Toast.LENGTH_SHORT).show();
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        scheduleMessageActivity.notifyy();
                    }
                    scheduleMessageActivity.viewPager.setCurrentItem(1);
                } else {
                    Toast.makeText(scheduleMessageActivity, "Enter details", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        scheduleMessageActivity = (ScheduleMessageActivity) context;
    }
}
