package com.bignerdranch.android.criminalintent51.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.bignerdranch.android.criminalintent51.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by zhangH on 2016/5/24.
 */
public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE =
            "com.bignerdranch.android.criminalintent.date";
    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date crimeDate){
        Bundle bundle =  new Bundle();
        bundle.putSerializable(ARG_DATE,crimeDate);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date crimeDate = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(crimeDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date,null);

        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /** 从DatePicker中取出整数的年月日 */
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int dayOfMonth = mDatePicker.getDayOfMonth();
                        /** 使用格里高利时间类将日期转为时间戳,设置result给调用者 */
                        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }

    /** 在用户点击了Dialog的按钮时,将新的日期
     * 通过调用targetFragment.onActivityResult()方法将结果返回
     */
    private void sendResult(int resultCode, Date date){
        /** 如果没有targetFragment,忽略 */
        if (getTargetFragment() == null){
            return;
        }
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
    }
}
