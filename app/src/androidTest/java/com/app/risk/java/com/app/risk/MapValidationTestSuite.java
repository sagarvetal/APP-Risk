package com.app.risk.java.com.app.risk;
import com.app.risk.java.com.app.risk.controller1.AttackPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller1.CardExchangeControllerTest;
import com.app.risk.java.com.app.risk.controller1.FortificationPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller1.MapDriverControllerTest;
import com.app.risk.java.com.app.risk.controller1.ReinforcementPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller1.StartUpPhaseControllerTest;
import com.app.risk.java.com.app.risk.impl1.AggresiveStrategyTest;
import com.app.risk.java.com.app.risk.impl1.BenevolentPlayerStrategyTest;
import com.app.risk.java.com.app.risk.impl1.RandomStrategyTest;
import com.app.risk.java.com.app.risk.utility1.MapWriterTest;
import com.app.risk.java.com.app.risk.utility1.MapReaderTest;
import com.app.risk.java.com.app.risk.utility1.MapVerificationTest;
import com.app.risk.java.com.app.risk.utility1.SaveLoadGameTest;
import com.app.risk.java.com.app.risk.view1.CardExchangeDialogTest;
import com.app.risk.java.com.app.risk.view1.PercentageOfMapPlayScreenActivityTest;
import com.app.risk.java.com.app.risk.view1.PlayScreenActivityTest;


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
        AttackPhaseControllerTest.class,//contains test cases used to validate the attack phase.
        CardExchangeControllerTest.class,//contains test cases used validate card assignment
        FortificationPhaseControllerTest.class,//contains test cases which checks and validates the fortification phase
        ReinforcementPhaseControllerTest.class,//contains test cases which checks and validates the reinforcement phase
        StartUpPhaseControllerTest.class,//contains test cases which checks and validates the startup phase
        MapDriverControllerTest.class,//contains test cases which checks whether the map is read properly or not
        MapReaderTest.class,//contains test cases which checks whether the country in the map is unique or not
        MapVerificationTest.class,//contains test cases which checks whether map is invalid or not
        MapWriterTest.class,//contains test cases which checks whether the map is saved properly or not
        SaveLoadGameTest.class,//contains test cases which checks whether the instance is saved to the map properly or not
        BenevolentPlayerStrategyTest.class,//contains test cases which checks the benevolent player behaviour in all the three phases
        CardExchangeDialogTest.class,//contains test cases which checks whether the cards have been removed after it has been exchanged for armies
        PercentageOfMapPlayScreenActivityTest.class,//contains test cases which checks test percentage of the Map owned by player
        PlayScreenActivityTest.class,//contains test cases which checks the continents owned by player
        AggresiveStrategyTest.class,
        RandomStrategyTest.class,
})
public class MapValidationTestSuite {
}
