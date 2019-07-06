package com.app.risk.java.com.app.risk.controller;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.GamePlayConstants;
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
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * This class validates the reinforcement phase
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class ReinforcementPhaseControllerTest {
    /**
     * Gameplay object
     */
    GamePlay gamePlay;
    /**
     * Context object
     */
    Context context=null;
    /**
     * Startupphasecontroller object
     */
    StartupPhaseController startupphase=null;
    /**
     * Reinforcementphasecontrolled object
     */
    ReinforcementPhaseController reinforcementPhaseController=null;
    /**
     * playername list
     */
    ArrayList<String> playerNames=new ArrayList<String>();

    /**
     * This method gets executed before the test case
     * sets the gameplay instance with the values required for the testing
     * and sets the context of the test case
     */
    @Before
    public void setUp() {
        gamePlay=new GamePlay();
        context = InstrumentationRegistry.getTargetContext();
        playerNames.add("player1");
        playerNames.add("player2");
        playerNames.add("player3");
        ArrayList<String> strategy=new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        gamePlay.setPlayers(playerNames,strategy);
        HashMap<String,Country> countries=new HashMap<String,Country>();
        countries.put("India",new Country("India",new Continent("Asia",2)));
        countries.put("Italy",new Country("Italy",new Continent("europe",3)));
        countries.put("America",new Country("America",new Continent("USA",4)));
        countries.put("pakistan",new Country("pakistan",new Continent("Asia",2)));
        countries.put("nepal",new Country("nepal",new Continent("europe",3)));
        countries.put("butan",new Country("butan",new Continent("USA",4)));
        gamePlay.setCountries(countries);
        startupphase =StartupPhaseController.getInstance().init(gamePlay);
        reinforcementPhaseController=ReinforcementPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(),gamePlay);
        PhaseViewController.getInstance().init(context);
    }
    /**
     * This method checks the army count in the reinforcement phase
     */
    @Test
    public void reinforcementArmiesCountTest() {
        startupphase.assignInitialCountries();
        startupphase.placeInitialArmies();
        gamePlay.setCurrentPlayer();
        assertTrue(gamePlay.getCurrentPlayer().calculateReinforcementArmies(gamePlay)==3);


    }

    /**
     * This method checks the army count after the player has occupied the whole continent
     */
    @Test
    public void reinforcementArmyCountAfterContinentOccupyTest() {
        int currentPlayerId=0;
        HashMap<String,Country> countries=new HashMap<String,Country>();
        Continent asia=new Continent("Asia",5);
        Continent europe=new Continent("europe",3);
        Continent usa=new Continent("USA",4);
        Country india =new Country("India",asia);
        Country italy =new Country("Italy",europe);
        Country america =new Country("America",usa);
        Country pakistan =new Country("pakistan",asia);
        Country nepal =new Country("nepal",europe);
        Country butan =new Country("butan",usa);
        asia.setCountries(india);
        asia.setCountries(pakistan);
        europe.setCountries(italy);
        europe.setCountries(nepal);
        usa.setCountries(america);
        usa.setCountries(butan);
        HashMap<String, Continent> continents=new HashMap<String, Continent>();
        continents.put("Asia",asia);
        continents.put("europe",europe);
        continents.put("USA",usa);
        gamePlay.setContinents(continents);

        countries.put("India",india);
        countries.put("Italy",italy);
        countries.put("America",america);
        countries.put("pakistan",pakistan);
        countries.put("nepal",nepal);
        countries.put("butan",butan);
        gamePlay.setCountries(countries);
        startupphase =StartupPhaseController.getInstance().init(gamePlay);
        Set<Integer> playerset=gamePlay.getPlayers().keySet();
        for(int playerId:playerset)
        {

            if(gamePlay.getPlayers().get(playerId).getName().equalsIgnoreCase("player1"))
            {
                currentPlayerId=playerId;
            }
        }
        gamePlay.getCountries().get("India").setPlayer(gamePlay.getPlayers().get(currentPlayerId));
        gamePlay.getCountries().get("Italy").setPlayer(gamePlay.getPlayers().get(currentPlayerId+1));
        gamePlay.getCountries().get("America").setPlayer(gamePlay.getPlayers().get(currentPlayerId+1));
        gamePlay.getCountries().get("nepal").setPlayer(gamePlay.getPlayers().get(currentPlayerId+1));
        gamePlay.getCountries().get("butan").setPlayer(gamePlay.getPlayers().get(currentPlayerId+1));
        gamePlay.getPlayers().get(currentPlayerId).incrementCountries(1);
        gamePlay.getCountries().get("pakistan").setPlayer(gamePlay.getPlayers().get(currentPlayerId));
        gamePlay.getPlayers().get(currentPlayerId).incrementCountries(1);
        gamePlay.setCurrentPlayer(gamePlay.getPlayers().get(currentPlayerId));
        startupphase.assignInitialArmies();
        startupphase.placeInitialArmies();
        reinforcementPhaseController.start();
        assertTrue(gamePlay.getCurrentPlayer().getContinentValue(gamePlay)==5);
        assertTrue(gamePlay.getCurrentPlayer().getNoOfArmies()==12);


    }
    /**
     * This method gets executed after the test case has been executed
     * its sets the gameplay to null
     */
    @After
    public void cleanUp()
    {
        gamePlay=null;
    }
}
