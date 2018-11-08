package com.app.risk.java.com.app.risk;

import com.app.risk.java.com.app.risk.utility.AllOutAttackTest;
import com.app.risk.java.com.app.risk.utility.AttackPhaseTest;
import com.app.risk.java.com.app.risk.utility.CardExchangePossibleTest;
import com.app.risk.java.com.app.risk.utility.CheckAfterEachAttackTest;
import com.app.risk.java.com.app.risk.utility.ConnectedGraphTest;
import com.app.risk.java.com.app.risk.utility.DisConnectedGraphTest;
import com.app.risk.java.com.app.risk.utility.ExchangeCardForArmiesTest;
import com.app.risk.java.com.app.risk.utility.FortificationConnectedTest;
import com.app.risk.java.com.app.risk.utility.FortificationUnConnectedTest;
import com.app.risk.java.com.app.risk.utility.InvalidMapTest;
import com.app.risk.java.com.app.risk.utility.MapReaderTest;
import com.app.risk.java.com.app.risk.utility.OneContinentCheckTest;
import com.app.risk.java.com.app.risk.utility.PreAttackAdjacentCountryTest;
import com.app.risk.java.com.app.risk.utility.PreAttackSufficientArmyTest;
import com.app.risk.java.com.app.risk.utility.ReinforcementArmiesCountTest;
import com.app.risk.java.com.app.risk.utility.RemoveCardsAfterExchange;
import com.app.risk.java.com.app.risk.utility.SaveMapTest;
import com.app.risk.java.com.app.risk.utility.StartUpPhaseAssignArmiesTest;
import com.app.risk.java.com.app.risk.utility.StartUpPhaseAssignCountriesTest;
import com.app.risk.java.com.app.risk.utility.StartUpPhaseInitialArmies;
import com.app.risk.java.com.app.risk.utility.SubGraphConnectedTest;
import com.app.risk.java.com.app.risk.utility.UniqueCountryTest;
import com.app.risk.java.com.app.risk.utility.ValidConnectedGraphTest;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * This class is the test suite for all the test cases
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
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
        InvalidMapTest.class,
        FortificationConnectedTest.class,
        FortificationUnConnectedTest.class,
        ReinforcementArmiesCountTest.class,
        StartUpPhaseAssignArmiesTest.class,
        StartUpPhaseAssignCountriesTest.class,
        StartUpPhaseInitialArmies.class,
        PreAttackAdjacentCountryTest.class,
        PreAttackSufficientArmyTest.class,
        CheckAfterEachAttackTest.class,
        AttackPhaseTest.class,
        AllOutAttackTest.class,
        CardExchangePossibleTest.class,
        ExchangeCardForArmiesTest.class,
        RemoveCardsAfterExchange.class,
})
public class MapValidationTestSuite {
}
