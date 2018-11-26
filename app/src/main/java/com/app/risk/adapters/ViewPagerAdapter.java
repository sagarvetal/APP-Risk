package com.app.risk.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.risk.model.GameMap;
import com.app.risk.model.GamePlay;
import com.app.risk.utility.MapReader;
import com.app.risk.utility.MapVerification;
import com.app.risk.view.MapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager Adapter which iterates the viewpager items multiple times
 * @author Himanshu Kohli
 * @version 1.0.0
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    private ArrayList<String> mapList;
    private Context context;

    /**
     * It is parameterized constructor
     * @param fm  fragment which is active on screen
     * @param mapList list of names of map
     */
    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> mapList,Context context) {
        super(fm);
        this.mapList = mapList;
        this.context = context;
    }

    /**
     * This method get the item on the index
     * @param index gets the Object on the index
     * @return fragment on the the index
     */
    @Override
    public Fragment getItem(int index) {
        Fragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MAP_NAME", mapList.get(index));
        fragment.setArguments(bundle);
        return fragment;
    }

    public boolean isFileValid(String fileName) {
        MapReader mapReader = new MapReader();
        MapVerification mapVerification = new MapVerification();
        List<GameMap> arrGamePlay = mapReader.returnGameMapFromFile(context,fileName);
        return  mapVerification.mapVerification(arrGamePlay);
    }

    /**
     * This method which returns the size of the list
     * @return the size of the list
     */

    @Override
    public int getCount() {
        return mapList.size();
    }
}
