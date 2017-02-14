package br.com.hotelurbano.desafio.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by gustavoamg on 11/02/17.
 */

public class DatePickerFragmentDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        long time = bundle.getLong("currentDate");

        Date currentSelectedDate = new Date();
        currentSelectedDate.setTime(time);

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        c.setTime(currentSelectedDate);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        if(onDateSetListener != null)
            return new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }
}
