package com.app.risk.Interfaces;

import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

/**
 * This interface is implemented by concrete strategy classes.
 * The Player class uses this to use a concrete strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public interface Strategy {

    /**
     * This is reinforcement method.
     * It sets the no of reinforcement armies given to the player based on no of countries player owns.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    public void reinforcementPhase(final GamePlay gamePlay, final Player player);

    /**
     * This is attack method.
     * Attacker attacks on country according to its strategy.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    public void attackPhase(final GamePlay gamePlay, final Player player);

    /**
     * This is fortification method.
     * It moves armies from one country to another country.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    public void fortificationPhase(final GamePlay gamePlay, final Player player);

}
