package com.app.risk.java.com.app.risk.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.controller.PhaseViewController;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.controller.StartupPhaseController;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertTrue;

/**
 * This class is used to test tournament mode of the game.
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class TournamentModeTest {

    private Context context = null;
    private GamePlay gamePlay1 = null;
    private GamePlay gamePlay2 = null;
    StartupPhaseController startupPhaseController = null;
    ReinforcementPhaseController reinforcementPhaseController = null;
    AttackPhaseController attackPhaseController = null;
    ArrayList<String> playerNames;
    HashMap<String, Country> countries1 = new HashMap<String, Country>();
    HashMap<String, Country> countries2 = new HashMap<String, Country>();
    boolean flag;
    ArrayList<GamePlay> mapList = null;
    int noOfGames;
    int noOfturns;
    ArrayList<String> strategy;
    LinkedHashMap<String, ArrayList<String>> tournamentResult;

    /**
     * This method gets executed before the test case
     * sets the gameplay instance with the values required for the testing
     * and sets the context of the test case
     */
    @Before
    public void setUp() {
        mapList = new ArrayList<GamePlay>();
        context = InstrumentationRegistry.getTargetContext();
        flag = false;
        gamePlay1=getgameplay1();
        gamePlay2=getgameplay2();
        mapList.add(gamePlay1);
        mapList.add(gamePlay2);
        noOfGames=2;
        noOfturns=5;
        strategy = new ArrayList<String>();
        strategy.add(GamePlayConstants.AGGRESSIVE_STRATEGY);
        strategy.add(GamePlayConstants.CHEATER_STRATEGY);
        playerNames = new ArrayList<String>();
        playerNames.add("player1");
        playerNames.add("player2");
        tournamentResult = new LinkedHashMap<>();
        PhaseViewController.getInstance().init(context);
    }

    @Test
    public void tournamentModeTest() {
        for(final GamePlay gamePlay : mapList){
            StartupPhaseController.getInstance().init(gamePlay).start(playerNames, strategy);
            ArrayList<String> winners = new ArrayList<>();
            for(int i = 1; i <= noOfGames; i++){
                gamePlay.setNoOfTurns(noOfturns);
                while(gamePlay.getNoOfTurns() != 0){
                    gamePlay.setCurrentPhase(GamePlayConstants.REINFORCEMENT_PHASE);
                    gamePlay.setCurrentPlayer();
                    ReinforcementPhaseController.getInstance().init(context, gamePlay).start();
                    ArrayList<Country> countriesOwnedByPlayer = gamePlay.getCountryListByPlayerId(gamePlay.getCurrentPlayer().getId());
                    gamePlay.getCurrentPlayer().reinforcementPhase(gamePlay, countriesOwnedByPlayer, null);

                    AttackPhaseController.getInstance().init(context, gamePlay);
                    gamePlay.getCurrentPlayer().attackPhase(gamePlay, countriesOwnedByPlayer, null, null);

                    if(gamePlay.getCurrentPlayer().isPlayerWon()){
                        winners.add(gamePlay.getCurrentPlayer().getName());
                    }

                    FortificationPhaseController.getInstance().init(context, gamePlay);
                    gamePlay.getCurrentPlayer().fortificationPhase(gamePlay, countriesOwnedByPlayer, null);
                }

                if(gamePlay.getNoOfTurns() == 0 && !gamePlay.getCurrentPlayer().isPlayerWon()){
                    winners.add("Draw");
                }

            }
            tournamentResult.put(gamePlay.getMapName(), winners);
        }
        assertTrue(tournamentResult.get(gamePlay1.getMapName()).size()==tournamentResult.get(gamePlay2.getMapName()).size());



    }

    @After
    public void cleanUp() {
        gamePlay1 = null;
        gamePlay2 = null;
    }

    GamePlay getgameplay2() {
        gamePlay2 = new GamePlay();
        gamePlay2.setMapName("Map2");
        ArrayList<String> india = new ArrayList<String>();
        india.add("pakistan");
        india.add("nepal");
        india.add("bhutan");
        ArrayList<String> bhutan = new ArrayList<String>();
        india.add("pakistan");
        india.add("India");
        ArrayList<String> montreal = new ArrayList<String>();
        india.add("nepal");
        india.add("Italy");
        ArrayList<String> pakistan = new ArrayList<String>();
        pakistan.add("India");
        pakistan.add("bhutan");
        ArrayList<String> italy = new ArrayList<String>();
        italy.add("nepal");
        italy.add("montreal");
        ArrayList<String> nepal = new ArrayList<String>();
        nepal.add("India");
        nepal.add("Italy");
        nepal.add("montreal");
        countries2.put("India", new Country("India", new Continent("Asia", 2)));
        countries2.put("bhutan", new Country("bhutan", new Continent("Asia", 2)));
        countries2.put("Italy", new Country("Italy", new Continent("europe", 3)));
        countries2.put("pakistan", new Country("pakistan", new Continent("Asia", 2)));
        countries2.put("nepal", new Country("nepal", new Continent("europe", 3)));
        countries2.put("montreal", new Country("montreal", new Continent("europe", 3)));
        gamePlay2.setCountries(countries2);
        gamePlay2.getCountries().get("India").setAdjacentCountries(india);
        gamePlay2.getCountries().get("pakistan").setAdjacentCountries(pakistan);
        gamePlay2.getCountries().get("nepal").setAdjacentCountries(nepal);
        gamePlay2.getCountries().get("Italy").setAdjacentCountries(italy);
        gamePlay2.getCountries().get("bhutan").setAdjacentCountries(bhutan);
        gamePlay2.getCountries().get("montreal").setAdjacentCountries(montreal);
        return gamePlay2;
    }
    GamePlay getgameplay1()
    {
        gamePlay1 = new GamePlay();
        gamePlay1.setMapName("Map1");
        ArrayList<String> india = new ArrayList<String>();
        india.add("pakistan");
        india.add("nepal");
        ArrayList<String> pakistan = new ArrayList<String>();
        pakistan.add("India");
        ArrayList<String> italy = new ArrayList<String>();
        italy.add("nepal");
        ArrayList<String> nepal = new ArrayList<String>();
        nepal.add("India");
        nepal.add("Italy");
        countries1.put("India", new Country("India", new Continent("Asia", 2)));
        countries1.put("Italy", new Country("Italy", new Continent("europe", 3)));
        countries1.put("pakistan", new Country("pakistan", new Continent("Asia", 2)));
        countries1.put("nepal", new Country("nepal", new Continent("europe", 3)));
        gamePlay1.setCountries(countries1);
        gamePlay1.getCountries().get("India").setAdjacentCountries(india);
        gamePlay1.getCountries().get("pakistan").setAdjacentCountries(pakistan);
        gamePlay1.getCountries().get("nepal").setAdjacentCountries(nepal);
        gamePlay1.getCountries().get("Italy").setAdjacentCountries(italy);
        return gamePlay1;
    }
    }