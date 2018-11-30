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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    /**
     * pImage: image of the player
     */
    private ImageView pImage;
    /**
     * pName: name of the player
     */
    private TextView pName;
    /**
     * pArmies: armies of the player
     */
    private TextView pArmies;
    /**
     * pCountries: number of player countries
     */
    private TextView pCountries;
    /**
     * recyclerView: hold and create data representation of player countries
     */
    private RecyclerView recyclerView;
    /**
     * cardView: holds the reference of a card
     */
    private CardView cardView;
    /**
     * gamePlay: To manage the state and retrieve data
     */
    private GamePlay gamePlay;
    /**
     * adapter: holds the data of play screen adapter
     */
    private PlayScreenRVAdapter adapter;
    /**
     * gameMode: holds the current game mode
     */
    private String gameMode;

    /**
     * mapName: contains the name of the map
     */
    private String mapName;

    /**
     * playerNames: contains the name of the players
     */
    private ArrayList<String> playerNames;

    /**
     * playerStrategies: contains the strategies of the player
     */
    private ArrayList<String> playerStrategies;
    /**
     * actionBar: reference to the top toolbar
     */
    private ActionBar actionBar;
    /**
     * floatingActionButton: the phase changing button
     */
    private FloatingActionButton floatingActionButton;
    /**
     * logView: phase view represented in the list view
     */
    private ListView logView;
    /**
     * logViewAdapter: holds the data of the list view
     */
    public static ArrayAdapter<String> logViewAdapter;
    /**
     * logViewArrayList: holds the data of list view
     */
    public static ArrayList<String> logViewArrayList;
    /**
     * playerStateAdapter: holds the state of the player
     */
    private PlayerStateAdapter playerStateAdapter;

    /**
     * mapList: list of all the maps
     */
    private ArrayList<String> mapList;

    /**
     * listPlayerState: holds the list of each player(world domination view)
     */
    private ListView listPlayerState ;
    /**
     * countriesOwnedByPlayer: holds all the countries hold by player
     */
    private ArrayList<Country> countriesOwnedByPlayer;
    /**
     * noOfTurns: restrictive turns in tournament mode for each player
     */

    private int noOfTurns;

    /**
     * noOfGames: number of games in tournament mode
     */
    private int noOfGames;
    /**
     * currentGameCount: holds the current count of game
     */
    private int currentGameCount;
    /**
     * tournamentResult: holds the results of the tournament mode
     */
    private LinkedHashMap<String, ArrayList<String>> tournamentResult = new LinkedHashMap<>();
    /**
     * winningPlayers: holds the data of winning players in games
     */
    private ArrayList<String> winningPlayers;

    /**
     * This method is the main creation method of the activity
     *
     * @param savedInstanceState: instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        PhaseViewController.getInstance().init(this.getFilesDir() + File.separator + FileConstants.LOG_FILE_PATH,this);
        logView=findViewById(R.id.activity_play_screen_logview);

        logViewArrayList = new ArrayList<>();
        logViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logViewArrayList);
        logView.setAdapter(logViewAdapter);

        manageLogViewDialog();

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
                                CardExchangeDialog cardExchangeDialog = new CardExchangeDialog(PlayScreenActivity.this, gamePlay.getCurrentPlayer());
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
        mapList = intent.getStringArrayListExtra("MAPS");
        playerStrategies = intent.getStringArrayListExtra("PLAYER_STRATERGIES");
        noOfGames = intent.getIntExtra("NO_OF_GAMES", 1);
        noOfTurns = intent.getIntExtra("MAX_TURNS", 10);

        winningPlayers = new ArrayList<>();
        startTournament();
    }

    public void startTournament(){
        if(currentGameCount < noOfGames) {
            ++currentGameCount;
        } else {
            currentGameCount = 1;
            tournamentResult.put(mapName, winningPlayers);
            winningPlayers = new ArrayList<String>();
            mapList.remove(0);
        }
        if(mapList.size() != 0){
            mapName = mapList.get(0);
            playerNames = getPlayerNames(playerStrategies);
            startGame(GamePlayConstants.STARTUP_PHASE);
        } else {
            showTournamentResult(tournamentResult, noOfGames);
        }
    }

    public void showTournamentResult(final LinkedHashMap<String, ArrayList<String>> tournamentResult, final int noOfGames){

        final ArrayList<String> gridViewArrayList = new ArrayList<>();

        for(int i = 0; i < noOfGames; i++){
            StringBuilder temp = new StringBuilder();
            temp.append("Game " + (i+1) + ":\n");
            for(Map.Entry<String,ArrayList<String>> entry : tournamentResult.entrySet()){
                temp.append(entry.getKey() + " : " + entry.getValue().get(i) + "\n");
            }
            gridViewArrayList.add(temp.toString());
        }

        String[] gridViewArray = new String[gridViewArrayList.size()];

        for(int i=0;i<gridViewArrayList.size();i++){
            gridViewArray[i] = gridViewArrayList.get(i);
        }

        new AlertDialog.Builder(this)
                .setItems(gridViewArray,null)
                .setTitle("Tournament Result")
                .setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performExitGame();
                    }
                })
                .setNeutralButton("Show Log", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLogDialog();
                    }
                })
                .setCancelable(false)
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
                    MapReader mapReader = new MapReader();
                    gamePlay = mapReader.returnGamePlayFromFile(this.getApplicationContext(), mapName);
                    gamePlay.setCards();
                    gamePlay.setNoOfTurns(noOfTurns);
                    gamePlay.setCurrentPhase(GamePlayConstants.STARTUP_PHASE);
                    StartupPhaseController.getInstance().init(gamePlay).start(playerNames, playerStrategies);
                    addObserversToPlayer();
                    changePhase(GamePlayConstants.REINFORCEMENT_PHASE);
                    break;

                case GamePlayConstants.REINFORCEMENT_PHASE:
                    if(GamePlayConstants.TOURNAMENT_MODE.equalsIgnoreCase(gameMode) && gamePlay.getNoOfTurns() == 0){
                        winningPlayers.add("Draw");
                        startTournament();
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
                                            performExitGame();
                                        }
                                    })
                                    .setTitle("Congratulations " + gamePlay.getCurrentPlayer().getName() + " !!!")
                                    .setCancelable(false)
                                    .create().show();
                        } else {
                            winningPlayers.add(gamePlay.getCurrentPlayer().getName());
                            startTournament();
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
                        performExitGame();
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
                                                performExitGame();
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
        if(gamePlay.getCurrentPlayer().isHuman()) {
            if (GamePlayConstants.PHASE_IN_PROGRESS) {
                saveGameButton.setEnabled(false);
            }
        } else {
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
           if(playerStateAdapter != null){
               playerStateAdapter.notifyDataSetChanged();
           }
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

    /**
     * This method shows the dialog box of the phase view
     */
    public void manageLogViewDialog(){

       logView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               if(gamePlay.getCurrentPlayer().isHuman()){
                   String[] logViewArray = new String[logViewArrayList.size()];

                   for(int i=0;i<logViewArrayList.size();i++){
                       logViewArray[i] = logViewArrayList.get(i);
                   }
                   new AlertDialog.Builder(PlayScreenActivity.this)
                           .setItems(logViewArray,null)
                           .setTitle("Phase View for " + gamePlay.getCurrentPlayer().getName())
                           .setPositiveButton("Back",null)
                           .create().show();
               }
           }
       });
    }

    /**
     * {@inheritDoc}
     * @param menu: reference to menu
     * @return : returns who is to handle the menu event (1:system)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.play_screen_menu,menu);
        return true;
    }

    /**
     *{@inheritDoc}
     * @param item: reference to menu item
     * @return : returns who is to handle the menu event (1:user)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.play_action_view){

            showLogDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows the alert dialog for the full log of system
     */
    public void showLogDialog(){
        ArrayList<String> logViewList = PhaseViewController.getInstance().readLog(this);
        String[] logViewArray = new String[logViewList.size()];
        Toast.makeText(this, ""
                +logViewArray.length, Toast.LENGTH_SHORT).show();
        for(int i=0;i<logViewList.size();i++){
            logViewArray[i] = logViewList.get(i);
        }
        new AlertDialog.Builder(PlayScreenActivity.this)
                .setItems(logViewArray,null)
                .setTitle("Phase View for " + gamePlay.getCurrentPlayer().getName())
                .setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performExitGame();
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    /**
     * Exits the game
     */
    public void performExitGame(){
        Intent intent = new Intent(PlayScreenActivity.this, MainScreenActivity.class);
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
