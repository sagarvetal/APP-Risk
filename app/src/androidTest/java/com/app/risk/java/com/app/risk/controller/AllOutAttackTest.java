package com.app.risk.java.com.app.risk.controller;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.FileConstants;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.PhaseViewController;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.view.PlayScreenActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

/**
 * This class is used to check whether a country has lost all it armies in all out attack
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class AllOutAttackTest {


    Context context = null;
    GamePlay gm = null;

    /**
     * This method gets executed before the test case
     * sets the gameplay instance with the values required for the testing and context of the test case
     */
    @Before
    public void setUp() {
        gm = new GamePlay();
        ArrayList<String> playerNames = new ArrayList<String>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        HashMap<String, Country> countryList = new HashMap<String, Country>();
        countryList.put("India", new Country("India", new Continent("Asia", 1)));
        countryList.put("Pakistan", new Country("Pakistan", new Continent("Africa", 2)));

        ArrayList<String> india = new ArrayList<String>();
        india.add("Pakistan");

        ArrayList<String> pakistan = new ArrayList<String>();
        pakistan.add("India");
        ArrayList<String> strategy=new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);

        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);

        gm.setCountries(countryList);
        gm.setPlayers(playerNames,strategy);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));

        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));

        gm.setCurrentPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setNoOfArmies(5);
        gm.getCountries().get("India").setNoOfArmies(5);
        context = InstrumentationRegistry.getTargetContext();

    }
    /**
     * This method checks whether a country has lost all it armies in all out attack
     */
    @Test
    public void afterAllOutAttackPhaseTest() {
        AttackPhaseController ac = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        gm.getPlayers().get(0).performAllOutAttack(gm.getCountries().get("India"),gm.getCountries().get("Pakistan"));
        assertTrue(gm.getCountries().get("India").getNoOfArmies()==1||gm.getCountries().get("Pakistan").getNoOfArmies()==0);

    }

    /**
     * This method gets executed after the test case has been executed
     * its sets the game map to null
     */
    @After
    public void cleanUp()
    {

        gm=null;
    }
}
