package com.example.blckclov3r.arduino_firebase_nodemcu.PickerPackage;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import static com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentFragment.mDob_tv;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String COMMON_TAG = "abrenica_nujla";
    private static final String TAG = DatePickerFragment.class.getSimpleName();

    int mYear = 0;
    int mMonth =0;
    int mDay = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month+1;
        mDay = dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mDob_tv.setText(String.valueOf(mMonth)+"/"+String.valueOf(mDay)+"/"+String.valueOf(mYear));
    }

}
