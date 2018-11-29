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
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.app.risk.model.PhaseModel;
import com.app.risk.model.Player;
import com.app.risk.controller.PhaseViewController;
import com.app.risk.utility.MapReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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
    private String gameMode;
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
    private int noOfTurns;
    private boolean isGameEnd;

    /**
     * This method is the main creation method of the activity
     *
     * @param savedInstanceState: instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        PhaseViewController.getInstance(this.getFilesDir() + File.separator + FileConstants.LOG_FILE_PATH,this).readLog();
        logView=findViewById(R.id.activity_play_screen_logview);

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

                if(gamePlay.getCurrentPlayer().isHuman()){

                    switch (gamePlay.getCurrentPhase()){

                        case GamePlayConstants.REINFORCEMENT_PHASE:
                            PhaseViewController.getInstance().addAction(gamePlay.getCurrentPlayer().getName() + " has decided to claim his cards.");
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
                            PhaseViewController.getInstance().addAction(gamePlay.getCurrentPlayer().getName() + " has decided to move to "+GamePlayConstants.FORTIFICATION_PHASE+" phase.");
                            changePhase(GamePlayConstants.FORTIFICATION_PHASE);
                            break;

                        case GamePlayConstants.FORTIFICATION_PHASE:
                            if (gamePlay.getCurrentPlayer().isNewCountryConquered()) {
                                gamePlay.getCurrentPlayer().assignCards(gamePlay);
                                gamePlay.getCurrentPlayer().setNewCountryConquered(false);
                            }

                            PhaseViewController.getInstance().addAction(gamePlay.getCurrentPlayer().getName() + " has decided to move to "+GamePlayConstants.REINFORCEMENT_PHASE+" phase.");
                            changePhase(GamePlayConstants.REINFORCEMENT_PHASE);
                            break;
                    }
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

        gameMode = intent.getStringExtra(GamePlayConstants.GAME_MODE);

        if(GamePlayConstants.SINGLE_GAME_MODE.equalsIgnoreCase(gameMode)) {
            singleGameMode(intent);
        } else {
            tournamentMode(intent);
        }

    }

    public void singleGameMode(final Intent intent) {
        if (((GamePlay) intent.getSerializableExtra("GAMEPLAY_OBJECT")) == null) {
            mapName = intent.getStringExtra("MAP_NAME");
            playerNames = intent.getStringArrayListExtra("PLAYER_INFO");
            playerStrategies = intent.getStringArrayListExtra("PLAYER_STRATERGIES");
            startGame(GamePlayConstants.STARTUP_PHASE);
        } else {
            gamePlay = (GamePlay) intent.getSerializableExtra("GAMEPLAY_OBJECT");
            addObserversToPlayer();
            startGame(gamePlay.getCurrentPhase());
        }
    }

    public void tournamentMode(final Intent intent) {
        final ArrayList<String> maps = intent.getStringArrayListExtra("MAPS");
        playerStrategies = intent.getStringArrayListExtra("PLAYER_STRATERGIES");
        final int noOfGames = intent.getIntExtra("NO_OF_GAMES", 1);
        noOfTurns = intent.getIntExtra("MAX_TURNS", 10);

        final LinkedHashMap<String, ArrayList<String>> tournamentResult = new LinkedHashMap<>();

        for(final String map : maps) {
            final ArrayList<String> winningPlayers = new ArrayList<>();
            for(int i = 1; i <= noOfGames; i++) {
                isGameEnd = false;
                mapName = map;
                playerNames = getPlayerNames(playerStrategies);
                startGame(GamePlayConstants.STARTUP_PHASE);

//                while(!isGameEnd){
//                    System.out.println("Khana bana le .....................................");
//                }
                if(gamePlay.getNoOfTurns() == 0){
                    winningPlayers.add("Draw");
                } else {
                    winningPlayers.add(gamePlay.getCurrentPlayer().getName());
                }
            }
            tournamentResult.put(map, winningPlayers);
        }

        showTournamentResult(tournamentResult,noOfGames);
    }

    public void showTournamentResult(final LinkedHashMap<String,ArrayList<String>> tournamentResult,final int noOfGames){

        final ArrayList<String> gridViewArrayList = new ArrayList<>();
         gridViewArrayList.add("");
        for(int i=1;i<=noOfGames;i++){
            gridViewArrayList.add("Game " + i);
        }

        for(Map.Entry<String,ArrayList<String>> entry : tournamentResult.entrySet()){
            gridViewArrayList.add(entry.getKey());
            for(String winnerName : entry.getValue()){
                gridViewArrayList.add(winnerName);
            }
        }

        final View view = getLayoutInflater().inflate(R.layout.final_result_dialog, null);
        GridView gridView = view.findViewById(R.id.final_result_dialog_gridview);
        gridView.setNumColumns(noOfGames + 1);
        gridView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gridViewArrayList));

        new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Results")
                .setPositiveButton("Ok",null)
                .create().show();

    }

    public ArrayList<String> getPlayerNames(final ArrayList<String> playerStrategies){
        final ArrayList<String> playerNames = new ArrayList<>();
        for(final String strategy : playerStrategies){
            playerNames.add(strategy.split(" ")[0]);
        }
        return playerNames;
    }

    /**
     * This method invoker method of start game i.e. startup method
     */
    public void startGame(final String phaseName) {
        changePhase(phaseName);
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
                    addObserversToPlayer();
                    gamePlay.setNoOfTurns(noOfTurns);
                    gamePlay.setCurrentPhase(GamePlayConstants.STARTUP_PHASE);
                    StartupPhaseController.getInstance().init(gamePlay).start(playerNames, playerStrategies);
                    changePhase(GamePlayConstants.REINFORCEMENT_PHASE);
                    break;

                case GamePlayConstants.REINFORCEMENT_PHASE:
                    if(GamePlayConstants.TOURNAMENT_MODE.equalsIgnoreCase(gameMode) && gamePlay.getNoOfTurns() == 0){
                        isGameEnd = true;
                    } else {
                        GamePlayConstants.PHASE_IN_PROGRESS = false;
                        PhaseViewController.getInstance().clearPhaseView();

                        floatingActionButton.setImageResource(R.drawable.ic_card_white_24dp);
                        actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);
                        gamePlay.setCurrentPhase(GamePlayConstants.REINFORCEMENT_PHASE);
                        gamePlay.setCurrentPlayer();

                        PhaseViewController.getInstance().addAction("\nPlayer Name : " + gamePlay.getCurrentPlayer().getName());
                        PhaseViewController.getInstance().addAction("\nPhase : " + phase);

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

                        final ArrayList<Player> arrPlayer = new ArrayList<>(gamePlay.getPlayers().values());
                        playerStateAdapter = new PlayerStateAdapter(arrPlayer,gamePlay,this);

                        listPlayerState = findViewById(R.id.list_play_view);
                        listPlayerState.setAdapter(playerStateAdapter);

                        if(!gamePlay.getCurrentPlayer().isHuman()){
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            gamePlay.getCurrentPlayer().reinforcementPhase(gamePlay, countriesOwnedByPlayer, null);
                            sleep(GamePlayConstants.SLEEP_TIME, GamePlayConstants.ATTACK_PHASE);
                        } else {
                            displayAlert("", message);
                        }
                    }

                    break;

                case GamePlayConstants.ATTACK_PHASE:
                    GamePlayConstants.PHASE_IN_PROGRESS = false;
                    gamePlay.getCurrentPlayer().setCardsExchangedInRound(false);
                    floatingActionButton.setImageResource(R.drawable.ic_shield_24dp);
                    gamePlay.setCurrentPhase(phase);

                    AttackPhaseController.getInstance().init(this, gamePlay);

                    PhaseViewController.getInstance().addAction("\nPhase : " + phase);
                    actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);

                    if(!gamePlay.getCurrentPlayer().isHuman()){
                        gamePlay.getCurrentPlayer().attackPhase(gamePlay, countriesOwnedByPlayer, null, null);
                        sleep(GamePlayConstants.SLEEP_TIME, GamePlayConstants.FORTIFICATION_PHASE);
                    }

                    break;

                case GamePlayConstants.FORTIFICATION_PHASE:
                    if(gamePlay.getCurrentPlayer().isPlayerWon()) {
                        Toast.makeText(this, gamePlay.getCurrentPlayer().getName() + " won the game", Toast.LENGTH_LONG).show();
                        if(GamePlayConstants.SINGLE_GAME_MODE.equalsIgnoreCase(gameMode)){
                            new AlertDialog.Builder(this)
                                    .setMessage("You won the game!!!")
                                    .setNeutralButton( "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(PlayScreenActivity.this, MainScreenActivity.class));
                                        }
                                    })
                                    .setTitle("Congratulations " + gamePlay.getCurrentPlayer().getName() + " !!!")
                                    .setCancelable(false)
                                    .create().show();
                        } else {
                            isGameEnd = true;
                        }
                    } else {
                        GamePlayConstants.PHASE_IN_PROGRESS = false;
                        floatingActionButton.setImageResource(R.drawable.ic_armies_add_24dp);
                        gamePlay.setCurrentPhase(phase);

                        FortificationPhaseController.getInstance().init(this, gamePlay);

                        PhaseViewController.getInstance().addAction("\nPhase : " + phase);
                        actionBar.setTitle(getResources().getString(R.string.app_name) + " : " + phase);


                        if(!gamePlay.getCurrentPlayer().isHuman()){
                            gamePlay.getCurrentPlayer().fortificationPhase(gamePlay, countriesOwnedByPlayer, null);
                            sleep(GamePlayConstants.SLEEP_TIME, GamePlayConstants.REINFORCEMENT_PHASE);
                        }
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

       if(observable instanceof PhaseModel) {
           logViewArrayList.clear();
           logViewArrayList.addAll(((PhaseModel)observable).getActions());
           logViewAdapter.notifyDataSetChanged();
       } else if(observable instanceof Player) {
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

    public void sleep(final int milliseconds, final String nextPhase){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changePhase(nextPhase);
            }
        },milliseconds);
    }
}
