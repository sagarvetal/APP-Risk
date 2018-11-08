package com.app.risk.java.com.app.risk;

import com.app.risk.java.com.app.risk.utility.ContinentsOwnedByPlayerTest;
import com.app.risk.java.com.app.risk.utility.PercentageOfMapOwnedByPlayerTest;


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

        PercentageOfMapOwnedByPlayerTest.class,
        ContinentsOwnedByPlayerTest.class
})
public class MapValidationTestSuite {
}
