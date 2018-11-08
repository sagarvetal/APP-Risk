package com.app.risk.controller;

import com.app.risk.model.Card;
import com.app.risk.model.GamePlay;

import java.util.List;

public class CardExchangeController {

    GamePlay gamePlay;

    public CardExchangeController(GamePlay gamePlay) {
        this.gamePlay = gamePlay;
    }

    public List<Card> getCardList(){
        return gamePlay.getCards();
    }

    public int exchangeArmiesForCards(List<Card> cardsToExchange){

        if(gamePlay.cardsExchangeable(cardsToExchange)){
            gamePlay.setArmiesInExchangeOfCards(gamePlay.getArmiesInExchangeOfCards() + 5);
            removeExchangedCards(cardsToExchange);
        }

        return gamePlay.getArmiesInExchangeOfCards();
    }

    public void removeExchangedCards(List<Card> cardsToExchange){

        gamePlay.getCards().removeAll(cardsToExchange);
    }
}
