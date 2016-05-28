//package com.bignerdranch.android.criminalintent51.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.text.format.DateFormat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.bignerdranch.android.criminalintent51.R;
//import com.bignerdranch.android.criminalintent51.bean.Crime;
//import com.bignerdranch.android.criminalintent51.holder.CrimeHolder;
//
//import java.util.List;
//
///**
// * Created by zhangH on 2016/5/24.
// */
//public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
//
//    private Context mContext;
//    private List<Crime> mCrimes;
//
//    /** 外部将数据集合通过构造传递进来 */
//    public CrimeAdapter(Context context, List<Crime> crimes) {
//        mContext = context;
//        mCrimes = crimes;
//    }
//
//    /** 只有在创建新的Holder的时候才会调用该方法,
//     * 例如在首次填充屏幕的时候,需要显示多少个条目就会创建多少个Holder,
//     * 以及在需要加载新类型的条目时
//     */
//    @Override
//    public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.list_item_crime, parent, false);
//        return new CrimeHolder(mContext,view);
//    }
//
//    /** 每次刷新条目都会调用,从Holder中获取组件对象并设置数据 */
//    @Override
//    public void onBindViewHolder(CrimeHolder holder, int position) {
//        Crime crime = mCrimes.get(position);
//        holder.bindCrime(crime,position);
//    }
//
//    /** 总数据量大小 */
//    @Override
//    public int getItemCount() {
//        return mCrimes.size();
//    }
//}
