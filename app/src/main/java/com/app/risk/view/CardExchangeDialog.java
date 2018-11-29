package com.app.risk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.app.risk.model.Card;
import com.app.risk.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Dialog which provides UI to facilitate exchange of cards by player
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class CardExchangeDialog extends AlertDialog implements Observer {

    private Context context;
    private Player player;
    List<Card> cardList;

    /**
     * Default Construtor
     * @param context current application context
     * @param player current player
     */
    public CardExchangeDialog(@NonNull Context context, Player player) {
        super(context);
        this.context = context;
        this.player = player;
        cardList = player.getCards();
        player.addObserver(this);
    }

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_exchange);
        setUp();
    }

    /**
     * Class to set up the dialog with the adapter, buttons, bind it with data and provide listeners.
     */
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

                if(cardList.size()==0){
                    CardExchangeDialog.super.dismiss();
                } else {
                    if (selectedCards.size() == 3) {
                        int armiesAwarded = player.exchangeArmiesForCards(selectedCards);
                        if(armiesAwarded != -1) {
                            String message = armiesAwarded + " armies have been exchanged for cards.";
                            new AlertDialog.Builder(context)
                                    .setTitle("Cards exchanged").setMessage(message)
                                    .setPositiveButton("Ok", new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            CardExchangeDialog.super.dismiss();
                                        }
                                    }).create().show();
                        } else {
                            Toast.makeText(context, "Selected cards can't be exchanged.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (selectedCards.size() < 3) {
                        Toast.makeText(context, "Less than 3 cards have been selected!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "More than 3 cards have been selected!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     * @param o Object to be observed
     * @param arg value that is changed of type object
     */
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Player){
            cardList = player.getCards();
            setUp();
        }
    }
}
