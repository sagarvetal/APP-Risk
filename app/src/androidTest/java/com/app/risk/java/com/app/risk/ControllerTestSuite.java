package com.app.risk.java.com.app.risk;



import com.app.risk.java.com.app.risk.controller.AllOutAttackTest;
import com.app.risk.java.com.app.risk.controller.AttackPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller.CardAssignTest;
import com.app.risk.java.com.app.risk.controller.CardExchangeControllerTest;
import com.app.risk.java.com.app.risk.controller.CheckAfterEachAttackTest;
import com.app.risk.java.com.app.risk.controller.ExchangeCardForArmiesTest;
import com.app.risk.java.com.app.risk.controller.FortificationPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller.FortificationUnConnectedTest;
import com.app.risk.java.com.app.risk.controller.MapDriverControllerTest;
import com.app.risk.java.com.app.risk.controller.MoreAttackPossibleTest;
import com.app.risk.java.com.app.risk.controller.PlayerWonTest;
import com.app.risk.java.com.app.risk.controller.PreAttackAdjacentCountryTest;
import com.app.risk.java.com.app.risk.controller.PreAttackSufficientArmyTest;
import com.app.risk.java.com.app.risk.controller.ReinforcementContinentValueTest;
import com.app.risk.java.com.app.risk.controller.ReinforcementPhaseControllerTest;
import com.app.risk.java.com.app.risk.controller.StartUpPhaseAssignArmiesTest;
import com.app.risk.java.com.app.risk.controller.StartUpPhaseAssignCountriesTest;
import com.app.risk.java.com.app.risk.controller.StartupPhaseControllerTest;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * Tests for all the implementation classes in the controller
 * @author Akhila Chilukuri
 * @version 1.0.0
 */

@RunWith(Suite.class)

/**
 * Suite for implementation classes in the controller
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
        CardAssignTest.class,//checks whether the card is assigned after the attack
        PlayerWonTest.class,//check whether did the player win the game
        ReinforcementContinentValueTest.class,//checks the army count after the player has occupied the whole continent
        MoreAttackPossibleTest.class//checks whether there is more attack possible or not
})
public class ControllerTestSuite {
}
