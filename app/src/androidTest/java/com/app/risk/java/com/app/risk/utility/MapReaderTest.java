package com.app.risk.java.com.app.risk.utility;


import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.model.GamePlay;
import com.app.risk.utility.MapReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapReaderTest {
    private String fileLocation;
    Context context=null;
    @Before
    public void setUp() {
        fileLocation = "Test Read Map File.map";
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void mapReaderTest() {
        GamePlay gamePlay = MapReader.returnGamePlayFromFile(context,fileLocation);
        assertNotNull(gamePlay);
        System.out.println("Continent list: " + gamePlay.getContinents().size());
        System.out.println("Territory list size: " + gamePlay.getCountries().size());
        assertEquals(3, gamePlay.getContinents().size());
        assertEquals(3, gamePlay.getCountries().size());

    }

    @After
    public void cleanUp() {
        fileLocation = null;
    }
}