package com.app.risk.java.com.app.risk;



import com.app.risk.java.com.app.risk.utility.ConnectedGraphTest;
import com.app.risk.java.com.app.risk.utility.DisConnectedGraphTest;
import com.app.risk.java.com.app.risk.utility.MapReaderTest;
import com.app.risk.java.com.app.risk.utility.MapVerificationTest;
import com.app.risk.java.com.app.risk.utility.MapWriterTest;
import com.app.risk.java.com.app.risk.utility.OneContinentCheckTest;
import com.app.risk.java.com.app.risk.utility.SubGraphConnectedTest;
import com.app.risk.java.com.app.risk.utility.ValidConnectedGraphTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * Tests for all the classes in the view
 * @author Akhila Chilukuri
 * @version 1.0.0
 */

@RunWith(Suite.class)

/**
 * Suite for implementation classes in the view
 */
@Suite.SuiteClasses({
        ConnectedGraphTest.class,//checks whether the graph is connected or not
        DisConnectedGraphTest.class,//checks whether the graph is disconnected or not
        MapReaderTest.class,//checks whether the country in the map is unique or not
        MapVerificationTest.class,//checks whether map is invalid or not
        MapWriterTest.class,//checks whether the map is saved properly or not
        OneContinentCheckTest.class,//checks whether all the countries belong to one continent or not
        SubGraphConnectedTest.class,//checks whether the subgraph is connected or not
        ValidConnectedGraphTest.class,//checks whether the graph is valid or not
})
public class UtilityTestSuite {
}
