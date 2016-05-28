//package com.bignerdranch.android.criminalintent51.holder;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bignerdranch.android.criminalintent51.R;
//import com.bignerdranch.android.criminalintent51.activity._CrimeActivity;
//import com.bignerdranch.android.criminalintent51.activity.CrimeListActivity;
//import com.bignerdranch.android.criminalintent51.bean.Crime;
//import com.bignerdranch.android.criminalintent51.fragment.CrimeListFragment;
//
///**
// * Created by zhangH on 2016/5/24.
// */
//public class CrimeHolder extends RecyclerView.ViewHolder {
//
//    private static final String TAG = "CrimeHolder";
//    private Context mContext;
//    private Crime mCrime;
//    private int mPosition;
//    public TextView mTitleTextView;
//    public TextView mDateTextView;
//    public CheckBox mSolvedCheckBox;
//
//    public CrimeHolder(final Context context, final View itemView) {
//        super(itemView);
//        mContext = context;
//        mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
//        mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
//        mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
//
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = _CrimeActivity.newIntent(mContext, mCrime.getId(), mPosition);
//
//            }
//        });
//    }
//
//    public void bindCrime(Crime crime, int position) {
//        Log.w(TAG, "bindCrime: ");
//        mCrime = crime;
//        mPosition = position;
//        mTitleTextView.setText(mCrime.getTitle());
//        mDateTextView.setText(DateFormat.format("cccc,MMMd,yyyy", crime.getDate()));
//        mSolvedCheckBox.setChecked(mCrime.isSolved());
//
//    }
//}
