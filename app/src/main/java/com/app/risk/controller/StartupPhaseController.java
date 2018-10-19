package com.app.risk.controller;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class is used to start the startup phase.
 * It randomly allocates initial armies to player, number of countries to players and
 * place armies on countries in round-robbin fashion.
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 07/10/2018)
 */
public class StartupPhaseController {

    private GamePlay gamePlay;

    /**
     * This parameterized constructor initializes the GamePlay object.
     * @param gamePlay The GamePlay object.
     */
    public StartupPhaseController(final GamePlay gamePlay) {
        this.gamePlay = gamePlay;
    }

    /**
    * This method starts the startup phase.
    */
    public void start(){
        assignInitialCountries();
        assignInitialArmies();
        placeInitialArmies();
    }

    /**
     * This method assigns initial countries to each player randomly,
     */
    public void assignInitialCountries() {
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
     */
    public void assignInitialArmies() {
        for(final Player player : gamePlay.getPlayers().values()) {
            player.setNoOfArmies(player.getNoOfCountries() * GamePlayConstants.AMRIES_MULTIPLIER);
        }
    }

    /**
     * This method place initial armies on countries in round robbin fashion,
     */
    public void placeInitialArmies(){

        /*Place one army on each country*/
        for(final Country country : gamePlay.getCountries().values()) {
            country.incrementArmies(1);
        }

        boolean isMoreArmiesAvailable = false;
        for(final Player player : gamePlay.getPlayers().values()) {
            final int diff = player.getNoOfArmies() - player.getNoOfCountries();
            if(diff > 0) {
                isMoreArmiesAvailable = true;
                break;
            }
        }

        /*
         * If players are having more no of armies than no of countries,
         * then place the armies in round robbin fashion.
         */
        if(isMoreArmiesAvailable) {
            final HashMap<Integer, Integer> hmPlayerUnplacedArmies = new HashMap<>();
            final HashMap<Integer, List<Country>> hmPlayerCountries= new HashMap<>();
            int totalUnplacedArmies = 0;

            for(final Player player : gamePlay.getPlayers().values()) {
                hmPlayerUnplacedArmies.put(player.getId(), player.getNoOfArmies() - player.getNoOfCountries());
                hmPlayerCountries.put(player.getId(), gamePlay.getCountryListByPlayerId(player.getId()));
                totalUnplacedArmies += player.getNoOfArmies() - player.getNoOfCountries();
            }

            while(totalUnplacedArmies != 0) {
                for(final Player player : gamePlay.getPlayers().values()) {
                    if(hmPlayerUnplacedArmies.get(player.getId()) > 0){
                        final List<Country> countryList = hmPlayerCountries.get(player.getId());
                        final int randomIndex = ThreadLocalRandom.current().nextInt(countryList.size());
                        countryList.get(randomIndex).incrementArmies(1);
                        hmPlayerUnplacedArmies.put(player.getId(), hmPlayerUnplacedArmies.get(player.getId()) - 1);
                        totalUnplacedArmies -= 1;
                    }
                }
            }
        }

    }

}
