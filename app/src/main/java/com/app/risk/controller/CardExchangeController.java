package com.app.risk.controller;

import com.app.risk.model.Card;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import java.util.List;

/**
 * Controller class to make changes to gamePlay model based on information from the view
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class CardExchangeController {

    private static CardExchangeController cardExchangeController;
    private Player player;

    private CardExchangeController(){}

    public static CardExchangeController getInstance() {
        if(cardExchangeController == null) {
            cardExchangeController = new CardExchangeController();
        }
        return cardExchangeController;
    }

    public static CardExchangeController init(final Player player) {
        getInstance();
        cardExchangeController.player = player;
        return cardExchangeController;
    }

    /**
     * Getter method to return the player
     * @return
     */
    public Player getPlayer() {
        return player;
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
            player.setCardsExchangedInRound(true);
            player.setArmiesInExchangeOfCards(player.getArmiesInExchangeOfCards() + 5);
            player.incrementArmies(player.getArmiesInExchangeOfCards());
            player.setReinforcementArmies(player.getReinforcementArmies() + player.getArmiesInExchangeOfCards());
            removeExchangedCards(cardsToExchange);
        } else {
            return -1;
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
