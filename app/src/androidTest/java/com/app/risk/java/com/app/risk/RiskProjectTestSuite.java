package com.app.risk.java.com.app.risk;
import com.app.risk.java.com.app.risk.controller.AttackPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller.CardExchangeControllerTest;
import com.app.risk.java.com.app.risk.controller.FortificationPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller.MapDriverControllerTest;
import com.app.risk.java.com.app.risk.controller.ReinforcementPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller.StartUpPhaseControllerTest;
import com.app.risk.java.com.app.risk.view.TournamentModeTest;
import com.app.risk.java.com.app.risk.impl.AggresiveStrategyTest;
import com.app.risk.java.com.app.risk.impl.BenevolentPlayerStrategyTest;
import com.app.risk.java.com.app.risk.impl.CheaterStrategyTest;
import com.app.risk.java.com.app.risk.impl.RandomStrategyTest;
import com.app.risk.java.com.app.risk.utility.MapWriterTest;
import com.app.risk.java.com.app.risk.utility.MapReaderTest;
import com.app.risk.java.com.app.risk.utility.MapVerificationTest;
import com.app.risk.java.com.app.risk.utility.SaveLoadGameTest;
import com.app.risk.java.com.app.risk.view.CardExchangeDialogTest;
import com.app.risk.java.com.app.risk.view.PercentageOfMapPlayScreenActivityTest;
import com.app.risk.java.com.app.risk.view.PlayScreenActivityTest;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Akhila Chilukuri on 07-Nov-17.
 * Tests for Risk Project
 */

@RunWith(Suite.class)

/**
 * Suite for Risk Project
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
        AggresiveStrategyTest.class,//contains test cases which test aggressive player strategy
        RandomStrategyTest.class,///contains test cases which test random player strategy
        CheaterStrategyTest.class,///contains test cases which test cheater player strategy
        TournamentModeTest.class ///contains test case for tournament
})
public class RiskProjectTestSuite {
}
