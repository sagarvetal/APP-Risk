package com.app.risk.controller;

import com.app.risk.model.Card;
import com.app.risk.model.Player;

import java.util.List;

/**
 * Controller class to make changes to gamePlay model based on information from the view
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class CardExchangeController {

    Player player;

    /**
     * Default constructor
     * @param player current gameplay object
     */
    public CardExchangeController(Player player) {
        this.player = player;
    }

    /**
     * List of cards owned by the current player
     * @return list of cards owned by the current player
     */
    public List<Card> getCardList(){
        return player.getCards();
    }

    /**
     * Check the validity of selected cards and return the number of armies to be awarded in its exchange
     * @param cardsToExchange list of cards selected by player
     * @return number of armies in exchange of selected cards
     */
    public int exchangeArmiesForCards(List<Card> cardsToExchange){

        if(player.cardsExchangeable(cardsToExchange)){
            player.setArmiesInExchangeOfCards(player.getArmiesInExchangeOfCards() + 5);
            player.incrementArmies(player.getArmiesInExchangeOfCards());
            removeExchangedCards(cardsToExchange);
        }

        return player.getArmiesInExchangeOfCards();
    }

    /**
     * Remove the selected cards from the cards owned by the player once they are exchanged
     * @param cardsToExchange list of cards selected by the player
     */
    public void removeExchangedCards(List<Card> cardsToExchange){

        List<Card> updatedCards = player.getCards();
        updatedCards.removeAll(cardsToExchange);
        player.setCards(updatedCards);
    }
}
