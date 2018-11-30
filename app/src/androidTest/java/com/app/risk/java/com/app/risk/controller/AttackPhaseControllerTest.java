package com.app.risk.java.com.app.risk.controller;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class is used to validate the attack phase
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class AttackPhaseControllerTest {
    /**
     * countryList instance would hold instance of the country against the country name
     */
    HashMap<String, Country> countryList = new HashMap<String, Country>();
    /**
     * context instance would hold the instance of the target activity
     */
    private Context context = null;
    /**
     * gameplay instances would hold the objects required for the test cases
     */
    private GamePlay gm = null;

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
        ArrayList<String> strategy = new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);

        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);

        gm.setCountries(countryList);
        gm.setPlayers(playerNames, strategy);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));

        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));

        gm.setCurrentPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setNoOfArmies(5);
        gm.getCountries().get("India").setNoOfArmies(5);
        context = InstrumentationRegistry.getTargetContext();

    }
    /**
     * This method checks whether the defender country is adjacent to the attacker country
     */
    @Test
    public void preAttackPhaseAdjacentTest() {
        gm = new GamePlay();
        ArrayList<String> playerNames = new ArrayList<String>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        HashMap<String, Country> countryList = new HashMap<String, Country>();
        countryList.put("India", new Country("India", new Continent("Asia", 1)));
        countryList.put("Pakistan", new Country("Pakistan", new Continent("Asia", 1)));
        countryList.put("Bangladesh", new Country("Bangladesh", new Continent("Asia", 1)));
        countryList.put("Myammar", new Country("Myammar", new Continent("Africa", 2)));
        countryList.put("Nepal", new Country("Nepal", new Continent("Africa", 2)));
        countryList.put("Bhutan", new Country("Bhutan", new Continent("Africa", 2)));
        ArrayList<String> india = new ArrayList<String>();
        india.add("Pakistan");
        india.add("Nepal");
        ArrayList<String> pakistan = new ArrayList<String>();
        pakistan.add("India");
        pakistan.add("Bangladesh");
        ArrayList<String> bangladesh = new ArrayList<String>();
        bangladesh.add("Pakistan");
        ArrayList<String> nepal = new ArrayList<String>();
        nepal.add("India");
        nepal.add("Bhutan");
        nepal.add("Myammar");
        ArrayList<String> bhutan = new ArrayList<String>();
        bhutan.add("Myammar");
        ArrayList<String> myammar = new ArrayList<String>();
        myammar.add("Nepal");
        myammar.add("Bhutan");
        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);
        countryList.get("Bangladesh").setAdjacentCountries(bangladesh);
        countryList.get("Myammar").setAdjacentCountries(myammar);
        countryList.get("Nepal").setAdjacentCountries(nepal);
        countryList.get("Bhutan").setAdjacentCountries(bhutan);
        gm.setCountries(countryList);
        ArrayList<String> strategy=new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        gm.setPlayers(playerNames,strategy);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Nepal").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Bhutan").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Bangladesh").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Myammar").setPlayer(gm.getPlayers().get(1));
        gm.setCurrentPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("India").setNoOfArmies(1);
        gm.getCountries().get("Bhutan").setNoOfArmies(4);
        AttackPhaseController ac = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        assertTrue(ac.isCountryAdjacent(gm.getCountries().get("India"),gm.getCountries().get("Nepal")));
    }
    /**
     * This method checks whether the defender country has sufficient armies to attack or not
     */
    @Test
    public void preAttackPhaseSufficientArmiesTest() {
        gm = new GamePlay();
        ArrayList<String> playerNames = new ArrayList<String>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        HashMap<String, Country> countryList = new HashMap<String, Country>();
        countryList.put("India", new Country("India", new Continent("Asia", 1)));
        countryList.put("Pakistan", new Country("Pakistan", new Continent("Asia", 1)));
        countryList.put("Bangladesh", new Country("Bangladesh", new Continent("Asia", 1)));
        countryList.put("Myammar", new Country("Myammar", new Continent("Africa", 2)));
        countryList.put("Nepal", new Country("Nepal", new Continent("Africa", 2)));
        countryList.put("Bhutan", new Country("Bhutan", new Continent("Africa", 2)));
        ArrayList<String> india = new ArrayList<String>();
        india.add("Pakistan");
        india.add("Nepal");
        ArrayList<String> pakistan = new ArrayList<String>();
        pakistan.add("India");
        pakistan.add("Bangladesh");
        ArrayList<String> bangladesh = new ArrayList<String>();
        bangladesh.add("Pakistan");
        ArrayList<String> nepal = new ArrayList<String>();
        nepal.add("India");
        nepal.add("Bhutan");
        nepal.add("Myammar");
        ArrayList<String> bhutan = new ArrayList<String>();
        bhutan.add("Myammar");
        ArrayList<String> myammar = new ArrayList<String>();
        myammar.add("Nepal");
        myammar.add("Bhutan");
        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);
        countryList.get("Bangladesh").setAdjacentCountries(bangladesh);
        countryList.get("Myammar").setAdjacentCountries(myammar);
        countryList.get("Nepal").setAdjacentCountries(nepal);
        countryList.get("Bhutan").setAdjacentCountries(bhutan);
        gm.setCountries(countryList);
        ArrayList<String> strategy=new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        gm.setPlayers(playerNames,strategy);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Nepal").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Bhutan").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Bangladesh").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Myammar").setPlayer(gm.getPlayers().get(1));
        gm.setCurrentPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setNoOfArmies(3);
        gm.getCountries().get("Myammar").setNoOfArmies(4);
        AttackPhaseController fc = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        assertTrue(fc.canCountryAttack(gm.getCountries().get("Pakistan"),gm.getCountries().get("Myammar")));
        gm.getCountries().get("Pakistan").setNoOfArmies(0);
        assertFalse(fc.canCountryAttack(gm.getCountries().get("Pakistan"),gm.getCountries().get("Myammar")));
    }
    /**
     * This method checks whether a country has lost all it armies in all out attack
     */
    @Test
    public void afterAllOutAttackPhaseTest() {
        AttackPhaseController ac = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        gm.getPlayers().get(0).performAllOutAttack(gm.getCountries().get("India"), gm.getCountries().get("Pakistan"));
        assertTrue(gm.getCountries().get("India").getNoOfArmies() == 1 || gm.getCountries().get("Pakistan").getNoOfArmies() == 0);

    }

    /**
     * This method checks whether after the attack phase the armies count has reduced or not
     */
    @Test
    public void afterAttackPhaseTest() {
        AttackPhaseController ac = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        gm.getPlayers().get(0).performAttack(gm.getCountries().get("India"), gm.getCountries().get("Pakistan"), 3, 2);
        assertTrue((gm.getCountries().get("India").getNoOfArmies() == 3) || (gm.getCountries().get("India").getNoOfArmies() == 5) || (gm.getCountries().get("India").getNoOfArmies() == 2) || (gm.getCountries().get("India").getNoOfArmies() == 4));
    }

    /**
     * This method checks whether after the attack phase the armies count has reduced in attacker and defender
     */
    @Test
    public void attackerAndDefenderArmiesTest() {
        gm.getCountries().get("India").setNoOfArmies(4);
        AttackPhaseController ac = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        gm.getPlayers().get(0).performAttack(gm.getCountries().get("India"), gm.getCountries().get("Pakistan"), 3, 2);
        assertTrue(((gm.getCountries().get("India").getNoOfArmies() == 3) && (gm.getCountries().get("Pakistan").getNoOfArmies() == 4)) ||
                ((gm.getCountries().get("India").getNoOfArmies() == 4) && (gm.getCountries().get("Pakistan").getNoOfArmies() == 3)) ||
                ((gm.getCountries().get("India").getNoOfArmies() == 2) && (gm.getCountries().get("Pakistan").getNoOfArmies() == 5)));

    }

    /**
     * This method checks after each attack whether the defender and attacker have enough number of armies to attack
     */
    @Test
    public void afterEachAttackPhaseTest() {
        ArrayList<String> playerNames = new ArrayList<String>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        HashMap<String, Country> countryList = gm.getCountries();
        countryList.put("Pakistan", new Country("Pakistan", new Continent("Asia", 1)));
        countryList.put("Bangladesh", new Country("Bangladesh", new Continent("Asia", 1)));
        countryList.put("Myammar", new Country("Myammar", new Continent("Africa", 2)));
        countryList.put("Nepal", new Country("Nepal", new Continent("Africa", 2)));
        countryList.put("Bhutan", new Country("Bhutan", new Continent("Africa", 2)));
        ArrayList<String> india = new ArrayList<String>();
        india.add("Pakistan");
        india.add("Nepal");
        ArrayList<String> pakistan = new ArrayList<String>();
        pakistan.add("India");
        pakistan.add("Bangladesh");
        ArrayList<String> bangladesh = new ArrayList<String>();
        bangladesh.add("Pakistan");
        ArrayList<String> nepal = new ArrayList<String>();
        nepal.add("India");
        nepal.add("Bhutan");
        nepal.add("Myammar");
        ArrayList<String> bhutan = new ArrayList<String>();
        bhutan.add("Myammar");
        ArrayList<String> myammar = new ArrayList<String>();
        myammar.add("Nepal");
        myammar.add("Bhutan");
        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);
        countryList.get("Bangladesh").setAdjacentCountries(bangladesh);
        countryList.get("Myammar").setAdjacentCountries(myammar);
        countryList.get("Nepal").setAdjacentCountries(nepal);
        countryList.get("Bhutan").setAdjacentCountries(bhutan);
        gm.setCountries(countryList);
        ArrayList<String> strategy = new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        gm.setPlayers(playerNames, strategy);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Nepal").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Bhutan").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Bangladesh").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Myammar").setPlayer(gm.getPlayers().get(1));
        gm.setCurrentPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setNoOfArmies(0);
        gm.getCountries().get("Myammar").setNoOfArmies(4);
        AttackPhaseController fc = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        assertFalse(gm.getCountries().get("Pakistan").checkAftereachAttack(gm.getCountries().get("Myammar")));
        gm.getCountries().get("Pakistan").setNoOfArmies(4);
        assertTrue(gm.getCountries().get("Pakistan").checkAftereachAttack(gm.getCountries().get("Myammar")));
    }

    /**
     * This method checks whether there is more attack possible or not
     */
    @Test
    public void isMoreAttackPossibleTest() {
        AttackPhaseController ac = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        ArrayList<Country> countryArrayList = new ArrayList<Country>();
        countryArrayList.addAll(countryList.values());
        assertFalse(gm.getCurrentPlayer().isMoreAttackPossible(gm, countryArrayList));

    }
    /**
     * This method checks whether the player win the game
     */
    @Test
    public void playerWonTest() {
        AttackPhaseController ac = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gm);
        assertTrue(gm.getCurrentPlayer().isPlayerWon(countryList));

    }
    /**
     * This method gets executed after the test case has been executed
     * its sets the game map to null
     */
    @After
    public void cleanUp() {

        gm = null;
    }
}
