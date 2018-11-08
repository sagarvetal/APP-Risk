package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.Interfaces.PhaseManager;
import com.app.risk.R;
import com.app.risk.adapters.PlayScreenRVAdapter;
import com.app.risk.constants.FileConstants;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.controller.StartupPhaseController;
import com.app.risk.model.Card;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Log;
import com.app.risk.model.Player;
import com.app.risk.utility.LogManager;
import com.app.risk.utility.MapReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * PlayScreenActivity display the play screen of the display
 * @author Himanshu Kohli
 * @version 1.0.0
 */
public class PlayScreenActivity extends AppCompatActivity implements PhaseManager,Observer {

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
    public ListView logView;
    public static ArrayAdapter<String> logViewAdapter;
    public static ArrayList<String> logViewArrayList;

    /**
     * This method is the main creation method of the activity
     * @param savedInstanceState: instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        LogManager.getInstance(this.getFilesDir() + File.separator + FileConstants.LOG_FILE_PATH,this).readLog();
        logView=findViewById(R.id.activity_play_screen_logview);
        logViewArrayList = new ArrayList<>();
        /*logViewArrayList.add("Sample 1");
        logViewArrayList.add("Sample 2");
        logViewArrayList.add("Sample 3");
        logViewArrayList.add("Sample 4");
        logViewArrayList.add("Sample 5");*/

        logViewAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,logViewArrayList);
        logView.setAdapter(logViewAdapter);

        LogManager.getInstance().deleteLog();
        LogManager.getInstance().writeLog("hello APP");
        LogManager.getInstance().writeLog("I am FIne..u?");
        LogManager.getInstance().writeLog("Before1 reading");
        LogManager.getInstance().readLog();
        LogManager.getInstance().writeLog("Before delete");
        LogManager.getInstance().deleteLog();
        LogManager.getInstance().writeLog("After delte");
        LogManager.getInstance().writeLog("hello APP");
        LogManager.getInstance().writeLog("I am FIne..u?");
        LogManager.getInstance().writeLog("Before2 reading");
        LogManager.getInstance().readLog();
        actionBar = getSupportActionBar();
        init();
        manageFloatingButtonTransitions();
        startGame();
    }
/**
 * This method initalizes and listens the evenets of the floating Button
 */
    private void manageFloatingButtonTransitions() {
        floatingActionButton = findViewById(R.id.activity_play_screen_floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (currentPhase){
                    case GamePlayConstants.REINFORCEMENT_PHASE:

                        if(gamePlay.getCurrentPlayer().getCards().size() != 0) {
                            String cards = "";

                            for(final Card card : gamePlay.getCurrentPlayer().getCards()){
                                if(cards.isEmpty()){
                                    cards = card.getType();
                                } else {
                                    cards += "\n" + card.getType();
                                }
                            }

                            if(cards.isEmpty()){
                                cards = GamePlayConstants.NO_CARDS_AVAILABLE;
                            }

                            new AlertDialog.Builder(PlayScreenActivity.this)
                                    .setTitle("Available Cards")
                                    .setMessage(cards)
                                    .create()
                                    .show();
                        }
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

    /**
     * This method initialize the the referrences and gets data from previous activity
     */
    public void init() {
        final Intent intent = getIntent();
        mapName = intent.getStringExtra("MAP_NAME");
        playerNames = intent.getStringArrayListExtra("PLAYER_INFO");
        pImage = findViewById(R.id.play_screen_image);
        pName = findViewById(R.id.play_screen_player_name);
        pCountries = findViewById(R.id.play_screen_territories);
        pArmies = findViewById(R.id.play_screen_armies);
        cardView = findViewById(R.id.play_screen_cardview);
        recyclerView = findViewById(R.id.play_screen_reyclerview);

        final LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
    }

    /**
     * This method invoker method of start game i.e. startup method
     */
    public void startGame() {
        changePhase(GamePlayConstants.STARTUP_PHASE);
    }

    /**
     * This method handles the phase transition of the gameplay
     * @param phase: The string which holds the phase
     */
    @Override
    public void changePhase(final String phase) {
        if (phase != null) {
            switch (phase) {
                case GamePlayConstants.STARTUP_PHASE:
                    gamePlay = MapReader.returnGamePlayFromFile(this.getApplicationContext(), mapName);
                    StartupPhaseController.getInstance().init(gamePlay).start(playerNames);
                    changePhase(GamePlayConstants.REINFORCEMENT_PHASE);
                    break;

                case GamePlayConstants.REINFORCEMENT_PHASE:
                    floatingActionButton.setImageResource(R.drawable.ic_card_white_24dp);
                    currentPhase = phase;
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);
                    ReinforcementPhaseController.getInstance().init(this, gamePlay).start();

                    adapter = new PlayScreenRVAdapter(this, gamePlay, recyclerView);
                    recyclerView.setAdapter(adapter);
                    adapter.setPhaseManager(this);
                    pName.setText(gamePlay.getCurrentPlayer().getName());
                    pArmies.setText("" + gamePlay.getCurrentPlayer().getNoOfArmies());
                    pCountries.setText("" + gamePlay.getCurrentPlayer().getNoOfCountries());

                    final Player currentPlayer = gamePlay.getCurrentPlayer();
                    final String message = String.format(GamePlayConstants.REINFORCEMENT_MSG,
                                                         currentPlayer.getNoOfArmies()-currentPlayer.getReinforcementArmies(),
                                                         currentPlayer.getNoOfCountries(),
                                                         currentPlayer.getName(),
                                                         currentPlayer.getReinforcementArmies());
                    displayAlert("", message);
                    displaySnackBar("Reinforcement : " + gamePlay.getCurrentPlayer().getName());
                    break;

                case GamePlayConstants.ATTACK_PHASE:
                    floatingActionButton.setImageResource(R.drawable.ic_shield_24dp);
                    currentPhase = phase;
                    gamePlay.setCurrentPhase(phase);
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);
                    displaySnackBar("Attack : " + gamePlay.getCurrentPlayer().getName());
                    break;

                case GamePlayConstants.FORTIFICATION_PHASE:
                    floatingActionButton.setImageResource(R.drawable.ic_armies_add_24dp);
                    currentPhase = phase;
                    gamePlay.setCurrentPhase(phase);
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);
                    displaySnackBar("Fortification : " + gamePlay.getCurrentPlayer().getName());
                    break;
            }
        }
    }

    /**
     * This method notify the adapter regarding data change.
     */
    public void notifyPlayScreenRVAdapter() {
        adapter.notifyDataSetChanged();
    }

    /**
     * This method is used to display given using snackbar
     * @param message : main message to be displayed
     */
    public void displaySnackBar(String message){
        final Snackbar make = Snackbar.make(cardView, message, Snackbar.LENGTH_LONG);
        make.show();
    }


    /**
     * This method is used to display the data using alert dialog
     * @param title title message to be displayed
     * @param message main message to be displayed
     */
    public void displayAlert(final String title, final String message){
        new AlertDialog.Builder(this)
                .setTitle(title).setMessage(message)
                .setPositiveButton("Ok",null).create().show();
    }

    /**
     * This method handles the back button listener and
     * confirms if user wants to exit or not
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Alert").setMessage("Do you want to exit the game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(PlayScreenActivity.this,MainScreenActivity.class));
                    }
                })
                .setNegativeButton("No",null)
                .create().show();

    }

    @Override
    public void update(Observable observable, Object o) {
       if(observable instanceof Log)
       {
           String message=((Log)observable).getMessage();
           logViewArrayList.add(0,message);
           logViewAdapter.notifyDataSetChanged();
       }
    }
}
