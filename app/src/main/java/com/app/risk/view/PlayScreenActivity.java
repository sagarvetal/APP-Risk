package com.app.risk.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.adapters.PlayScreenRVAdapter;

import java.util.ArrayList;

public class PlayScreenActivity extends AppCompatActivity {

    private ImageView pImage;
    private TextView pName,pArmies,pCountries;
    private RecyclerView recyclerView;
    private CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        initId();
        getData();
    }

    public void getData(){
        Intent intent = getIntent();
        ArrayList<String> sample = new ArrayList<>();
        String mapInfo = intent.getStringExtra("MAP_INFO");
        sample = intent.getStringArrayListExtra("PLAYER_INFO");

        Toast.makeText(this, "" + mapInfo + "\n"
                + sample, Toast.LENGTH_SHORT).show();
    }

    public void initId()
    {
        pImage = findViewById(R.id.play_screen_image);
        pName = findViewById(R.id.play_screen_player_name);
        pCountries = findViewById(R.id.play_screen_territories);
        pArmies = findViewById(R.id.play_screen_armies);
        cardView = findViewById(R.id.play_screen_cardview);
        recyclerView = findViewById(R.id.play_screen_reyclerview);

        ArrayList<String> temp = new ArrayList<>();
        for(int i=0;i<10;i++){
            temp.add("Something "+ i);
        }

        LinearLayoutManager layout = new LinearLayoutManager(this);
        PlayScreenRVAdapter adapter = new PlayScreenRVAdapter(this,temp);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layout);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(false);

    }
}
