package com.app.risk.java.com.app.risk.utility;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.view.Display;

import com.app.risk.model.GameMap;
import com.app.risk.utility.MapReader;
import com.app.risk.utility.MapVerification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * This class is used check whether the subgraph is connected or not
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class SubGraphConnectedTest {
    private String fileLocation;
    Context context = null;
    /**
     * This method gets executed before the test case
     * sets the file location and the context of the test case
     */
    @Before
    public void setUp() {
        fileLocation = "src\\test\\java\\com\\app\\risk\\resources\\Test Read Map File.map";
        context = InstrumentationRegistry.getTargetContext();
    }
    /**
     * This method checks whether the subgraph is connected or not
     */
    @Test
    public void subGraphConnectedTest() {
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
     * This method gets executed after the test case has been executed
     * its sets the file location to null
     */
    @After
    public void cleanUp()
    {
        fileLocation = null;
    }

}