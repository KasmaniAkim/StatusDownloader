package com.HashTagApps.WATool.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.HashTagApps.WATool.fragment.NewScheduledFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    NewScheduledFragment newScheduledFragment;
    public TimePickerFragment(NewScheduledFragment newScheduledFragment) {
        this.newScheduledFragment = newScheduledFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        newScheduledFragment.hour = c.get(Calendar.HOUR_OF_DAY);
        newScheduledFragment.min = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, newScheduledFragment.hour, newScheduledFragment.min,
                DateFormat.is24HourFormat(getActivity()));
    }

    @SuppressLint("SetTextI18n")
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        newScheduledFragment.hour = hourOfDay;
        newScheduledFragment.min = minute;
        newScheduledFragment.time.setText(newScheduledFragment.hour+" : "+newScheduledFragment.min);
    }
}
