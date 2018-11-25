package com.app.risk.impl;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import java.util.ArrayList;

/**
 * A concrete strategy class that implements a cheater player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class CheaterPlayerStrategy implements Strategy {

    /**
     * This is reinforcement method for cheater strategy player.
     * It doubles the number of armies on all its countries.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player) {

        ArrayList<Country> countryArrayList = gamePlay.getCountryListByPlayerId(player.getId());

        for(Country country : countryArrayList){
            country.setNoOfArmies(country.getNoOfArmies() * 2);
        }
        player.setNoOfArmies(player.getNoOfArmies() * 2);
    }

    /**
     * This is attack method for cheater strategy player.
     * It conquers all the neighbors of all its countries.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void attackPhase(final GamePlay gamePlay, final Player player) {
        ArrayList<Country> countryArrayList = gamePlay.getCountryListByPlayerId(player.getId());

        for(Country country: countryArrayList){
            ArrayList<String> adjacentCountries = country.getAdjacentCountries();
            for(String neighbour: adjacentCountries){
                Country defenderCountry = gamePlay.getCountries().get(neighbour);
                int noOfArmies = defenderCountry.getNoOfArmies();
                defenderCountry.getPlayer().decrementArmies(noOfArmies);
                defenderCountry.setPlayer(player);
                defenderCountry.getPlayer().incrementArmies(noOfArmies);
            }
        }
    }

    /**
     * This is fortification method for cheater strategy player.
     * It doubles the number of armies on its countries that have neighbors that belong to other players.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player) {
        ArrayList<Country> countryArrayList = gamePlay.getCountryListByPlayerId(player.getId());

        for(Country country : countryArrayList){
            ArrayList<String> adjacentCountries = country.getAdjacentCountries();
            for(String neighbour: adjacentCountries){
                if(player.getId() != gamePlay.getCountries().get(neighbour).getPlayer().getId()){
                    int noOfArmies = country.getNoOfArmies();
                    country.incrementArmies(noOfArmies);
                    player.incrementArmies(noOfArmies);
                    break;
                }
            }
        }
    }

}
