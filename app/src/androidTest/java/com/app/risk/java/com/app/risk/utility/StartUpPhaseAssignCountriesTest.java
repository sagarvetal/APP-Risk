package com.app.risk.java.com.app.risk.utility;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.controller.StartupPhaseController;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;
import com.app.risk.model.GamePlay;
import com.app.risk.utility.MapReader;
import com.app.risk.utility.MapVerification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StartUpPhaseAssignCountriesTest {
    GamePlay gamePlay;
    Context context=null;
    StartupPhaseController startupphase=null;

    ArrayList<String> playerNames=new ArrayList<String>();
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
        startupphase =new StartupPhaseController(gamePlay);
    }

    @Test
    public void startUpPhaseAssignCountriesTest() {
        startupphase.assignInitialCountries();
        assertEquals(2,gamePlay.getCountryListByPlayerId(gamePlay.getPlayers().get(0).getId()).size());
        assertEquals(2,gamePlay.getCountryListByPlayerId(gamePlay.getPlayers().get(1).getId()).size());
        assertEquals(2,gamePlay.getCountryListByPlayerId(gamePlay.getPlayers().get(2).getId()).size());
    }

    @After
    public void cleanUp() {
        gamePlay=null;
    }

}