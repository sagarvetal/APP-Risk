package com.app.risk.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.risk.R;
import com.app.risk.adapters.MainRecyclerViewAdapter;

import java.util.ArrayList;

public class MainScreenActivity extends AppCompatActivity {

    // Holds the data to main menu and passed to recyceler view
    private ArrayList<String> mainMenuList;

    /*
     *Main onCreate method of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setUpData();
        recyclerView();
    }

    /*
     *Method retrieve the data and sets up the mainMenuList
     */
    public void setUpData() {
        final String mainMenuOptions[] = {"Play", "Create Map", "Edit Map",
                "Help", "Setting", "Exit"};
        mainMenuList = new ArrayList<>();
        for (String i : mainMenuOptions) {
            mainMenuList.add(i);
        }
    }

    /*
     *Method sets up recyclerview and its layout and passes
     * the list to it
     */
    public void recyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);

        /*StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        */

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MainRecyclerViewAdapter(mainMenuList, MainScreenActivity.this));


    }


}
