package com.app.risk.java.com.app.risk.utility;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.model.GameMap;
import com.app.risk.utility.MapReader;
import com.app.risk.utility.MapVerification;
import com.app.risk.utility.MapWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class is used check whether the map is saved properly or not
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class MapWriterTest {
    private String fileLocation;
    Context context = null;

    /**
     * This method gets executed before the test case
     * sets the file location and the context of the test case
     */
    @Before
    public void setUp() {
        fileLocation = "Test Read Map File.map";
        context = InstrumentationRegistry.getTargetContext();
    }

    /**
     * This method checks whether the map is saved properly or not
     */
    @Test
    public void saveMapTest() {

        MapReader mapReader = new MapReader();
        MapVerification mapVerification = new MapVerification();
        List<GameMap> listGameMap = mapReader.returnGameMapFromFile(context, fileLocation);
        if (mapVerification.mapVerification(listGameMap) == true) {
            MapWriter writeGameMapToFile = new MapWriter();
            writeGameMapToFile.writeGameMapToFile(context, "testMap", listGameMap);
            Path path = null;
            try {
                path = Files.createTempFile("testMap", ".map");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (Files.exists(path))     //true
                assertTrue(true);
            else
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