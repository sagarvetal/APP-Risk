package com.app.risk.java.com.app.risk;

import com.app.risk.java.com.app.risk.UtilityOld.AllOutAttackTest;
import com.app.risk.java.com.app.risk.UtilityOld.AttackPhaseTest;
import com.app.risk.java.com.app.risk.UtilityOld.CardExchangePossibleTest;
import com.app.risk.java.com.app.risk.UtilityOld.CheckAfterEachAttackTest;
import com.app.risk.java.com.app.risk.UtilityOld.ConnectedGraphTest;
import com.app.risk.java.com.app.risk.UtilityOld.ContinentsOwnedByPlayerTest;
import com.app.risk.java.com.app.risk.UtilityOld.DisConnectedGraphTest;
import com.app.risk.java.com.app.risk.UtilityOld.ExchangeCardForArmiesTest;
import com.app.risk.java.com.app.risk.UtilityOld.FortificationConnectedTest;
import com.app.risk.java.com.app.risk.UtilityOld.FortificationUnConnectedTest;
import com.app.risk.java.com.app.risk.UtilityOld.InvalidMapTest;
import com.app.risk.java.com.app.risk.UtilityOld.MapReaderTest;
import com.app.risk.java.com.app.risk.UtilityOld.OneContinentCheckTest;
import com.app.risk.java.com.app.risk.UtilityOld.PercentageOfMapOwnedByPlayerTest;
import com.app.risk.java.com.app.risk.UtilityOld.PreAttackAdjacentCountryTest;
import com.app.risk.java.com.app.risk.UtilityOld.PreAttackSufficientArmyTest;
import com.app.risk.java.com.app.risk.UtilityOld.ReinforcementArmiesCountTest;
import com.app.risk.java.com.app.risk.UtilityOld.RemoveCardsAfterExchange;
import com.app.risk.java.com.app.risk.UtilityOld.SaveMapTest;
import com.app.risk.java.com.app.risk.UtilityOld.StartUpPhaseAssignArmiesTest;
import com.app.risk.java.com.app.risk.UtilityOld.StartUpPhaseAssignCountriesTest;
import com.app.risk.java.com.app.risk.UtilityOld.StartUpPhaseInitialArmies;
import com.app.risk.java.com.app.risk.UtilityOld.SubGraphConnectedTest;
import com.app.risk.java.com.app.risk.UtilityOld.UniqueCountryTest;
import com.app.risk.java.com.app.risk.UtilityOld.ValidConnectedGraphTest;


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
        PercentageOfMapOwnedByPlayerTest.class,
        ContinentsOwnedByPlayerTest.class
})
public class MapValidationTestSuite {
}
