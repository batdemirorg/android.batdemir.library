package com.android.batdemir.mylibrary.Tools;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.android.batdemir.mylibrary.GlobalVariable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToolTimeExpressions {

    private String TAG = ToolTimeExpressions.class.getSimpleName();
    private Context context;

    public ToolTimeExpressions(@NonNull Context context) {
        this.context = context;
    }

    @SuppressLint("SimpleDateFormat")
    public Date setStringToDate(@NonNull String stringDate,
                                @NonNull GlobalVariable.DateFormat outputFormat) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(outputFormat.toString());
            date = sdf.parse(stringDate);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public String setDateToString(@NonNull Date date,
                                  @NonNull GlobalVariable.DateFormat outputFormat) {
        String result = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat.toString());
            result = dateFormat.format(date);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    public String setDateFormat(@NonNull String stringDate,
                                @NonNull GlobalVariable.DateFormat inputFormat,
                                @NonNull GlobalVariable.DateFormat outputFormat) {
        String result = "";
        try {
            Date date = setStringToDate(stringDate, inputFormat);
            SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat.toString());
            result = dateFormat.format(date);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    public void showDatePicker(@NonNull final View buttonOrTextView,
                               @NonNull final String titleText,
                               @NonNull final String positiveButtonText,
                               @NonNull final String negativeButtonText) {
        buttonOrTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        if (buttonOrTextView.getClass() == Button.class) {
                            Button button = (Button) buttonOrTextView;
                            button.setText(setDateToString(selectedDate.getTime(), GlobalVariable.DateFormat.SHOW_DATE_FORMAT));
                        }
                        if (buttonOrTextView.getClass() == TextView.class) {
                            TextView textView = (TextView) buttonOrTextView;
                            textView.setText(setDateToString(selectedDate.getTime(), GlobalVariable.DateFormat.SHOW_DATE_FORMAT));
                        }
                    }
                }, year, month, day);

                datePickerDialog.setTitle(titleText);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, positiveButtonText, datePickerDialog);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, negativeButtonText, datePickerDialog);
                datePickerDialog.show();
            }
        });
    }

    public void showTimePicker(@NonNull final View buttonOrTextView,
                               @NonNull final String titleText,
                               @NonNull final String positiveButtonText,
                               @NonNull final String negativeButtonText) {
        buttonOrTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, hourOfDay, minute, Calendar.SECOND);
                        if (buttonOrTextView.getClass() == Button.class) {
                            Button button = (Button) buttonOrTextView;
                            button.setText(setDateToString(selectedTime.getTime(), GlobalVariable.DateFormat.SMALL_TIME_FORMAT));
                        }
                        if (buttonOrTextView.getClass() == TextView.class) {
                            TextView textView = (TextView) buttonOrTextView;
                            textView.setText(setDateToString(selectedTime.getTime(), GlobalVariable.DateFormat.SMALL_TIME_FORMAT));
                        }
                    }
                }, hour, minute, true);

                timePickerDialog.setTitle(titleText);
                timePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, positiveButtonText, timePickerDialog);
                timePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, negativeButtonText, timePickerDialog);
                timePickerDialog.show();
            }
        });
    }
}
