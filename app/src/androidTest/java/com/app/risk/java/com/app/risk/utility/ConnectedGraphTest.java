package com.app.risk.java.com.app.risk.utility;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.app.risk.model.GameMap;
import com.app.risk.utility.MapReader;
import com.app.risk.utility.MapVerification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConnectedGraphTest {
    private String fileLocation;
    Context context=null;
    @Before
    public void setUp() {
        fileLocation = "Test Read Map File.map";
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void connectedGraphTest() {
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

    @After
    public void cleanUp() {
        fileLocation = null;
    }

}