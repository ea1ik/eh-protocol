package com.example.ehprotocol;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.YEAR, year);
        firstDay.set(Calendar.MONTH, 0);
        firstDay.set(Calendar.DAY_OF_MONTH, 22);

        DatePickerDialog datePicker =  new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePicker.getDatePicker().setMinDate(firstDay.getTimeInMillis());

        return datePicker;
    }


}


