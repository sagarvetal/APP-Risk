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

public class CardExchangeAdapter extends BaseAdapter {

    List<Card> cardsOfPlayer;
    private Context context;

    public CardExchangeAdapter(List<Card> cardsOfPlayer, Context context) {
        this.cardsOfPlayer = cardsOfPlayer;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cardsOfPlayer.size();
    }

    @Override
    public Card getItem(int position) {
        return cardsOfPlayer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_row_card_exchange, parent, false);

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
