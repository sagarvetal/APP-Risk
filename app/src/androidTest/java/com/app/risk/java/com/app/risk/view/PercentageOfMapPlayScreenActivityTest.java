package com.app.risk.java.com.app.risk.view;

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
 * Class to test percentage of the Map owned by player
 */

public class PercentageOfMapPlayScreenActivityTest {
    GamePlay gamePlay = new GamePlay();

    /**
     * Set's up Game play object to be used to test percentage owned by player
     */
    @Before
    public void setUp() {
        ArrayList<String> arrPlayer = new ArrayList<>();
        arrPlayer.add("1");
        arrPlayer.add("2");
        gamePlay.setPlayers(arrPlayer);
        HashMap<String, Country> countries = new HashMap<>();
        HashMap<String, Continent> continents = new HashMap<>();

        Country country1 = new Country();
        country1.setNameOfCountry("India");
        Country country2 = new Country();
        country2.setNameOfCountry("Sri");
        Country country3 = new Country();
        country3.setNameOfCountry("Pak");
        Country country4 = new Country();
        country4.setNameOfCountry("Nepal");

        Continent continent1 = new Continent();
        continent1.setNameOfContinent("Asia");
        continent1.setCountries(country1);
        continent1.setCountries(country2);
        Continent continent2 = new Continent();
        continent2.setNameOfContinent("Europe");
        continent2.setCountries(country3);
        continent2.setCountries(country4);

        country1.setBelongsToContinent(continent1);
        country2.setBelongsToContinent(continent1);
        country3.setBelongsToContinent(continent2);
        country4.setBelongsToContinent(continent2);

        country1.setPlayer(gamePlay.getPlayers().get(0));
        country2.setPlayer(gamePlay.getPlayers().get(0));
        country3.setPlayer(gamePlay.getPlayers().get(1));
        country4.setPlayer(gamePlay.getPlayers().get(1));

        countries.put("India",country1);
        countries.put("Sri",country2);
        countries.put("Pak",country3);
        countries.put("Nepal",country4);
        gamePlay.setCountries(countries);
        continents.put("Asia",continent1);
        continents.put("Europe",continent2);
        gamePlay.setContinents(continents);

    }

    /**
     * Test percentage of map owned by player
     */
    @Test
    public void percentageOwnedByPlayerTest() {
        assertTrue(gamePlay.getPlayers().get(0).getPercentageOfMapOwnedByPlayer(gamePlay) == 50 );
    }

    /**
     * Set's gameplay object to null
     */
    @After
    public void cleanUp() {
        gamePlay = null;
    }
}
