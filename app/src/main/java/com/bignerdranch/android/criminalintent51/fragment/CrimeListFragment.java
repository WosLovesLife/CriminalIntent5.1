package com.bignerdranch.android.criminalintent51.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bignerdranch.android.criminalintent51.R;
import com.bignerdranch.android.criminalintent51.bean.Crime;
import com.bignerdranch.android.criminalintent51.single.CrimeLab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangH on 2016/5/24.
 */
public class CrimeListFragment extends Fragment {

    private static final String TAG = "CrimeListFragment";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    private boolean mSubtitleVisible;
    private TextView mHintText;

    private Callbacks mCallbacks;

    public interface Callbacks {
        /** 当某个条目被选中时 */
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycle_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCrimeRecyclerView.setHasFixedSize(true);
        mHintText = (TextView) view.findViewById(R.id.crime_hint_text_view);

        return view;
    }

    /** 当托管Activity从暂停/停止状态恢复时,通过此生命周期方法刷新列表,同步数据 */
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * 刷新数据列表,保持和持久层数据一致.
     * 为了避免重复创建对象,加上判断,
     * 由于Adapter中的数据集合直接引用了持久层数据,所以这里不再重复过去数据
     */
    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
        hideHintText();
    }

    class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        private List<View> mViews;

        /** 外部将数据集合通过构造传递进来 */
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
            mViews = new ArrayList<>();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        /**
         * 只有在创建新的Holder的时候才会调用该方法,
         * 例如在首次填充屏幕的时候,需要显示多少个条目就会创建多少个Holder,
         * 以及在需要加载新类型的条目时
         */
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_crime, parent, false);
            mViews.add(view);
            return new CrimeHolder(view);
        }

        /** 每次刷新条目都会调用,从Holder中获取组件对象并设置数据 */
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            /** 为每一个ViewHolder设置Tag */
            holder.mView.setTag(mCrimes.get(position));
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        /** 总数据量大小 */
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    class CrimeHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "CrimeHolder";
        private Crime mCrime;
        public View mView;
        public TextView mTitleTextView;
        public TextView mDateTextView;
        public CheckBox mSolvedCheckBox;

        public CrimeHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w(TAG, "onClick: ");
                    mCallbacks.onCrimeSelected(mCrime);
                }
            });
        }

        public void bindCrime(Crime crime) {
            Log.w(TAG, "bindCrime: ");
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(DateFormat.format("cccc,MMMd,yyyy", crime.getDate()));
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem item = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            item.setTitle(R.string.hide_subtitle);
        } else {
            item.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                updateSubtitle();
                getActivity().invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSubtitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        String subtitle = null;
        if (mSubtitleVisible) {
            int size = CrimeLab.get(getActivity()).getCrimes().size();
            subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, size, size);
        }
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setSubtitle(subtitle);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    private void hideHintText() {
        if (CrimeLab.get(getActivity()).getCrimes().size() > 0) {
            mHintText.setVisibility(View.GONE);
        } else {
            mHintText.setVisibility(View.VISIBLE);
        }
    }
}
