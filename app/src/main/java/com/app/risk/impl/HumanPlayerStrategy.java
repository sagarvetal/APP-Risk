package com.app.risk.impl;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

/**
 * A concrete strategy class that implements a human player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class HumanPlayerStrategy implements Strategy {

    /**
     * This is reinforcement method for human strategy player.
     * It sets the no of reinforcement armies given to the player based on no of countries player owns.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player) {

    }

    /**
     * This is attack method for human strategy player.
     * In this strategy given no of dices are rolled for attacker and defender.
     * If the defender rolls greater or equal to the attacker then the attacker loses an army,
     * otherwise the defender loses an army.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void attackPhase(final GamePlay gamePlay, final Player player) {

    }

    /**
     * This is fortification method for human strategy player.
     * It moves armies from one country to another country.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player) {

    }

}
