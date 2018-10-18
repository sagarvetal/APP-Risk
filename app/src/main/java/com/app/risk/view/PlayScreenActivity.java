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
import com.app.risk.controller.StartupPhaseController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.utility.MapReader;

import java.util.ArrayList;

public class PlayScreenActivity extends AppCompatActivity {

    private ImageView pImage;
    private TextView pName,pArmies,pCountries;
    private RecyclerView recyclerView;
    private CardView cardView;
    private GamePlay gamePlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        init();
    }

    public void init()
    {
        final Intent intent = getIntent();
        final String mapName = intent.getStringExtra("MAP_NAME");
        final ArrayList<String> playerNames = intent.getStringArrayListExtra("PLAYER_INFO");
        Toast.makeText(this, "" + mapName + "\n" + playerNames, Toast.LENGTH_SHORT).show();

        pImage = findViewById(R.id.play_screen_image);
        pName = findViewById(R.id.play_screen_player_name);
        pCountries = findViewById(R.id.play_screen_territories);
        pArmies = findViewById(R.id.play_screen_armies);
        cardView = findViewById(R.id.play_screen_cardview);
        recyclerView = findViewById(R.id.play_screen_reyclerview);

//        gamePlay = MapReader.readMap(getResources().openRawResource(R.raw.map1));
//        final StartupPhaseController startupPhaseController = new StartupPhaseController();
//        startupPhaseController.setPlayers(playerNames, gamePlay);
//        startupPhaseController.assignInitialCountries(gamePlay);
//        startupPhaseController.assignInitialArmies(gamePlay);
//        startupPhaseController.placeInitialArmies(gamePlay);
//        gamePlay.setCurrentPlayer(gamePlay.getPlayers().get(0));

//        final ArrayList<Country> countries = startupPhaseController.getCountryListByPlayerId(gamePlay.getCurrentPlayer().getId(), gamePlay);
//
//        final LinearLayoutManager layout = new LinearLayoutManager(this);
//        final PlayScreenRVAdapter adapter = new PlayScreenRVAdapter(this, gamePlay, countries);
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(layout);
//        recyclerView.setNestedScrollingEnabled(true);
//        recyclerView.setHasFixedSize(false);
//
//        pName.setText(gamePlay.getCurrentPlayer().getName());
//        pArmies.setText("" + gamePlay.getCurrentPlayer().getNoOfArmies());
//        pCountries.setText("" + gamePlay.getCurrentPlayer().getNoOfCountries());

    }
}
