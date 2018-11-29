package com.app.risk.controller;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.Card;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import java.util.ArrayList;
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

    /**
     * Default constructor
     */
    private CardExchangeController(){}

    /**
     * This method implements singleton pattern for CardExchangeController
     * @return Static reference of CardExchangeController
     */
    public static CardExchangeController getInstance() {
        if(cardExchangeController == null) {
            cardExchangeController = new CardExchangeController();
        }
        return cardExchangeController;
    }

    /**
     * This method implements singleton pattern for CardExchangeController and sets the player object
     * @param player Player object
     * @return Static reference of CardExchangeController
     */
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

    /**
     * This method implements card exchange for computer strategy players without user interaction by choosing three
     * most suitable cards for exchange (based on the rules).
     * It performs the exchange based on the cards automatically chosen and awards the player the appropriate number of
     * armies received in exchange for those cards and removes those cards from the player's list of cards.
     */
    public void exchangeCardsStrategyImplementation(){

        List<Card> cardList = player.getCards();
        int infantryCardCount = 0;
        int cavalryCardCount = 0;
        int artilleryCardCount = 0;
        for(int i=0; i<cardList.size(); i++){
            if(cardList.get(i).getType().equals(GamePlayConstants.ARTILLERY_CARD)){
                artilleryCardCount++;
                if(artilleryCardCount == 3)
                    break;
            } else if(cardList.get(i).getType().equals(GamePlayConstants.CAVALRY_CARD)){
                cavalryCardCount++;
                if(cavalryCardCount == 3)
                    break;
            } else if(cardList.get(i).getType().equals(GamePlayConstants.INFANTRY_CARD)){
                infantryCardCount++;
                if(infantryCardCount == 3)
                    break;
            }
        }
        if(artilleryCardCount == 3 || cavalryCardCount == 3 || infantryCardCount == 3 ||
                (artilleryCardCount>=1 && cavalryCardCount>=1 && infantryCardCount>=1)){
            player.setArmiesInExchangeOfCards(player.getArmiesInExchangeOfCards() + 5);
            player.incrementArmies(player.getArmiesInExchangeOfCards());
            player.setReinforcementArmies(player.getReinforcementArmies() + player.getArmiesInExchangeOfCards());

            List<Card> cardsToRemove = new ArrayList<>();
            for(int i=0; i<cardList.size(); i++){
                if(artilleryCardCount == 3 && cardList.get(i).getType().equals(GamePlayConstants.ARTILLERY_CARD))
                    cardsToRemove.add(cardList.get(i));
                else if(cavalryCardCount == 3 && cardList.get(i).getType().equals(GamePlayConstants.CAVALRY_CARD))
                    cardsToRemove.add(cardList.get(i));
                else if(infantryCardCount == 3 && cardList.get(i).getType().equals(GamePlayConstants.INFANTRY_CARD))
                    cardsToRemove.add(cardList.get(i));
            }
            if(cardsToRemove.size() == 3){
                removeExchangedCards(cardsToRemove);
            } else {
                cardsToRemove.clear();
                for(int i=0; i<cardList.size(); i++) {
                    if (cardsToRemove.size() > 0 && cardsToRemove.contains(cardList.get(i)))
                        break;
                    else
                        cardsToRemove.add(cardList.get(i));
                }
                removeExchangedCards(cardsToRemove);
            }
        }
    }
}
