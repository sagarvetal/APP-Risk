package com.app.risk.java.com.app.risk;



import com.app.risk.java.com.app.risk.view.CardExchangeDialogTest;
import com.app.risk.java.com.app.risk.view.PercentageOfMapPlayScreenActivityTest;
import com.app.risk.java.com.app.risk.view.PlayScreenActivityTest;

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
        CardExchangeDialogTest.class,//check whether the cards have been removed after it has been exchanged for armies
        PercentageOfMapPlayScreenActivityTest.class,//percentage of the Map owned by player
        PlayScreenActivityTest.class,//number of continents owned by player
})
public class ViewTestSuite {
}
