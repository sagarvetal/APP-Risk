package com.app.risk.impl;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.controller.PhaseViewController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A concrete strategy class that implements a random player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class RandomPlayerStrategy implements Strategy,Serializable {

    /**
     * random: To generate random numbers
     */
    private Random random = new Random();

    /**
     * This is reinforcement method for random strategy player.
     * It places the random no of reinforcement armies on random country player owns.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param toCountry The country where player placing the reinforcement armies.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country toCountry) {
        while(player.getReinforcementArmies()!=0){
            final Country reinforcementCountry = countriesOwnedByPlayer.get(random.nextInt(countriesOwnedByPlayer.size()));
            int noOfArmies = random.nextInt(player.getReinforcementArmies());
            if(noOfArmies == 0){
                noOfArmies = 1;
            }
            reinforcementCountry.incrementArmies(noOfArmies);
            player.decrementReinforcementArmies(noOfArmies);
            PhaseViewController.getInstance().addAction(player.getName() + " has placed " + noOfArmies + " armies on " + reinforcementCountry.getNameOfCountry());
        }
        PhaseViewController.getInstance().addAction(player.getName() + " has placed all his reinforcement armies.");
    }

    /**
     * This is attack method for random strategy player.
     * It attacks a random no of times on a random country.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param attackingCountry The attacker country
     * @param defendingCountry The defender country
     */
    @Override
    public void attackPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country attackingCountry, final Country defendingCountry) {
        int numberOfTurns = random.nextInt(gamePlay.getCountries().values().size() - countriesOwnedByPlayer.size());
        if(numberOfTurns == 0){
            numberOfTurns = 1;
        }
        for (int i = 0; i < numberOfTurns; i++) {
            int fromCountryIndex = random.nextInt(countriesOwnedByPlayer.size());
            Country fromCountry = countriesOwnedByPlayer.get(fromCountryIndex);
            if(fromCountry.getNoOfArmies() < 2){
                continue;
            }
            List<Country> attackableCountries = new ArrayList<>();
            List<String> adjacentCountries = fromCountry.getAdjacentCountries();
            for (int j = 0; j < adjacentCountries.size(); j++) {
                if (player.getId() != gamePlay.getCountries().get(adjacentCountries.get(j)).getPlayer().getId()) {
                    attackableCountries.add(gamePlay.getCountries().get(adjacentCountries.get(j)));
                }
            }
            if(attackableCountries.size()==0) {
                continue;
            }

            int toCountryIndex = random.nextInt(attackableCountries.size());
            Country toCountry = attackableCountries.get(toCountryIndex);
            performAllOutAttack(fromCountry, toCountry, player, countriesOwnedByPlayer);
            if(player.isPlayerWon(gamePlay.getCountries())) {
                PhaseViewController.getInstance().addAction(player.getName() + " has conquered the entire map.");
                player.setPlayerWon(true);
                break;
            } else if(!(player.isMoreAttackPossible(gamePlay, countriesOwnedByPlayer))) {
                PhaseViewController.getInstance().addAction("No more attack possible by "+player.getName());
                break;
            }
        }
    }

    /**
     * Method to perform all out attack from a country to another country till either the attacking country has only 1 army
     * or till the defending country has no armies remaining.
     * @param fromCountry Attacking country
     * @param toCountry Defending country
     * @param player Current player
     * @param countriesOwnedByPlayer All countries owned by the player
     */
    public void performAllOutAttack(final Country fromCountry, final Country toCountry, final Player player, final List<Country> countriesOwnedByPlayer){
        int attackingDiceRoll = 0;
        PhaseViewController.getInstance().addAction("\nAttacking country: "+fromCountry.getNameOfCountry());
        PhaseViewController.getInstance().addAction("Defending country: "+toCountry.getNameOfCountry());
        final StringBuilder attackResult = new StringBuilder();
        while(fromCountry.getNoOfArmies() > 1 || toCountry.getNoOfArmies() > 0) {
            attackingDiceRoll = random.nextInt(fromCountry.getNoOfArmies() > 3 ? 3 : fromCountry.getNoOfArmies()-1);
            if(attackingDiceRoll == 0){
                attackingDiceRoll = 1;
            }
            int defendingDiceRoll = AttackPhaseController.getInstance().getDefenderDices(toCountry);
            final StringBuilder result = player.performAttack(fromCountry, toCountry, attackingDiceRoll, defendingDiceRoll);
            attackResult.append(result);
        }
        PhaseViewController.getInstance().addAction(attackResult.toString());
        if(toCountry.getNoOfArmies() == 0){
            int noOfArmiesToMove = 1;
            if(attackingDiceRoll > fromCountry.getNoOfArmies()){
                noOfArmiesToMove = fromCountry.getNoOfArmies() - 1;
            } else {
                noOfArmiesToMove = attackingDiceRoll;
            }
            player.setNewCountryConquered(true);
            PhaseViewController.getInstance().addAction(player.getName() + " conquered " + toCountry.getNameOfCountry());
            PhaseViewController.getInstance().addAction(noOfArmiesToMove +" armies moved from " + fromCountry.getNameOfCountry() + " to " + toCountry.getNameOfCountry());
            toCountry.getPlayer().decrementCountries(1);
            fromCountry.decrementArmies(noOfArmiesToMove);
            toCountry.setNoOfArmies(noOfArmiesToMove);
            toCountry.setPlayer(player);
            player.incrementCountries(1);
            countriesOwnedByPlayer.add(toCountry);
        }
    }

    /**
     * This is fortification method for random strategy player.
     * It fortifies a random country.@param gamePlay The GamePlay object.
     * @param player The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param fromCountry The country from where player wants to move armies.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country fromCountry) {
        Collections.shuffle(countriesOwnedByPlayer);
        boolean isFortificationDone = false;
        for(final Country randomFromCountry : countriesOwnedByPlayer) {
            if (randomFromCountry.getNoOfArmies() > 1) {
                final List<String> reachableCountries = FortificationPhaseController.getInstance().getReachableCountries(randomFromCountry, countriesOwnedByPlayer,false);
                if(reachableCountries.size() > 0) {
                    final Country toCountry = gamePlay.getCountries().get(reachableCountries.get(random.nextInt(reachableCountries.size())));
                    int noOfArmies = random.nextInt(randomFromCountry.getNoOfArmies());
                    if(noOfArmies == 0){
                        noOfArmies = 1;
                    }
                    randomFromCountry.decrementArmies(noOfArmies);
                    toCountry.incrementArmies(noOfArmies);
                    isFortificationDone = true;
                    PhaseViewController.getInstance().addAction(noOfArmies + "moved from " + randomFromCountry + " to " + toCountry);
                    break;
                }
            }
        }

        if(!isFortificationDone) {
            PhaseViewController.getInstance().addAction("Fortification was not possible.");
        }

        if(player.isNewCountryConquered()) {
            PhaseViewController.getInstance().addAction("Cards being assigned to "+player.getName());
            player.assignCards(gamePlay);
        }
    }

}
