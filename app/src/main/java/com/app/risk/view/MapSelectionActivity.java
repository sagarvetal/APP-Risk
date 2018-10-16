package com.app.risk.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.adapters.ViewPagerAdapter;

import java.util.ArrayList;

public class MapSelectionActivity extends AppCompatActivity {

    private ArrayList<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selection);

        setUpViewPager();
        //setUpDisplay();
    }

    /*
     * This method initialize the pagerview of the activity
     */

    public void setUpViewPager(){
        ViewPager viewPager = findViewById(R.id.map_selection_viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),3);

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Toast.makeText(MapSelectionActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



}
