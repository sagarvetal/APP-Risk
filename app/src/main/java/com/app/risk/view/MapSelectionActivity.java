package com.app.risk.view;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.risk.R;
import com.app.risk.adapters.ViewPagerAdapter;
import com.app.risk.utility.MapReader;

import java.util.ArrayList;

/**
 * MapSelectionActivity display the map selection screen of the display
 * @author Himanshu Kohli
 * @version 1.0.0
 */
public class MapSelectionActivity extends AppCompatActivity {

    private ArrayList<String> list;

    /**
     * This is the main creation method of the activity
     * @param savedInstanceState: the instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selection);

        setUpViewPager();
    }

    /**
     * This method initialize the pagerview of the activity
     */
    public void setUpViewPager() {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), MapReader.getMapList(this.getApplicationContext()));
        final ViewPager viewPager = findViewById(R.id.map_selection_viewpager);
        viewPager.setAdapter(adapter);

    }

}
