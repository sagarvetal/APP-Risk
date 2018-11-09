package com.app.risk.java.com.app.risk;

import com.app.risk.java.com.app.risk.controller.AllOutAttackTest;
import com.app.risk.java.com.app.risk.controller.AttackPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller.CardExchangeControllerTest;
import com.app.risk.java.com.app.risk.controller.CheckAfterEachAttackTest;
import com.app.risk.java.com.app.risk.controller.ExchangeCardForArmiesTest;
import com.app.risk.java.com.app.risk.controller.FortificationPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller.FortificationUnConnectedTest;
import com.app.risk.java.com.app.risk.controller.MapDriverControllerTest;
import com.app.risk.java.com.app.risk.controller.PreAttackAdjacentCountryTest;
import com.app.risk.java.com.app.risk.controller.PreAttackSufficientArmyTest;
import com.app.risk.java.com.app.risk.controller.ReinforcementPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller.StartUpPhaseAssignArmiesTest;
import com.app.risk.java.com.app.risk.controller.StartUpPhaseAssignCountriesTest;
import com.app.risk.java.com.app.risk.controller.StartupPhaseControllerTest;
import com.app.risk.java.com.app.risk.utility.ConnectedGraphTest;
import com.app.risk.java.com.app.risk.utility.DisConnectedGraphTest;
import com.app.risk.java.com.app.risk.utility.MapReaderTest;
import com.app.risk.java.com.app.risk.utility.MapVerificationTest;
import com.app.risk.java.com.app.risk.utility.MapWriterTest;
import com.app.risk.java.com.app.risk.utility.OneContinentCheckTest;
import com.app.risk.java.com.app.risk.utility.SubGraphConnectedTest;
import com.app.risk.java.com.app.risk.utility.ValidConnectedGraphTest;
import com.app.risk.java.com.app.risk.view.CardExchangeDialogTest;
import com.app.risk.java.com.app.risk.view.PercentageOfMapPlayScreenActivityTest;
import com.app.risk.java.com.app.risk.view.PlayScreenActivityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * Tests for all the test cases
 * @author Akhila Chilukuri
 * @version 1.0.0
 */

@RunWith(Suite.class)

/**
 * Suite for implementation classes in the view
 */
@Suite.SuiteClasses({
        AllOutAttackTest.class, //checks whether one of the two countries have lost all it armies in all out attack
        AttackPhaseControllerTest.class,//checks whether after the attack phase the armies count has reduced or not
        CardExchangeControllerTest.class,//checks whether the card exchange is possible or not
        CheckAfterEachAttackTest.class,//checks after each attack whether the defender and attacker have enough number of armies to attack
        ExchangeCardForArmiesTest.class,//checks whether army count after the exchange has been increased or not.
        FortificationPhaseControllerTest.class,//checks whether the fortification is done between connected countries or not
        FortificationUnConnectedTest.class,//check whether the fortification is not done between unconnected countries or not
        MapDriverControllerTest.class,//checks whether the map is read properly or not
        PreAttackAdjacentCountryTest.class,//checks whether the attacking and defending countries are adjacent to each other or not
        PreAttackSufficientArmyTest.class,//checks for sufficient armies for attack phase
        ReinforcementPhaseControllerTest.class,//checks the army count in the reinforcement phase
        StartUpPhaseAssignArmiesTest.class,//checks armies count in each country in the startup phase
        StartUpPhaseAssignCountriesTest.class,//checks number of countries assigned in the startup phase
        StartupPhaseControllerTest.class,//checks initial armies count in the startup phase
        CardExchangeDialogTest.class,//check whether the cards have been removed after it has been exchanged for armies
        PercentageOfMapPlayScreenActivityTest.class,//percentage of the Map owned by player
        PlayScreenActivityTest.class,//number of continents owned by player
        ConnectedGraphTest.class,//checks whether the graph is connected or not
        DisConnectedGraphTest.class,//checks whether the graph is disconnected or not
        MapReaderTest.class,//checks whether the country in the map is unique or not
        MapVerificationTest.class,//checks whether map is invalid or not
        MapWriterTest.class,//checks whether the map is saved properly or not
        OneContinentCheckTest.class,//checks whether all the countries belong to one continent or not
        SubGraphConnectedTest.class,//checks whether the subgraph is connected or not
        ValidConnectedGraphTest.class,//checks whether the graph is valid or not
})
public class ProjectTestSuite {
}
