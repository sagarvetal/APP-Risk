package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.risk.R;
import com.app.risk.adapters.MainRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * MainScreenActivity display the main screen of the display
 * @author Himanshu Kohli
 * @version 1.0.0
 */
public class MainScreenActivity extends AppCompatActivity {
     //Holds the data to main menu and passed to recycler view
    private ArrayList<String> mainMenuList;

    /**
     * This method is the main method of the activity
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setUpData();
        recyclerView();
    }

    /**
     * This method retrieve the data and sets up the mainMenuList
     */
    public void setUpData() {
        final String mainMenuOptions[] = {"Play", "Create Map", "Edit Map",
                "Help", "Setting", "Exit"};
        mainMenuList = new ArrayList<>();
        for (String i : mainMenuOptions) {
            mainMenuList.add(i);
        }
    }

    /**
     * This method sets up recyclerview and its layout and passes
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



    /**
     * This method handles the back button listener and
     * confirms if user wants to exit or not
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Alert").setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No",null)
                .create().show();

    }
}
