package com.app.risk.java.com.app.risk.utility;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FortificationConnectedTest {
    private String fileLocation;
    Context context=null;
    GamePlay gm=null;
    @Before
    public void setUp() {
        gm=new GamePlay();
        ArrayList<String> playerNames=new ArrayList<String>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        playerNames.add("Player3");
        HashMap<String,Country> countryList=new HashMap<String,Country>();
        countryList.put("India",new Country("India",new Continent("Asia",1)));
        countryList.put("Pakistan",new Country("Pakistan",new Continent("Asia",1)));
        countryList.put("Bangladesh",new Country("Bangladesh",new Continent("Asia",1)));
        countryList.put("Myammar",new Country("Myammar",new Continent("Africa",2)));
        countryList.put("Nepal",new Country("Nepal",new Continent("Africa",2)));
        countryList.put("Bhutan",new Country("Bhutan",new Continent("Africa",2)));
        ArrayList<String> india=new ArrayList<String>();
        india.add("Pakistan");
        india.add("Nepal");
        ArrayList<String> pakistan=new ArrayList<String>();
        pakistan.add("India");
        pakistan.add("Bangladesh");
        ArrayList<String> bangladesh=new ArrayList<String>();
        bangladesh.add("Pakistan");
        ArrayList<String> nepal=new ArrayList<String>();
        nepal.add("India");
        nepal.add("Bhutan");
        nepal.add("Myammar");
        ArrayList<String> bhutan=new ArrayList<String>();
        bhutan.add("Myammar");
        ArrayList<String> myammar=new ArrayList<String>();
        myammar.add("Nepal");
        myammar.add("Bhutan");
        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);
        countryList.get("Bangladesh").setAdjacentCountries(bangladesh);
        countryList.get("Myammar").setAdjacentCountries(myammar);
        countryList.get("Nepal").setAdjacentCountries(nepal);
        countryList.get("Bhutan").setAdjacentCountries(bhutan);
        gm.setCountries(countryList);
        gm.setPlayers(playerNames);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Nepal").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Bhutan").setPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Bangladesh").setPlayer(gm.getPlayers().get(1));
        gm.getCountries().get("Myammar").setPlayer(gm.getPlayers().get(2));
        gm.setCurrentPlayer(gm.getPlayers().get(0));
        context = InstrumentationRegistry.getTargetContext();
    }//gm.getCountries().get("India").getPlayer().getId()==gm.getCountries().get("Bhutan").getPlayer().getId(

    @Test
    public void fortificationConnectedTest() {
        //StartupPhaseController startup=new StartupPhaseController(gm);
        //startup.assignInitialArmies();
        //startup.placeInitialArmies();
        FortificationPhaseController fc=new FortificationPhaseController(gm);
        assertTrue(fc.isCountriesConneted(gm.getCountries().get("India"),gm.getCountries().get("Bhutan")));
        // ArrayList<String> reachableCountries=fc.getReachableCountries(gm.getCountries().get("India"),gm.getCountryListByPlayerId(0));



    }

    @After
    public void cleanUp() {

    }
}