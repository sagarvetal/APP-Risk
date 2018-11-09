package com.app.risk.java.com.app.risk.UtilityOld;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

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
/**
 * This class is used check the army count in the reinforcement phase
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class ReinforcementArmiesCountTest {
    GamePlay gamePlay;
    Context context=null;
    StartupPhaseController startupphase=null;
    ReinforcementPhaseController reinforcementPhaseController=null;
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
        gamePlay.setPlayers(playerNames);
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
    }
    /**
     * This method checks the army count in the reinforcement phase
     */
    @Test
    public void reinforcementArmiesCountTest() {
        startupphase.assignInitialCountries();
        startupphase.assignInitialCountries();
        startupphase.placeInitialArmies();
        gamePlay.setCurrentPlayer();
        assertTrue(gamePlay.getCurrentPlayer().calculateReinforcementArmies(gamePlay)==3);


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
