package com.app.risk.java.com.app.risk.impl;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.FortificationPhaseController;
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

import static org.junit.Assert.assertTrue;

public class AggresiveStrategyTest {
    /**
     * Game play object which stores information of players,countries contients
     */
    GamePlay gamePlay;
    /**
     * Context object to be used in while invoking diffrent controllers
     */
    Context context = null;
    /**
     *  instance of Startupphasecontroller which manages all start up related activities
     */
    StartupPhaseController startupphase = null;
    /**
     * instance of Reinforcementphasecontroller which manages all reinforcement related activites
     */
    ReinforcementPhaseController reinforcementPhaseController = null;
    /**
     * list of player name objects
     */
    ArrayList<String> playerNames = new ArrayList<String>();
    /**
     * hashmap to store country name and their corresponding country
     */
    HashMap<String, Country> countries = new HashMap<String, Country>();

    /**
     * Sets up all variables to be used for testing
     */
    @Before
    public void setUp(){
        gamePlay = new GamePlay();
        context = InstrumentationRegistry.getTargetContext();
        playerNames.add("player1");
        playerNames.add("player2");
        ArrayList<String> strategy = new ArrayList<String>();
        strategy.add(GamePlayConstants.AGGRESSIVE_STRATEGY);
        strategy.add(GamePlayConstants.AGGRESSIVE_STRATEGY);
        gamePlay.setPlayers(playerNames, strategy);
        ArrayList<String> india = new ArrayList<String>();
        india.add("Italy");
        india.add("America");
        ArrayList<String> italy = new ArrayList<String>();
        italy.add("India");
        italy.add("America");
        ArrayList<String> america = new ArrayList<String>();
        america.add("India");
        america.add("Italy");
        countries.put("India", new Country("India", new Continent("Asia", 2)));
        countries.put("Italy", new Country("Italy", new Continent("europe", 3)));
        countries.put("America", new Country("America", new Continent("USA", 4)));
        countries.put("pakistan", new Country("pakistan", new Continent("Asia", 2)));
        countries.put("nepal", new Country("nepal", new Continent("europe", 3)));
        countries.put("butan", new Country("butan", new Continent("USA", 4)));
        gamePlay.setCountries(countries);
        gamePlay.getCountries().get("India").setAdjacentCountries(india);
        gamePlay.getCountries().get("Italy").setAdjacentCountries(italy);
        gamePlay.getCountries().get("America").setAdjacentCountries(america);
        startupphase = StartupPhaseController.getInstance().init(gamePlay);
    }

    /**
     * Checks aggresive reinforcement functionality
     */
    @Test
    public void aggresiveReinforcementArmiesCountTest(){
        gamePlay.getPlayers().get(0).setNoOfArmies(6);
        gamePlay.getPlayers().get(0).setNoOfCountries(3);
        gamePlay.getCountries().get("India").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("India").setNoOfArmies(1);
        gamePlay.getCountries().get("Italy").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("Italy").setNoOfArmies(2);
        gamePlay.getCountries().get("America").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("America").setNoOfArmies(3);
        gamePlay.getCountries().get("pakistan").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("nepal").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("butan").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.setCurrentPlayer(gamePlay.getPlayers().get(0));
        reinforcementPhaseController = ReinforcementPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gamePlay);
        reinforcementPhaseController.start();
        int reinforcement = gamePlay.getCurrentPlayer().calculateReinforcementArmies(gamePlay);
        System.out.println(reinforcement);
        gamePlay.getCurrentPlayer().reinforcementPhase(gamePlay, gamePlay.getCountryListByPlayerId(0), null);
        assertTrue(gamePlay.getCountries().get("America").getNoOfArmies() == reinforcement + 3);
        System.out.println(reinforcement);
    }

    /**
     * Checks aggresive attack functionality
     */
    @Test
    public void aggresiveAttackTest(){
        startupphase.assignInitialCountries();
        startupphase.assignInitialArmies();
        startupphase.placeInitialArmies();
        gamePlay.setCurrentPlayer(gamePlay.getPlayers().get(0));
        reinforcementPhaseController = ReinforcementPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gamePlay);
        reinforcementPhaseController.start();
        gamePlay.getCurrentPlayer().reinforcementPhase(gamePlay, gamePlay.getCountryListByPlayerId(0), null);
        AttackPhaseController fc = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gamePlay);
        int beforeAttackArmies = gamePlay.getCountryListByPlayerId(0).get(0).getNoOfArmies();
        gamePlay.getCurrentPlayer().attackPhase(gamePlay,gamePlay.getCountryListByPlayerId(0),gamePlay.getCountryListByPlayerId(0).get(0), gamePlay.getCountryListByPlayerId(1).get(0));
        int afterAttackArmies = gamePlay.getCountryListByPlayerId(0).get(0).getNoOfArmies();
        assertTrue(afterAttackArmies == 1);
    }

    /**
     * Checks aggresive fortification functionality
     */

    @Test
    public void aggresiveFortificationTest(){
        ArrayList<String> adjacentCountries = new ArrayList<>();
        adjacentCountries.add("Italy");
        adjacentCountries.add("America");
        gamePlay.getPlayers().get(0).setNoOfArmies(6);
        gamePlay.getPlayers().get(0).setNoOfCountries(3);
        gamePlay.getCountries().get("India").setAdjacentCountries(adjacentCountries);
        gamePlay.getCountries().get("India").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("India").setNoOfArmies(1);
        gamePlay.getCountries().get("Italy").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("Italy").setNoOfArmies(2);
        gamePlay.getCountries().get("America").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("America").setNoOfArmies(3);
        gamePlay.getCountries().get("pakistan").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("nepal").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("butan").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.setCurrentPlayer(gamePlay.getPlayers().get(0));
        FortificationPhaseController.getInstance().init(context,gamePlay);
        gamePlay.getCurrentPlayer().fortificationPhase(gamePlay,gamePlay.getCountryListByPlayerId(0),null);
        int india=gamePlay.getCountries().get("India").getNoOfArmies();
        int america=gamePlay.getCountries().get("America").getNoOfArmies();
        assertTrue(gamePlay.getCountries().get("Italy").getNoOfArmies()==4);
    }

    /**
     * This method gets executed after the test case has been executed
     * its sets the gameplay to null
     */
    @After
    public void cleanUp() {
        gamePlay = null;
    }
}
