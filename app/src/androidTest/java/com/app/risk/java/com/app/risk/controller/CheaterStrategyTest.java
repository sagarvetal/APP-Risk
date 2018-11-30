package com.app.risk.java.com.app.risk.controller;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.controller.StartupPhaseController;
import com.app.risk.impl.CheaterPlayerStrategy;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.assertTrue;

public class CheaterStrategyTest {

    /**
     * context instance would hold the instance of the target activity
     */
    private Context context = null;
    /**
     * gameplay instances would hold the objects required for the test cases
     */
    private GamePlay gamePlay = null;
    /**
     * startupphase would hold the instance of the StartupPhaseController required for all the test cases
     */
    StartupPhaseController startupphase = null;
    /**
     * reinforcementPhaseController would hold the instance of the ReinforcementPhaseController required for all the test cases
     */
    ReinforcementPhaseController reinforcementPhaseController = null;
    /**
     * playerNames would hold the list of all the player names
     */
    ArrayList<String> playerNames = new ArrayList<String>();
    /**
     * countries would hold map each of the country name to the country object
     */
    HashMap<String, Country> countries = new HashMap<String, Country>();

    /**
     * This method gets executed before the test case
     * sets the gameplay instance with the values required for the testing
     * and sets the context of the test case
     */
    @Before
    public void setUp() {
        gamePlay = new GamePlay();
        context = InstrumentationRegistry.getTargetContext();
        playerNames.add("player1");
        playerNames.add("player2");
        ArrayList<String> strategy = new ArrayList<String>();
        strategy.add(GamePlayConstants.CHEATER_STRATEGY);
        strategy.add(GamePlayConstants.CHEATER_STRATEGY);
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
     * This method checks the army count doubles in the reinforcement phase for cheater player
     */
    @Test
    public void cheaterReinforcementArmiesCountTest() {
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
        int reinforcement = (gamePlay.getCountries().get("India").getNoOfArmies()) * 2;
        //gamePlay.getCurrentPlayer().calculateReinforcementArmies(gamePlay);
        System.out.println(reinforcement);
        gamePlay.getCurrentPlayer().reinforcementPhase(gamePlay, gamePlay.getCountryListByPlayerId(0), null);
        assertTrue(gamePlay.getCountries().get("India").getNoOfArmies() == reinforcement);
    }

    /**
     * This method checks whether all neighbouring country becomes his after the attack for cheater player
     */
    @Test
    public void cheaterAttackTest() {
        gamePlay.getPlayers().get(0).setNoOfArmies(6);
        gamePlay.getPlayers().get(0).setNoOfCountries(3);
        gamePlay.getCountries().get("India").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("India").setNoOfArmies(1);
        gamePlay.getCountries().get("Italy").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("Italy").setNoOfArmies(2);
        gamePlay.getCountries().get("America").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("America").setNoOfArmies(3);
        gamePlay.getCountries().get("pakistan").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("nepal").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("butan").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.setCurrentPlayer(gamePlay.getPlayers().get(0));

        gamePlay.getCurrentPlayer().getStrategy().attackPhase(gamePlay,gamePlay.getPlayers().get(0),gamePlay.getCountryListByPlayerId(0)
                ,gamePlay.getCountries().get("India")
                ,gamePlay.getCountries().get("Italy"));

        assertTrue(gamePlay.getCountries().get("India").getPlayer() == gamePlay.getCountries().get("Italy").getPlayer()
                       && gamePlay.getCountries().get("India").getPlayer() == gamePlay.getCountries().get("America").getPlayer() );
    }

    /**
     * This method checks army count doubles on each country which has enemy neighbour
     */
    @Test
    public void cheaterFortificationTest() {
        gamePlay.getPlayers().get(0).setNoOfArmies(6);
        gamePlay.getPlayers().get(0).setNoOfCountries(3);
        gamePlay.getCountries().get("India").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("India").setNoOfArmies(1);
        gamePlay.getCountries().get("Italy").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("Italy").setNoOfArmies(2);
        gamePlay.getCountries().get("America").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("America").setNoOfArmies(3);
        gamePlay.getCountries().get("pakistan").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("nepal").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("butan").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.setCurrentPlayer(gamePlay.getPlayers().get(0));

        // india and italy have america as their neighbour
        int armiesOnIndia = gamePlay.getCountries().get("India").getNoOfArmies();
        int armiesOnItaly = gamePlay.getCountries().get("Italy").getNoOfArmies();


        FortificationPhaseController.getInstance().init(context, gamePlay);
        gamePlay.getCurrentPlayer().fortificationPhase(gamePlay, gamePlay.getCountryListByPlayerId(0), null);

        assertTrue(gamePlay.getCountries().get("India").getNoOfArmies() == (armiesOnIndia * 2)
                && gamePlay.getCountries().get("Italy").getNoOfArmies() == (armiesOnItaly * 2));

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
