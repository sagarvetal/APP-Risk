package com.app.risk.controller;

import com.app.risk.model.Card;
import com.app.risk.model.GamePlay;

import java.util.List;

public class CardExchangeController {

    GamePlay gamePlay;
    Card card;

    public CardExchangeController(GamePlay gamePlay) {
        this.gamePlay = gamePlay;
        this.card = new Card();
    }

    public int exchangeArmiesForCards(List<Card> cardsToExchange){

        if(card.cardsExchangeable(cardsToExchange)){
            gamePlay.setArmiesInExchangeOfCards(gamePlay.getArmiesInExchangeOfCards() + 5);
        }

        return gamePlay.getArmiesInExchangeOfCards();
    }
}
