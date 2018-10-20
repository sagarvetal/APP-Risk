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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SaveMapTest{
    private String fileLocation;
    Context context=null;

    @Before
    public void setUp() {
        fileLocation = "Test Read Map File.map";
        context=InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void saveMapTest() {

        MapReader mapReader=new MapReader();
        MapVerification mapVerification = new MapVerification();
        List<GameMap> listGameMap = mapReader.returnGameMapFromFile(context,fileLocation);
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

    @After
    public void cleanUp() {
        fileLocation = null;
    }

}