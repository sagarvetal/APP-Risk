package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.Interfaces.PhaseManager;
import com.app.risk.R;
import com.app.risk.adapters.PlayScreenRVAdapter;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.controller.StartupPhaseController;
import com.app.risk.model.GamePlay;
import com.app.risk.utility.MapReader;

import java.util.ArrayList;

public class PlayScreenActivity extends AppCompatActivity implements PhaseManager {

    private ImageView pImage;
    private TextView pName, pArmies, pCountries;
    private RecyclerView recyclerView;
    private CardView cardView;
    private GamePlay gamePlay;
    private PlayScreenRVAdapter adapter;
    private String mapName;
    private ArrayList<String> playerNames;
    private ActionBar actionBar;
    private FloatingActionButton floatingActionButton;
    private String currentPhase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        actionBar = getSupportActionBar();
        init();
        manageFloatingButtonTransitions();
        startGame();
    }

    private void manageFloatingButtonTransitions() {


        floatingActionButton = findViewById(R.id.activity_play_screen_floating_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (currentPhase){
                    case GamePlayConstants.REINFORCEMENT_PHASE:

                        View view = getLayoutInflater().inflate(R.layout.player_selection_option,null);
                        new AlertDialog.Builder(PlayScreenActivity.this)
                                .setTitle("Card Exchange")
                                .setView(view)
                                .create()
                                .show();

                        break;
                    case GamePlayConstants.ATTACK_PHASE:
                        changePhase(GamePlayConstants.FORTIFICATION_PHASE);
                        break;
                    case GamePlayConstants.FORTIFICATION_PHASE:
                        changePhase(GamePlayConstants.REINFORCEMENT_PHASE);
                        break;
                }

            }
        });
    }

    public void init() {
        final Intent intent = getIntent();
        mapName = intent.getStringExtra("MAP_NAME");
        playerNames = intent.getStringArrayListExtra("PLAYER_INFO");
        Toast.makeText(this, "" + mapName + "\n" + playerNames, Toast.LENGTH_SHORT).show();

        pImage = findViewById(R.id.play_screen_image);
        pName = findViewById(R.id.play_screen_player_name);
        pCountries = findViewById(R.id.play_screen_territories);
        pArmies = findViewById(R.id.play_screen_armies);
        cardView = findViewById(R.id.play_screen_cardview);
        recyclerView = findViewById(R.id.play_screen_reyclerview);

        final LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
    }

    public void startGame() {
        changePhase(GamePlayConstants.STARTUP_PHASE);
    }

    @Override
    public void changePhase(final String phase) {
        if (phase != null) {
            switch (phase) {
                case GamePlayConstants.STARTUP_PHASE:
                    Toast.makeText(this, phase, Toast.LENGTH_SHORT).show();
                    gamePlay = MapReader.returnGamePlayFromFile(this.getApplicationContext(), mapName);
                    gamePlay.setCurrentPhase(phase);
                    gamePlay.setPlayers(playerNames);
                    final StartupPhaseController startupPhase = new StartupPhaseController(gamePlay);
                    startupPhase.start();
                    changePhase(GamePlayConstants.REINFORCEMENT_PHASE);
                    break;

                case GamePlayConstants.REINFORCEMENT_PHASE:
                    floatingActionButton.setImageResource(R.drawable.ic_card_white_24dp);
                    currentPhase = GamePlayConstants.REINFORCEMENT_PHASE;
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);
                    gamePlay.setCurrentPhase(phase);
                    gamePlay.setCurrentPlayer();
                    final ReinforcementPhaseController reinforcementPhase = new ReinforcementPhaseController(gamePlay);
                    reinforcementPhase.setReinforcementArmies();

                    adapter = new PlayScreenRVAdapter(PlayScreenActivity.this, gamePlay);
                    recyclerView.setAdapter(adapter);
                    adapter.setPhaseManager(this);
                    pName.setText(gamePlay.getCurrentPlayer().getName());
                    pArmies.setText("" + gamePlay.getCurrentPlayer().getNoOfArmies());
                    pCountries.setText("" + gamePlay.getCurrentPlayer().getNoOfCountries());
                    break;

                case GamePlayConstants.ATTACK_PHASE:
                    floatingActionButton.setImageResource(R.drawable.ic_shield_24dp);
                    currentPhase = GamePlayConstants.ATTACK_PHASE;
                    Toast.makeText(PlayScreenActivity.this, phase, Toast.LENGTH_SHORT).show();
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);
                    break;

                case GamePlayConstants.FORTIFICATION_PHASE:
                    floatingActionButton.setImageResource(R.drawable.ic_armies_add_24dp);
                    currentPhase = GamePlayConstants.FORTIFICATION_PHASE;
                    Toast.makeText(this, phase, Toast.LENGTH_SHORT).show();
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);
                    break;
            }
        }
    }

}
