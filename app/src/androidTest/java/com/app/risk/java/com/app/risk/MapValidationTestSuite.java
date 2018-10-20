package com.app.risk.java.com.app.risk;

import com.app.risk.java.com.app.risk.utility.ConnectedGraphTest;
import com.app.risk.java.com.app.risk.utility.DisConnectedGraphTest;
import com.app.risk.java.com.app.risk.utility.InvalidMapTest;
import com.app.risk.java.com.app.risk.utility.MapReaderTest;
import com.app.risk.java.com.app.risk.utility.OneContinentCheckTest;
import com.app.risk.java.com.app.risk.utility.SaveMapTest;
import com.app.risk.java.com.app.risk.utility.SubGraphConnectedTest;
import com.app.risk.java.com.app.risk.utility.UniqueCountryTest;
import com.app.risk.java.com.app.risk.utility.ValidConnectedGraphTest;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Akhila Chilukuri on 07-Nov-17.
 * Tests for Map Validation
 */

@RunWith(Suite.class)

/**
 * Suite for Map Validation
 */
@Suite.SuiteClasses({
        ConnectedGraphTest.class,           //Validation for connected graph
        MapReaderTest.class,                //Validation for reading the Map
        OneContinentCheckTest.class,        //Checks for one continent countries
        SubGraphConnectedTest.class,        //Checks whether subgraph is connected in graph
        UniqueCountryTest.class,           //Checks whether countries are unique or not
        SaveMapTest.class,                 //validates whther the map is saved or not
        DisConnectedGraphTest.class,
        ValidConnectedGraphTest.class,
        InvalidMapTest.class
})
public class MapValidationTestSuite {
}
