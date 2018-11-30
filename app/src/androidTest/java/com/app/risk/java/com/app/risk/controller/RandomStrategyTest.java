package com.app.risk.java.com.app.risk.controller;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.controller.StartupPhaseController;
import com.app.risk.impl.RandomPlayerStrategy;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;

public class RandomStrategyTest {

    private Context context = null;
    private GamePlay gamePlay = null;
    StartupPhaseController startupPhaseController = null;
    ReinforcementPhaseController reinforcementPhaseController = null;
    AttackPhaseController attackPhaseController = null;
    ArrayList<String> playerNames = new ArrayList<String>();
    HashMap<String, Country> countries = new HashMap<String, Country>();

    @Before
    public void setUp(){
        gamePlay = new GamePlay();
        context = InstrumentationRegistry.getTargetContext();
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
        boolean flag = false;
        if(gamePlay.getCountries().get("India").getNoOfArmies()>1){
            flag = true;
        }
        if(gamePlay.getCountries().get("Italy").getNoOfArmies()>2){
            flag = true;
        }
        if(gamePlay.getCountries().get("America").getNoOfArmies()>3){
            flag = true;
        }
        assertTrue(flag == true);
    }

    @Test
    public void randomAttackTest(){
        gamePlay.getPlayers().get(0).setNoOfArmies(6);
        gamePlay.getPlayers().get(1).setNoOfArmies(4);
        gamePlay.getPlayers().get(0).setNoOfCountries(1);
        gamePlay.getPlayers().get(1).setNoOfCountries(1);
        gamePlay.getCountries().get("America").setPlayer(gamePlay.getPlayers().get(0));
        gamePlay.getCountries().get("America").setNoOfArmies(6);
        gamePlay.getCountries().get("Italy").setPlayer(gamePlay.getPlayers().get(1));
        gamePlay.getCountries().get("Italy").setNoOfArmies(4);
        gamePlay.setCurrentPlayer(gamePlay.getPlayers().get(0));
        attackPhaseController = AttackPhaseController.getInstance().init(InstrumentationRegistry.getTargetContext(), gamePlay);
        ((RandomPlayerStrategy) gamePlay.getCurrentPlayer().getStrategy()).performAllOutAttack(gamePlay.getCountries().get("America"),
                gamePlay.getCountries().get("Italy"), null, null, null);
    }
}
