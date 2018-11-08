package com.app.risk.java.com.app.risk.utility;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.controller.StartupPhaseController;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class is used check armies count in each country in the startup phase
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class StartUpPhaseAssignArmiesTest {
    GamePlay gamePlay;
    Context context = null;
    StartupPhaseController startupphase = null;

    ArrayList<String> playerNames = new ArrayList<String>();

    /**
     * This method gets executed before the test case
     * sets the gameplay instance with the values required for the testing and the context of the test case
     */
    @Before
    public void setUp() {
        gamePlay = new GamePlay();
        context = InstrumentationRegistry.getTargetContext();
        playerNames.add("player1");
        playerNames.add("player2");
        playerNames.add("player3");
        gamePlay.setPlayers(playerNames);
        HashMap<String, Country> countries = new HashMap<String, Country>();
        countries.put("India", new Country("India", new Continent("Asia", 2)));
        countries.put("Italy", new Country("Italy", new Continent("europe", 3)));
        countries.put("America", new Country("America", new Continent("USA", 4)));
        countries.put("pakistan", new Country("pakistan", new Continent("Asia", 2)));
        countries.put("nepal", new Country("nepal", new Continent("europe", 3)));
        countries.put("butan", new Country("butan", new Continent("USA", 4)));
        gamePlay.setCountries(countries);
        startupphase = StartupPhaseController.getInstance().init(gamePlay);
    }

    /**
     * This method checks count in each country in the startup phase
     */
    @Test
    public void startUpPhaseAssignArmiesTest() {
        startupphase.assignInitialCountries();
        startupphase.assignInitialCountries();
        startupphase.placeInitialArmies();

        for (Country country : gamePlay.getCountries().values()) {
            assertTrue(country.getNoOfArmies() >= 1);
        }

    }

    /**
     * This method gets executed after the test case has been executed
     * its sets the gameplay object to null
     */
    @After
    public void cleanUp()
    {
        gamePlay = null;
    }

}
