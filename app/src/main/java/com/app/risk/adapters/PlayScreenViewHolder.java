package com.app.risk.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.app.risk.R;

public class PlayScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CardView cardView;
    private TextView countryName,armies,continent;
    private ListView adjacentCountries;
    private View view;

    public PlayScreenViewHolder(View itemView) {
        super(itemView);
        view = itemView;

        cardView = itemView.findViewById(R.id.play_screen_card_carview);
        countryName = itemView.findViewById(R.id.play_screen_card_country_name);
        armies = itemView.findViewById(R.id.play_screen_card_armies);
        continent = itemView.findViewById(R.id.play_screen_card_continent);
        adjacentCountries = itemView.findViewById(R.id.play_screen_card_neighbours);


        cardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
