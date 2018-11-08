package com.app.risk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.adapters.CardExchangeAdapter;
import com.app.risk.controller.CardExchangeController;
import com.app.risk.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardExchangeDialog extends AlertDialog {

    private Context context;
    CardExchangeController cardExchangeController;
    List<Card> cardList;

    public CardExchangeDialog(@NonNull Context context, CardExchangeController cardExchangeController) {
        super(context);
        this.context = context;
        this.cardExchangeController = cardExchangeController;
        cardList = cardExchangeController.getCardList();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_exchange);
        setUp();
    }

    public void setUp() {

        final ListView exchangeCards = findViewById(R.id.player_exchange_cardlist);
        Button btnCancel = findViewById(R.id.btn_card_exchange_cancel);
        Button btnOk = findViewById(R.id.btn_card_exchange_ok);
        final CardExchangeAdapter cardExchangeAdapter = new CardExchangeAdapter(cardList, context);
        exchangeCards.setAdapter(cardExchangeAdapter);

        exchangeCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cardList.get(position).setSelected(!cardList.get(position).isSelected());
                cardExchangeAdapter.notifyDataSetChanged();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardList.size()>=5){
                    Toast.makeText(context, "More than 5 cards available, need to exchange!", Toast.LENGTH_SHORT).show();
                    System.out.println("Have to exchange cards");
                } else {
                    CardExchangeDialog.super.dismiss();
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Card> selectedCards = new ArrayList<>();
                for(Card card: cardList){
                    if(card.isSelected())
                        selectedCards.add(card);
                }
                if(selectedCards.size()==3){
                    String message = cardExchangeController.exchangeArmiesForCards(selectedCards) +
                            " armies have been exchanged for cards.";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else if (selectedCards.size()<3){
                    Toast.makeText(context, "Less than 3 cards have been selected!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "More than 3 cards have been selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
