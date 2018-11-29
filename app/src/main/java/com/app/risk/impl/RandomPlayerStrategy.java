package com.app.risk.impl;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A concrete strategy class that implements a random player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class RandomPlayerStrategy implements Strategy,Serializable {

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
            int countryIndex = random.nextInt(countriesOwnedByPlayer.size());
            int noOfArmies = random.nextInt(player.getReinforcementArmies());
            countriesOwnedByPlayer.get(countryIndex).incrementArmies(noOfArmies);
            player.decrementReinforcementArmies(noOfArmies);
            if(player.getCards().size()>2) {
                player.exchangeCardsStrategyImplementation();
            }
        }
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
        ArrayList<Country> countriesNotBelongingToPlayer = (ArrayList<Country>) gamePlay.getCountries().values();
        List<Country> countriesWithLessThan2Armies = new ArrayList<>();
        countriesNotBelongingToPlayer.removeAll(countriesOwnedByPlayer);
        for (int i = 0; i < countriesOwnedByPlayer.size(); i++) {
            if (countriesOwnedByPlayer.get(i).getNoOfArmies() < 2) {
                countriesWithLessThan2Armies.add(countriesOwnedByPlayer.get(i));
            }
        }
        countriesOwnedByPlayer.removeAll(countriesWithLessThan2Armies);
        int numberOfTurns = random.nextInt(countriesNotBelongingToPlayer.size());
        for (int i = 0; i < numberOfTurns; i++) {
            int fromCountryIndex = random.nextInt(countriesOwnedByPlayer.size());
            Country fromCountry = countriesOwnedByPlayer.get(fromCountryIndex);
            List<Country> attackableCountries = new ArrayList<>();
            List<String> adjacentCountries = fromCountry.getAdjacentCountries();
            for (int j = 0; j < adjacentCountries.size(); j++) {
                if (countriesNotBelongingToPlayer.contains(gamePlay.getCountries().get(adjacentCountries.get(j))) &&
                        gamePlay.getCountries().get(adjacentCountries.get(j)).getNoOfArmies()>=1)
                    attackableCountries.add(gamePlay.getCountries().get(adjacentCountries.get(j)));
            }
            if(attackableCountries.size()==0)
                continue;
            int armiesInFromCountry = countriesOwnedByPlayer.get(fromCountryIndex).getNoOfArmies() - 1;
            int toCountryIndex = random.nextInt(attackableCountries.size());
            Country toCountry = attackableCountries.get(toCountryIndex);
            performAllOutAttack(fromCountry, toCountry, armiesInFromCountry, player, attackableCountries, countriesOwnedByPlayer);
            if(player.isPlayerWon(gamePlay.getCountries())) {
                player.setPlayerWon(true);
                break;
            } else if(!(player.isMoreAttackPossible(gamePlay, countriesOwnedByPlayer))){
                break;
            }
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
        while (true) {
            int fromCountryIndex = random.nextInt(countriesOwnedByPlayer.size());
            Country fromCountryChosenRandomly = countriesOwnedByPlayer.get(fromCountryIndex);
            if (fromCountryChosenRandomly.getNoOfArmies() > 1) {
                List<String> reachableCountries = FortificationPhaseController.getInstance().getReachableCountries(fromCountryChosenRandomly, countriesOwnedByPlayer,false);
                Country toCountry = gamePlay.getCountries().get(reachableCountries.get(random.nextInt(reachableCountries.size())));
                final int noOfArmies = random.nextInt(fromCountryChosenRandomly.getNoOfArmies());
                fromCountryChosenRandomly.decrementArmies(noOfArmies);
                toCountry.incrementArmies(noOfArmies);
                break;
            }
        }
        if(player.isNewCountryConquered())
            player.assignCards(gamePlay);
    }

    /**
     * Method to perform all out attack from a country to another country till either the attacking country has only 1 army
     * or till the defending country has no armies remaining.
     * @param fromCountry Attacking country
     * @param toCountry Defending country
     * @param armiesInFromCountry Armies in the attacking country
     * @param player Current player
     * @param attackableCountries Countries that can be attacked by the attacking country
     * @param countriesOwnedByPlayer All countries owned by the player
     */
    public void performAllOutAttack(Country fromCountry, Country toCountry, int armiesInFromCountry, Player player, List<Country> attackableCountries, List<Country> countriesOwnedByPlayer){
        int attackingDiceRoll = 0;
        int defendingDiceRoll;
        while(fromCountry.getNoOfArmies() != 1 || toCountry.getNoOfArmies() != 0) {
            attackingDiceRoll = random.nextInt(armiesInFromCountry > 3 ? 3 : armiesInFromCountry);
            defendingDiceRoll = AttackPhaseController.getInstance().getDefenderDices(toCountry);
            player.performAttack(fromCountry, toCountry, attackingDiceRoll, defendingDiceRoll);
        }
        if(toCountry.getNoOfArmies() == 0){
            player.setNewCountryConquered(true);
            toCountry.setNoOfArmies(attackingDiceRoll);
            fromCountry.decrementArmies(attackingDiceRoll);
            toCountry.setPlayer(player);
            player.incrementCountries(1);
            attackableCountries.remove(toCountry);
            if(toCountry.getNoOfArmies()>=2)
                countriesOwnedByPlayer.add(toCountry);
        }
    }
}
