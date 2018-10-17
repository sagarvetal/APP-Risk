package com.app.risk.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.risk.R;
import com.app.risk.adapters.MainRecyclerViewAdapter;

import java.util.ArrayList;

public class PlaceArmiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_armies);

        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        ArrayList<String> arrayList = new ArrayList<>();

        for(int i=0;i<10;i++) {
            arrayList.add("Country " + i);
        }
        recyclerView = findViewById(R.id.place_armiew_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(arrayList,this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);


    }

}

