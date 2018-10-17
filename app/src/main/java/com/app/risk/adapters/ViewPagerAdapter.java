package com.app.risk.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.risk.view.MapFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter{


    private ArrayList<String> mapList;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> mapList) {
        super(fm);
        this.mapList = mapList;
    }

    @Override
    public Fragment getItem(int index) {
        Fragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MAP_NAME", mapList.get(index));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mapList.size();
    }
}
