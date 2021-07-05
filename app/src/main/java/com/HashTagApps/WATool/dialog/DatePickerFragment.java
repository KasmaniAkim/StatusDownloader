package com.HashTagApps.WATool.dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.HashTagApps.WATool.fragment.NewScheduledFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private NewScheduledFragment newScheduledFragment;
    public DatePickerFragment(NewScheduledFragment newScheduledFragment) {
        this.newScheduledFragment =  newScheduledFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        newScheduledFragment.mYear = c.get(Calendar.YEAR);
        newScheduledFragment.mMonth = c.get(Calendar.MONTH);
        newScheduledFragment.mDay = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, newScheduledFragment.mYear, newScheduledFragment.mMonth, newScheduledFragment.mDay);
    }

    @SuppressLint("SetTextI18n")
    public void onDateSet(DatePicker view, int year, int month, int day) {
        newScheduledFragment.mYear = year;
        newScheduledFragment.mMonth = month;
        newScheduledFragment.mDay = day;
        int fMonth = month+1;
        newScheduledFragment.date1.setText(day+"-"+fMonth+"-"+year);
    }
}