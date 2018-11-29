package com.app.risk.java.com.app.risk.UtilityOld;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.StartupPhaseController;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class is used check number of countries assigned in the startup phase
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class StartUpPhaseAssignCountriesTest {
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
        ArrayList<String> strategy=new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        gamePlay.setPlayers(playerNames,strategy);
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
     * This method checks number of countries assigned in the startup phase
     */
    @Test
    public void startUpPhaseAssignCountriesTest() {
        startupphase.assignInitialCountries();
        assertEquals(2, gamePlay.getCountryListByPlayerId(gamePlay.getPlayers().get(0).getId()).size());
        assertEquals(2, gamePlay.getCountryListByPlayerId(gamePlay.getPlayers().get(1).getId()).size());
        assertEquals(2, gamePlay.getCountryListByPlayerId(gamePlay.getPlayers().get(2).getId()).size());
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
