package com.app.risk.controller;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This class is used to randomly allocate initial armies to player and
 * number of countries to players.
 * @author Sagar Vetal
 * @date 07/10/2018
 * @version 1.0.0
 */
public class StartupPhaseController {

    /**
     * This method assigns initial countries to each player randomly,
     * @param gamePlay The gamePlay object.
     */
    public void assignInitialCountries(final GamePlay gamePlay) {
        final List<Integer> playerIds =  new ArrayList<>(gamePlay.getPlayers().keySet());
        final List<String> countryNames = new ArrayList<>(gamePlay.getCountries().keySet());

        Collections.sort(playerIds);
        Collections.shuffle(countryNames);

        int countryIndex = 0;
        while(countryIndex != countryNames.size()){
            for(final Integer playerId : playerIds) {
                final String countryName = countryNames.get(countryIndex);
                gamePlay.getCountries().get(countryName).setPlayer(gamePlay.getPlayers().get(playerId));
                gamePlay.getPlayers().get(playerId).incrementCountries(1);
                ++countryIndex;

                if(countryIndex == countryNames.size()){
                    break;
                }
            }
        }
    }

    /**
     * This method assigns initial armies to each player randomly,
     * @param gamePlay The gamePlay object.
     */
    public void assignInitialArmies(final GamePlay gamePlay) {
        final int totalArmies = gamePlay.getCountries().size() * GamePlayConstants.AMRIES_MULTIPLIER;
        final int armiesPerPlayer = totalArmies / gamePlay.getPlayers().size();

        for(final Player player : gamePlay.getPlayers().values()) {
            player.setNoOfArmies(armiesPerPlayer);
        }

        int remainingArmies = totalArmies % gamePlay.getPlayers().size();
        if(remainingArmies != 0) {
            final List<Integer> playerIds =  new ArrayList<>(gamePlay.getPlayers().keySet());
            for(int i = 0; remainingArmies != 0; i++, remainingArmies--) {
                gamePlay.getPlayers().get(playerIds.get(i)).incrementArmies(1);
            }
        }
    }

}
