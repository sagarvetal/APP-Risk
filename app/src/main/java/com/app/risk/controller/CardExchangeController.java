package com.app.risk.controller;

import com.app.risk.model.Card;
import com.app.risk.model.GamePlay;

import java.util.List;

/**
 * Controller class to make changes to gamePlay model based on information from the view
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class CardExchangeController {

    GamePlay gamePlay;

    /**
     * Default constructor
     * @param gamePlay current gameplay object
     */
    public CardExchangeController(GamePlay gamePlay) {
        this.gamePlay = gamePlay;
    }

    /**
     * List of cards owned by the current player
     * @return list of cards owned by the current player
     */
    public List<Card> getCardList(){
        return gamePlay.getCards();
    }

    /**
     * Check the validity of selected cards and return the number of armies to be awarded in its exchange
     * @param cardsToExchange list of cards selected by player
     * @return number of armies in exchange of selected cards
     */
    public int exchangeArmiesForCards(List<Card> cardsToExchange){

        if(gamePlay.cardsExchangeable(cardsToExchange)){
            gamePlay.setArmiesInExchangeOfCards(gamePlay.getArmiesInExchangeOfCards() + 5);
            removeExchangedCards(cardsToExchange);
        }

        return gamePlay.getArmiesInExchangeOfCards();
    }

    /**
     * Remove the selected cards from the cards owned by the player once they are exchanged
     * @param cardsToExchange list of cards selected by the player
     */
    public void removeExchangedCards(List<Card> cardsToExchange){

        gamePlay.getCards().removeAll(cardsToExchange);
    }
}
