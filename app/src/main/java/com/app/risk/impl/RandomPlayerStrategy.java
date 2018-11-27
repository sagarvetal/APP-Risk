package com.app.risk.impl;

import android.content.Context;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.CardExchangeController;
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
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param toCountry The country where player placing the reinforcement armies.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country toCountry) {
        while(player.getReinforcementArmies()!=0){
            int putCountryIndex = random.nextInt(countriesOwnedByPlayer.size()+1)-1;
            int reinforcementArmiesToPutInCountry = random.nextInt(player.getReinforcementArmies());
            countriesOwnedByPlayer.get(putCountryIndex).incrementArmies(reinforcementArmiesToPutInCountry);
            player.decrementReinforcementArmies(reinforcementArmiesToPutInCountry);
            if(player.getCards().size()>2) {
                CardExchangeController.getInstance().init(player).exchangeCardsStrategyImplementation();
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
        int numberOfTurns = random.nextInt(countriesNotBelongingToPlayer.size() + 1);
        for (int i = 0; i < numberOfTurns; i++) {
            int fromCountryIndex = random.nextInt(countriesOwnedByPlayer.size() + 1) - 1;
            Country fromCountry = countriesOwnedByPlayer.get(fromCountryIndex);
            int armiesInFromCountry = countriesOwnedByPlayer.get(fromCountryIndex).getNoOfArmies() - 1;
            List<String> adjacentCountries = fromCountry.getAdjacentCountries();
            List<Country> attackableCountries = new ArrayList<>();
            for (int j = 0; j < adjacentCountries.size(); j++) {
                if (countriesNotBelongingToPlayer.contains(gamePlay.getCountries().get(adjacentCountries.get(j))) &&
                        gamePlay.getCountries().get(adjacentCountries.get(j)).getNoOfArmies()>=1)
                    attackableCountries.add(gamePlay.getCountries().get(adjacentCountries.get(j)));
            }
            int toCountryIndex = random.nextInt(attackableCountries.size() + 1) - 1;
            Country toCountry = attackableCountries.get(toCountryIndex);
            int attackingDiceRoll = 0;
            int defendingDiceRoll;
            while(fromCountry.getNoOfArmies() == 1 || toCountry.getNoOfArmies() == 0) {
                attackingDiceRoll = random.nextInt(armiesInFromCountry > 3 ? 3 : armiesInFromCountry);
                defendingDiceRoll = 1;
                if (toCountry.getPlayer().getStrategy() instanceof HumanPlayerStrategy)
                    defendingDiceRoll = AttackPhaseController.getInstance()
                            .setUpDiceRollView(toCountry.getNoOfArmies() >= 2 ? 2 : 1);
                else if (toCountry.getPlayer().getStrategy() instanceof AggressivePlayerStrategy ||
                        toCountry.getPlayer().getStrategy() instanceof CheaterPlayerStrategy)
                    defendingDiceRoll = toCountry.getNoOfArmies() >= 2 ? 2 : 1;
                else if (toCountry.getPlayer().getStrategy() instanceof BenevolentPlayerStrategy)
                    defendingDiceRoll = 1;
                else if (toCountry.getPlayer().getStrategy() instanceof RandomPlayerStrategy)
                    defendingDiceRoll = random.nextInt(toCountry.getNoOfArmies() > 2 ? 2 : 1);
                player.performAttack(fromCountry, toCountry, attackingDiceRoll, defendingDiceRoll);
            }
            if(toCountry.getNoOfArmies() == 0){
                toCountry.setNoOfArmies(attackingDiceRoll);
                fromCountry.decrementArmies(attackingDiceRoll);
                toCountry.setPlayer(player);
                player.incrementCountries(1);
                attackableCountries.remove(toCountry);
                if(toCountry.getNoOfArmies()>=2)
                    countriesOwnedByPlayer.add(toCountry);
            }
            if(!(player.isMoreAttackPossible(gamePlay, countriesOwnedByPlayer)))
                break;
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
            int fromCountryIndex = random.nextInt(countriesOwnedByPlayer.size() + 1) - 1;
            Country fromCountryChosenRandom = countriesOwnedByPlayer.get(fromCountryIndex);
            if (fromCountryChosenRandom.getNoOfArmies() > 1) {
                List<String> reachableCountries = FortificationPhaseController.getInstance().getReachableCountries(fromCountryChosenRandom, countriesOwnedByPlayer);
                Country toCountry = gamePlay.getCountries().get(reachableCountries.get(random.nextInt(reachableCountries.size()) - 1));
                FortificationPhaseController.getInstance().fortifyCountry(fromCountryChosenRandom, toCountry, random.nextInt(fromCountryChosenRandom.getNoOfArmies()));
                break;
            }
        }
    }
}
