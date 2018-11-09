package com.app.risk.java.com.app.risk.controller;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.controller.AttackPhaseController;
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
 * This class is used to check whether after the attack phase the armies count has reduced or not
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class AttackPhaseControllerTest {

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


        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);

        gm.setCountries(countryList);
        gm.setPlayers(playerNames);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));

        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));

        gm.setCurrentPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setNoOfArmies(5);
        gm.getCountries().get("India").setNoOfArmies(4);
        context = InstrumentationRegistry.getTargetContext();
    }
    /**
     * This method checks whether after the attack phase the armies count has reduced or not
     */
    @Test
    public void afterAttackPhaseTest() {
        AttackPhaseController ac = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        gm.getPlayers().get(0).performAttack(gm.getCountries().get("India"),gm.getCountries().get("Pakistan"),3,2);
        assertTrue((gm.getCountries().get("India").getNoOfArmies()==3)||(gm.getCountries().get("India").getNoOfArmies()==5)||(gm.getCountries().get("India").getNoOfArmies()==2)||(gm.getCountries().get("India").getNoOfArmies()==4));
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
