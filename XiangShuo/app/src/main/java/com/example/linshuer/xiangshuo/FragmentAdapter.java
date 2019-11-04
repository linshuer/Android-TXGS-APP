package com.example.linshuer.xiangshuo;

import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Linshuer on 2018/6/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter{
    public final static int TAB_COUNT = 3;
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int id) {
        switch (id) {
            case 0:
                return new HomeFragment();
            case 1:
                return new EditFragment();
            case 2:
                return new InfoFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}