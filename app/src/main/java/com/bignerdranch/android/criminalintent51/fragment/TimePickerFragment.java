package com.bignerdranch.android.criminalintent51.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.bignerdranch.android.criminalintent51.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhangH on 2016/5/25.
 */
public class TimePickerFragment extends DialogFragment {
    public static final String ARG_TIME ="time";
    private static final String TAG = "TimePicker";
    public static final String EXTRA_TIME =
            "com.bignerdranch.android.criminalintent.time";

    public static TimePickerFragment newInstance(Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME,date);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        return timePickerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Date date = (Date) getArguments().getSerializable(ARG_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.w(TAG, "onCreateView: year:"+year+";month:"+month+";day:"+day );

        final TimePicker timePicker = (TimePicker) inflater.inflate(R.layout.dialog_time_picker, container, false);
//        Button enterButton = (Button) timePicker.findViewById(R.id.dialog_time_enter_button);
//        enterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Integer hour = timePicker.getCurrentHour();
//                Integer minute = timePicker.getCurrentMinute();
//                Date time = new GregorianCalendar(year, month, day, hour, minute).getTime();
//                sendTimeResult(Activity.RESULT_OK,time);
//                dismiss();
//            }
//        });
        return timePicker;
    }



    private void sendTimeResult(int resultCode, Date date){
        if (getTargetFragment()==null){
            return;
        }
        Log.w(TAG, "sendTimeResult: date: "+date );
        Intent i = new Intent();
        i.putExtra(EXTRA_TIME,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
    }
}
