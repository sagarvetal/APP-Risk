package com.app.risk.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.risk.R;
import com.app.risk.model.Card;

import java.util.List;

/**
 * Adapter class to display the list of cards owned by the player to facilitate exchange
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class CardExchangeAdapter extends BaseAdapter {

    /**
     * cardsOfPlayer: To hold the list of card
     */

    List<Card> cardsOfPlayer;

    /**
     * context: Instance of invoking activity
     */
    private Context context;

    /**
     * Default constructor
     * @param cardsOfPlayer list of cards owned by the player
     * @param context application context
     */
    public CardExchangeAdapter(List<Card> cardsOfPlayer, Context context) {
        this.cardsOfPlayer = cardsOfPlayer;
        this.context = context;
    }

    /**
     * {@inheritDoc}
     * @return count of number of cards of player
     */
    @Override
    public int getCount() {
        return cardsOfPlayer.size();
    }

    /**
     * {@inheritDoc}
     * @param position index of card
     * @return card at index passed as parameter
     */
    @Override
    public Card getItem(int position) {
        return cardsOfPlayer.get(position);
    }

    /**
     * {@inheritDoc}
     * @param position
     * @return position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the view after binding cards to the view
     * @param position index of card
     * @param convertView convert view
     * @param parent parent
     * @return view
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_row_cards_exchange, parent, false);

        TextView txtCardType = view.findViewById(R.id.txt_card_type);
        txtCardType.setText(getItem(position).getType());

        if(getItem(position).isSelected()){
            txtCardType.setBackgroundColor(Color.GREEN);
        } else {
            txtCardType.setBackgroundColor(Color.WHITE);
        }

        return view;
    }
}
