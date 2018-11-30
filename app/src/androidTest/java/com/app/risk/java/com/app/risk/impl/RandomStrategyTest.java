package com.app.risk.java.com.app.risk.impl;

import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.controller.StartupPhaseController;
import com.app.risk.impl.RandomPlayerStrategy;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;

/**
 * This class is used to test random strategy player behaviour in all the three phases.
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class RandomStrategyTest {

    /**
     * gamePlay instances would hold the objects required for the test cases
     */
    private GamePlay gamePlay = null;
    /**
     * startUpPhaseController would hold the instance of the StartupPhaseController required for all the test cases
     */
    StartupPhaseController startupPhaseController = null;
    /**
     * reinforcementPhaseController would hold the instance of the ReinforcementPhaseController required for the randomReinforcementTest case
     */
    ReinforcementPhaseController reinforcementPhaseController = null;
    /**
     * attackPhaseController would hold the instance of the AttackPhaseController required for the randomAttackTest case
     */
    AttackPhaseController attackPhaseController = null;
    /**
     * playerNames would hold the list of all the player names
     */
    ArrayList<String> playerNames = new ArrayList<String>();
    /**
     * Hashmap to map each country to its name with the name of the country as key
     */
    HashMap<String, Country> countries = new HashMap<String, Country>();
    /**
     * A false flag which will change to true only if there is a change in the number of armies of countries in the test cases
     */
    boolean flag;

    /**
     * This method gets executed before the test case
     * sets the gameplay instance with the values required for the testing
     * and sets the context of the test case
     */
    @Before
    public void setUp(){
        gamePlay = new GamePlay();
        flag = false;
        playerNames.add("player1");
        playerNames.add("player2");
        ArrayList<String> strategy = new ArrayList<String>();
        strategy.add(GamePlayConstants.RANDOM_STRATEGY);
        strategy.add(GamePlayConstants.RANDOM_STRATEGY);
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
        startupPhaseController = StartupPhaseController.getInstance().init(gamePlay);
    }

    /**
     * This method checks that atleast one country from all the countries that belong to the random player has been reinforced.
     */
    @Test
    public void randomReinforcementTest(){
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
        if(gamePlay.getCountries().get("India").getNoOfArmies()!=1){
            flag = true;
        }
        if(gamePlay.getCountries().get("Italy").getNoOfArmies()!=2){
            flag = true;
        }
        if(gamePlay.getCountries().get("America").getNoOfArmies()!=3){
            flag = true;
        }
        assertTrue(flag == true);
    }

    /**
     * This method checks whether an attack was completely performed on the attacking country of the random player and the defending country
     */
    @Test
    public void randomAttackTest(){
        gamePlay.getPlayers().get(0).setNoOfArmies(6);
        gamePlay.getPlayers().get(1).setNoOfArmies(4);
        gamePlay.getPlayers().get(0).setNoOfCountries(3);
        gamePlay.getPlayers().get(1).setNoOfCountries(3);
        gamePlay.getCountries().get("India").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("pakistan").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("America").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("America").setNoOfArmies(6);
        gamePlay.getCountries().get("nepal").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("butan").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("Italy").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("Italy").setNoOfArmies(4);
        gamePlay.setCurrentPlayer(gamePlay.getPlayers().get(0));
        attackPhaseController = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gamePlay);
        ((RandomPlayerStrategy) gamePlay.getCurrentPlayer().getStrategy()).performAllOutAttack(gamePlay.getCountries().get("America"),
                gamePlay.getCountries().get("Italy"), gamePlay.getCurrentPlayer(), gamePlay.getCountryListByPlayerId(gamePlay.getCurrentPlayer().getId()));
        if(gamePlay.getCountries().get("America").getPlayer()==gamePlay.getCountries().get("Italy").getPlayer()){
            assertTrue(gamePlay.getCountries().get("Italy").getNoOfArmies()<=3);
        }
        else
        {
            assertTrue(gamePlay.getCountries().get("America").getNoOfArmies()==1);
        }
    }

    /**
     * This method checks valid fortification is performed for random player
     */
    @Test
    public void randomFortificationTest(){
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
        FortificationPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gamePlay);
        gamePlay.getCurrentPlayer().fortificationPhase(gamePlay, gamePlay.getCountryListByPlayerId(0), null);
        if(gamePlay.getCountries().get("India").getNoOfArmies()!=1){
            flag = true;
        }
        if(gamePlay.getCountries().get("Italy").getNoOfArmies()!=2){
            flag = true;
        }
        if(gamePlay.getCountries().get("America").getNoOfArmies()!=3){
            flag = true;
        }
        assertTrue(flag == true);
    }

    /**
     * This method gets executed after the test case has been executed
     * its sets the gameplay to null
     */
    @After
    public void cleanUp(){
        gamePlay = null;
    }
}
