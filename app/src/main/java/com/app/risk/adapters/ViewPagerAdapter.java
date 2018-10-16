package com.app.risk.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.risk.view.MapFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter{


    private int length;
    private String a[] = {"a","b","c"};

    public ViewPagerAdapter(FragmentManager fm,int length) {
        super(fm);
        this.length = length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("abc",a[position]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return length;
    }
}
