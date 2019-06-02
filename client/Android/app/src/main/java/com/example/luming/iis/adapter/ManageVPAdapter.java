package com.example.luming.iis.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.luming.iis.base.BaseFragment;

import java.util.List;

/**
 * Created by TukulHX on 2019/4/5
 *
 */
public class ManageVPAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;

    public ManageVPAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
