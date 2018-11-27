package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.adapters.PlayScreenRVAdapter;
import com.app.risk.adapters.PlayerStateAdapter;
import com.app.risk.constants.FileConstants;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.CardExchangeController;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.controller.SaveLoadGameController;
import com.app.risk.controller.StartupPhaseController;
import com.app.risk.model.Country;
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
 *
 * @author Himanshu Kohli
 * @version 1.0.0
 */
public class PlayScreenActivity extends AppCompatActivity implements Observer {

    private ImageView pImage;
    private TextView pName, pArmies, pCountries;
    private RecyclerView recyclerView;
    private CardView cardView;
    private GamePlay gamePlay;
    private PlayScreenRVAdapter adapter;
    private String playType;
    private String mapName;
    private ArrayList<String> playerNames;
    private ArrayList<String> playerStrategies;
    private ActionBar actionBar;
    private FloatingActionButton floatingActionButton;
    private ListView logView;
    public static ArrayAdapter<String> logViewAdapter;
    public static ArrayList<String> logViewArrayList;
    private PlayerStateAdapter playerStateAdapter;
    private ListView listPlayerState ;
    private ArrayList<Country> countriesOwnedByPlayer;


    /**
     * This method is the main creation method of the activity
     *
     * @param savedInstanceState: instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        LogManager.getInstance(this.getFilesDir() + File.separator + FileConstants.LOG_FILE_PATH, this).readLog();
        logView = findViewById(R.id.activity_play_screen_logview);
        logViewArrayList = new ArrayList<>();
        logViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logViewArrayList);
        logView.setAdapter(logViewAdapter);

        actionBar = getSupportActionBar();
        manageFloatingButtonTransitions();
        init();
    }

    /**
     * This method initalizes and listens the evenets of the floating Button
     */
    private void manageFloatingButtonTransitions() {
        floatingActionButton = findViewById(R.id.activity_play_screen_floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (gamePlay.getCurrentPhase()){

                    case GamePlayConstants.REINFORCEMENT_PHASE:
                        LogManager.getInstance().writeLog(gamePlay.getCurrentPlayer().getName() + " has decided to claim his cards.");
                        if (gamePlay.getCurrentPlayer().getCards().size() > 0 && !gamePlay.getCurrentPlayer().isCardsExchangedInRound()) {
                            CardExchangeDialog cardExchangeDialog = new CardExchangeDialog(PlayScreenActivity.this, CardExchangeController.getInstance().init(gamePlay.getCurrentPlayer()));
                            cardExchangeDialog.setContentView(R.layout.card_exchange);
                            cardExchangeDialog.setCancelable(false);
                            cardExchangeDialog.show();
                        } else if (gamePlay.getCurrentPlayer().getCards().size() == 0) {
                            displayAlert("No cards", "No cards to show.");
                        } else if (gamePlay.getCurrentPlayer().isCardsExchangedInRound()) {
                            displayAlert("Exchanged", "Cards have already been exchanged for this round.");
                        }
                        break;

                    case GamePlayConstants.ATTACK_PHASE:
                        LogManager.getInstance().writeLog(gamePlay.getCurrentPlayer().getName() + " has decided to move to " + GamePlayConstants.FORTIFICATION_PHASE + " phase.");
                        changePhase(GamePlayConstants.FORTIFICATION_PHASE);
                        break;

                    case GamePlayConstants.FORTIFICATION_PHASE:
                        if (gamePlay.getCurrentPlayer().isNewCountryConquered()) {
                            gamePlay.getCurrentPlayer().assignCards(gamePlay);
                            gamePlay.getCurrentPlayer().setNewCountryConquered(false);
                        }
                        LogManager.getInstance().writeLog(gamePlay.getCurrentPlayer().getName() + " has decided to move to " + GamePlayConstants.REINFORCEMENT_PHASE + " phase.");
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
        pImage = findViewById(R.id.play_screen_image);
        pName = findViewById(R.id.play_screen_player_name);
        pCountries = findViewById(R.id.play_screen_territories);
        pArmies = findViewById(R.id.play_screen_armies);
        cardView = findViewById(R.id.play_screen_cardview);
        recyclerView = findViewById(R.id.play_screen_reyclerview);

        final LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        if (((GamePlay) intent.getSerializableExtra("GAMEPLAY_OBJECT")) == null) {
            mapName = intent.getStringExtra("MAP_NAME");
            playerNames = intent.getStringArrayListExtra("PLAYER_INFO");
            playerStrategies = intent.getStringArrayListExtra("STRATERGY_INFO");
            playType = intent.getStringExtra("PLAY_TYPE");
            startGame();
            addObserversToPlayer();
        } else {
            gamePlay = (GamePlay) intent.getSerializableExtra("GAMEPLAY_OBJECT");
            changePhase(gamePlay.getCurrentPhase());
        }
    }

    /**
     * This method invoker method of start game i.e. startup method
     */
    public void startGame() {
        changePhase(GamePlayConstants.STARTUP_PHASE);
    }

    /**
     * This method handles the phase transition of the gameplay
     * @param phase The name of the phase of type string.
     */
    public void changePhase(final String phase) {
        if (phase != null) {
            switch (phase) {
                case GamePlayConstants.STARTUP_PHASE:
                    gamePlay = MapReader.returnGamePlayFromFile(this.getApplicationContext(), mapName);
                    gamePlay.setCards();
                    gamePlay.setCurrentPhase(GamePlayConstants.STARTUP_PHASE);
                    StartupPhaseController.getInstance().init(gamePlay).start(playerNames, playerStrategies);
                    changePhase(GamePlayConstants.REINFORCEMENT_PHASE);
                    break;

                case GamePlayConstants.REINFORCEMENT_PHASE:
                    GamePlayConstants.PHASE_IN_PROGRESS = false;
                    LogManager.getInstance().deleteLog();
                    floatingActionButton.setImageResource(R.drawable.ic_card_white_24dp);
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);
                    gamePlay.setCurrentPhase(GamePlayConstants.REINFORCEMENT_PHASE);
                    gamePlay.setCurrentPlayer();

                    LogManager.getInstance().writeLog("\nPlayer Name : " + gamePlay.getCurrentPlayer().getName());
                    LogManager.getInstance().writeLog("\nPhase : " + phase);

                    ReinforcementPhaseController.getInstance().init(this, gamePlay).start();
                    countriesOwnedByPlayer = gamePlay.getCountryListByPlayerId(gamePlay.getCurrentPlayer().getId());

                    adapter = new PlayScreenRVAdapter(this, gamePlay, countriesOwnedByPlayer);
                    recyclerView.setAdapter(adapter);
                    pName.setText(gamePlay.getCurrentPlayer().getName());
                    pArmies.setText("" + gamePlay.getCurrentPlayer().getNoOfArmies());
                    pCountries.setText("" + gamePlay.getCurrentPlayer().getNoOfCountries());

                    final Player currentPlayer = gamePlay.getCurrentPlayer();
                    final String message = String.format(GamePlayConstants.REINFORCEMENT_MSG,
                            currentPlayer.getNoOfArmies() - currentPlayer.getReinforcementArmies(),
                            currentPlayer.getNoOfCountries(),
                            currentPlayer.getName(),
                            currentPlayer.getReinforcementArmies());
                    displayAlert("", message);

                    final ArrayList<Player> arrPlayer = new ArrayList<>(gamePlay.getPlayers().values());
                    playerStateAdapter = new PlayerStateAdapter(arrPlayer,gamePlay,this);

                    listPlayerState = findViewById(R.id.list_play_view);
                    listPlayerState.setAdapter(playerStateAdapter);

                    if(!gamePlay.getCurrentPlayer().isHuman()){
                        gamePlay.getCurrentPlayer().reinforcementPhase(gamePlay, countriesOwnedByPlayer, null);
                    }

                    break;

                case GamePlayConstants.ATTACK_PHASE:
                    GamePlayConstants.PHASE_IN_PROGRESS = false;
                    gamePlay.getCurrentPlayer().setCardsExchangedInRound(false);
                    floatingActionButton.setImageResource(R.drawable.ic_shield_24dp);
                    gamePlay.setCurrentPhase(phase);
                    AttackPhaseController.getInstance().init(this, gamePlay);
                    LogManager.getInstance().writeLog("\nPhase : " + phase);
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);

                    if(!gamePlay.getCurrentPlayer().isHuman()){
                        gamePlay.getCurrentPlayer().attackPhase(gamePlay, countriesOwnedByPlayer, null, null);
                    }

                    break;

                case GamePlayConstants.FORTIFICATION_PHASE:
                    GamePlayConstants.PHASE_IN_PROGRESS = false;
                    floatingActionButton.setImageResource(R.drawable.ic_armies_add_24dp);
                    gamePlay.setCurrentPhase(phase);
                    FortificationPhaseController.getInstance().init(this, gamePlay);
                    LogManager.getInstance().writeLog("\nPhase : " + phase);
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);

                    if(!gamePlay.getCurrentPlayer().isHuman()){
                        gamePlay.getCurrentPlayer().fortificationPhase(gamePlay, countriesOwnedByPlayer, null);
                    }

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
     * This method is used to display the data using alert dialog
     *
     * @param title   title message to be displayed
     * @param message main message to be displayed
     */
    public void displayAlert(final String title, final String message) {
        new AlertDialog.Builder(this)
                .setTitle(title).setMessage(message)
                .setPositiveButton("Ok", null).create().show();
    }

    /**
     * This method handles the back button listener and
     * confirms if user wants to exit or not
     */
    @Override
    public void onBackPressed() {

        AlertDialog.Builder backAlertDialogBox = new AlertDialog.Builder(this);

        backAlertDialogBox
                .setTitle("Alert").setMessage("What would you like to do?")
                .setPositiveButton("Exit Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(PlayScreenActivity.this, MainScreenActivity.class));
                    }
                })
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Save Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder nameOfGameADB = new AlertDialog.Builder(PlayScreenActivity.this);
                        nameOfGameADB.setTitle("Name of game");
                        nameOfGameADB.setMessage("Enter the name of the game: ");
                        final EditText nameOfGame = new EditText(PlayScreenActivity.this);
                        nameOfGame.setInputType(InputType.TYPE_CLASS_TEXT);
                        nameOfGameADB.setView(nameOfGame);
                        nameOfGameADB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SaveLoadGameController.saveGame(gamePlay, nameOfGame.getText().toString(), PlayScreenActivity.this);
                                new AlertDialog.Builder(PlayScreenActivity.this)
                                        .setTitle("Saved")
                                        .setMessage("Your game has been saved.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                startActivity(new Intent(PlayScreenActivity.this, MainScreenActivity.class));
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();

                            }
                        });
                        nameOfGameADB.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        nameOfGameADB.show();
                    }
                });

        AlertDialog backAlertDialog = backAlertDialogBox.show();
        Button saveGameButton = backAlertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        if (GamePlayConstants.PHASE_IN_PROGRESS) {
            saveGameButton.setEnabled(false);
        }
    }

    /**
     * This method is called when ever observable model is changed
     * and these changes are notified to the observer with the changes made
     *
     * @param observable model class that has its data changed
     * @param object     value that is changed of type Object
     */
    @Override
    public void update(Observable observable, Object object) {
        if (observable instanceof Log) {
            String message = ((Log) observable).getMessage();
            logViewArrayList.add(0, message);
            logViewAdapter.notifyDataSetChanged();
        } else if (observable instanceof Player) {
            playerStateAdapter.notifyDataSetChanged();
            pArmies.setText("" + ((Player) observable).getNoOfArmies());
            pCountries.setText("" + ((Player) observable).getNoOfCountries());
        }
    }

    /**
     * Bind each player with the observer object
     */
    public void addObserversToPlayer() {
        for (Player player : gamePlay.getPlayers().values()) {
            player.addObserver(this);
        }
    }
}
