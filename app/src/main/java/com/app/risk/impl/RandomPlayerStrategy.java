package com.app.risk.impl;

import android.content.Context;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A concrete strategy class that implements a random player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class RandomPlayerStrategy implements Strategy {

    private Random random = new Random();

    /**
     * This is reinforcement method for random strategy player.
     * It places the random no of reinforcement armies on random country player owns.
     *
     * @param gamePlay The GamePlay object.
     * @param player   The Player object.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player) {

    }

    /**
     * This is attack method for random strategy player.
     * It attacks a random no of times on a random country.
     *
     * @param gamePlay The GamePlay object.
     * @param player   The Player object.
     */
    @Override
    public void attackPhase(final GamePlay gamePlay, final Player player) {

    }

    /**
     * This is fortification method for random strategy player.
     * It fortifies a random country.
     *
     * @param gamePlay The GamePlay object.
     * @param player   The Player object.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player) {
        ArrayList<Country> countriesBelongingToPlayer = gamePlay.getCountryListByPlayerId(player.getId());
        while (true) {
            int fromCountryIndex = random.nextInt(countriesBelongingToPlayer.size());
            Country fromCountry = countriesBelongingToPlayer.get(fromCountryIndex);
            if (fromCountry.getNoOfArmies() > 1) {
                List<String> reachableCountries = FortificationPhaseController.getInstance().init(null, gamePlay).getReachableCountries(fromCountry, countriesBelongingToPlayer);
                Country toCountry = gamePlay.getCountries().get(reachableCountries.get(random.nextInt(reachableCountries.size())));
                FortificationPhaseController.getInstance().init(null, gamePlay).fortifyCountry(fromCountry, toCountry, random.nextInt(fromCountry.getNoOfArmies() - 1));
                break;
            }
        }
    }
}
