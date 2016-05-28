package com.bignerdranch.android.criminalintent51.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent51.R;
import com.bignerdranch.android.criminalintent51.base.SingleFragmentActivity;
import com.bignerdranch.android.criminalintent51.bean.Crime;
import com.bignerdranch.android.criminalintent51.fragment.CrimeFragment;
import com.bignerdranch.android.criminalintent51.fragment.CrimeListFragment;

/**
 * Created by zhangH on 2016/5/24.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks{

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }


    @Override
    public void onCrimeSelected(Crime crime) {
        /** 如果当前布局R.layout.activity_masterdetail中不包含R.id.detail_fragment_container
         * 说明系统没有匹配w600dp,说明当前不是平板,则将Crime对象启动在CrimePagerActivity中
         * 如果!=null, 说明当前匹配的是一个平板,则将Crime对象启动在CrimeFragment中
         * 并将其对象添加到R.id.detail_fragment_container节点下 */
        if (findViewById(R.id.detail_fragment_container) == null){
            Intent i = CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(i);
        }else {
            CrimeFragment crimeFragment = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,crimeFragment).commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeRemove(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }
}
