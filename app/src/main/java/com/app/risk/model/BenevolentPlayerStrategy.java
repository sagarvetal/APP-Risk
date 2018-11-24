package com.app.risk.model;

import com.app.risk.Interfaces.Strategy;

/**
 * A concrete strategy class that implements a benevolent player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class BenevolentPlayerStrategy implements Strategy {

    /**
     * This is reinforcement method for benevolent strategy player.
     * It places the reinforcement armies on the weakest countries having less no of armies.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player) {

    }

    /**
     * This is attack method for benevolent strategy player.
     * It never attacks on any country.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void attackPhase(final GamePlay gamePlay, final Player player) {

    }

    /**
     * This is fortification method for benevolent strategy player.
     * It fortifies in order to move armies to weaker countries.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player) {

    }

}
