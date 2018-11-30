package com.app.risk.java.com.app.risk.utility;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.model.GameMap;
import com.app.risk.utility.MapReader;
import com.app.risk.utility.MapVerification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class is used check whether map is invalid or not
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class MapVerificationTest {
    /**
     * context instance would hold the instance of the target activity
     */
    private Context context = null;
    /**
     * gameplay instances class objects against the country name
     */
    private String fileLocation = null;

    /**
     * This method gets executed before the test case
     * sets the file location and the context of the test case
     */
    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
    }
    /**
     * This method checks whether the graph is connected or not
     */
    @Test
    public void connectedGraphTest() {
        fileLocation = "Test Read Map File.map";
        MapReader mapReader = new MapReader();
        MapVerification mapVerification = new MapVerification();
        List<GameMap> listGameMap = mapReader.returnGameMapFromFile(context, fileLocation);
        mapVerification.mapVerification(listGameMap);
        if (mapVerification.checkMapIsConnectedGraph()) {
            assertTrue(true);
        } else {
            assertFalse(false);
        }
    }
    /**
     * This method checks whether all the countries belong to
     * one continent or not
     */
    @Test
    public void oneContinentCheckTest() {
        fileLocation = "Test Read Map File.map";
        MapReader mapReader=new MapReader();
        MapVerification mapVerification = new MapVerification();
        List<GameMap> listGameMap = mapReader.returnGameMapFromFile(context,fileLocation);
        mapVerification.mapVerification(listGameMap);
        if (mapVerification.checkCountryBelongsToOneContinent()) {
            assertTrue(true);
        } else {
            assertFalse(false);
        }
    }
    /**
     * This method checks whether the subgraph is connected or not
     */
    @Test
    public void subGraphConnectedTest() {
        fileLocation = "src\\test\\java\\com\\app\\risk\\resources\\Test Read Map File.map";
        MapReader mapReader=new MapReader();
        MapVerification mapVerification = new MapVerification();
        List<GameMap> listGameMap = mapReader.returnGameMapFromFile(context,fileLocation);
        mapVerification.mapVerification(listGameMap);
        if (mapVerification.checkContinentIsConnectedSubgraph()) {
            assertTrue(true);
        } else {
            assertFalse(false);
        }
    }

    /**
     * This method checks whether the graph is valid or not
     */
    @Test
    public void validConnectedGraphTest() {
        fileLocation = "3D.map";
        MapReader mapReader=new MapReader();
        MapVerification mapVerification = new MapVerification();
        List<GameMap> listGameMap = mapReader.returnGameMapFromFile(context,fileLocation);
        mapVerification.mapVerification(listGameMap);
        if (mapVerification.checkMapIsConnectedGraph()) {
            assertTrue(true);
        } else {
            assertFalse(false);
        }
    }

    /**
     * This method test the map verification
     */
    @Test
    public void verificationTest() {
        fileLocation = "Twin Volcano.map";
        MapReader mapReader = new MapReader();
        MapVerification mapVerification = new MapVerification();
        List<GameMap> listGameMap = mapReader.returnGameMapFromFile(context, fileLocation);
        mapVerification.mapVerification(listGameMap);
        assertFalse(mapVerification.checkContinentIsConnectedSubgraph());

    }
    /**
     * This method checks whether the country in the map is unique or not
     */
    @Test
    public void uniqueCountryTest() {
        fileLocation = "Read Map File.map";
        MapReader mapReader=new MapReader();
        MapVerification mapVerification = new MapVerification();
        List<GameMap> listGameMap = mapReader.returnGameMapFromFile(context,fileLocation);
        mapVerification.mapVerification(listGameMap);
        if (mapVerification.uniqueCountries()) {
            assertTrue(true);
        } else {
            assertFalse(false);
        }
    }
    /**
     * This method gets executed after the test case has been executed
     * its sets the file location to null
     */
    @After
    public void cleanUp()
    {
        fileLocation = null;
    }
}
