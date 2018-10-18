package com.app.risk.controller;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import java.util.ArrayList;

/**
 * This class is used to start the reinforcement phase.
 * It calculates the reinforcement armies for current player whose turn is going on.
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 015/10/2018)
 */
public class ReinforcementPhaseController {

    private GamePlay gamePlay;

    /**
     * This parameterized constructor initializes the GamePlay object.
     * @param gamePlay The GamePlay object.
     */
    public ReinforcementPhaseController(final GamePlay gamePlay) {
        this.gamePlay = gamePlay;
    }

    /**
     * This method sets the no of reinforcement armies given to player
     * based on no of countries player owns.
     * @param countriesOwnedByPlayer The list of countries owned by  player.
     */
    public void setReinforcementArmies(final ArrayList<Country> countriesOwnedByPlayer) {
        final int reinforcementArmies = calculateReinforcementArmies(countriesOwnedByPlayer);
        gamePlay.getCurrentPlayer().setReinforcementArmies(reinforcementArmies);
        gamePlay.getCurrentPlayer().incrementArmies(reinforcementArmies);
    }

    /**
     * This method calculates the no of reinforcement armies.
     * @param countriesOwnedByPlayer The list of countries owned by  player.
     */
    public int calculateReinforcementArmies(final ArrayList<Country> countriesOwnedByPlayer){
        int reinforcementArmies = countriesOwnedByPlayer.size() / 3;
        if(reinforcementArmies > GamePlayConstants.MIN_REINFORCEMENT_AMRIES) {
            return  reinforcementArmies;
        }
        return GamePlayConstants.MIN_REINFORCEMENT_AMRIES;
    }

    /**
     * This method gives list of countries concurred by given player.
     * @param playerId This is player id.
     */
    public ArrayList<Country> getCountryListByPlayerId(final int playerId) {
        final ArrayList<Country> countryList = new ArrayList<>();
        for(final Country country : gamePlay.getCountries().values()) {
            if(country.getPlayer().getId() == playerId) {
                countryList.add(country);
            }
        }
        return countryList;
    }
}
