package com.app.risk.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import java.util.ArrayList;

/**
 * PlayScreenRVAdapter Adapter which iterates the recyclerview multiple times
 * @author Himanshu Kohli
 * @version 1.0.0
 */
public class PlayScreenRVAdapter extends RecyclerView.Adapter<PlayScreenRVAdapter.PlayScreenViewHolder> {

    private GamePlay gamePlay;
    private ArrayList<Country> countries;
    private Context context;
    private ArrayList<String> neighbouringCountries;


    /**
     * This is the paramerized constructor
     * @param context It is used to call any activity methods using reference
     * @param gamePlay The GamePlay object
     * @param countriesOwnedByPlayer The list of countries owned by player.
     */
    public PlayScreenRVAdapter(final Context context, final GamePlay gamePlay, final ArrayList<Country> countriesOwnedByPlayer) {
        this.context = context;
        this.gamePlay = gamePlay;
        this.countries = countriesOwnedByPlayer;
        this.neighbouringCountries = new ArrayList<>();
    }

    /**
     * This method inflates the view to be represented in recyclerview
     *
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PlayScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_screen_card,parent,false);
        final PlayScreenViewHolder mainAdapter = new PlayScreenViewHolder(view);
        return mainAdapter;
    }

    /**
     * This method binds the data with the particular layout and
     * displays on the recyclerview
     *
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull PlayScreenViewHolder holder, int position) {
        holder.countryName.setText(countries.get(position).getNameOfCountry());
        holder.countryName.setTextColor(gamePlay.getCurrentPlayer().getColorCode());
        holder.continentName.setText(countries.get(position).getBelongsToContinent().getNameOfContinent());
        holder.armies.setText(""+countries.get(position).getNoOfArmies());

        neighbouringCountries = gamePlay.getCountries().get(countries.get(position).getNameOfCountry()).getAdjacentCountries();
        CustomArrayAdapter adapter = new CustomArrayAdapter(context, R.layout.adapters_textview,R.id.adapter_textview_text, neighbouringCountries,gamePlay);
        holder.adjacentCountries.setAdapter(adapter);

        ViewGroup.LayoutParams layoutParams = holder.adjacentCountries.getLayoutParams();
        layoutParams.height = neighbouringCountries.size() * 158;
        holder.adjacentCountries.setLayoutParams(layoutParams);
        holder.adjacentCountries.requestLayout();

    }

    /**
     * This method returns the number of items in recyclerview adapter
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return countries.size();
    }

    /**
     * Inner class of recyclerview adapter which gets the reference to
     * view holder and add functionality to the views
     */
    public class PlayScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemClickListener {

        private CardView cardView;
        private TextView countryName,armies, continentName;
        private ListView adjacentCountries;
        private View view;

        /**
         * Default constructor of the class
         * @param itemView
         *
         * {@inheritDoc}
         */
        public PlayScreenViewHolder(final View itemView) {
            super(itemView);
            view = itemView;

            cardView = itemView.findViewById(R.id.play_screen_card_carview);
            countryName = itemView.findViewById(R.id.play_screen_card_country_name);
            armies = itemView.findViewById(R.id.play_screen_card_armies);
            continentName = itemView.findViewById(R.id.play_screen_card_continent);
            adjacentCountries = itemView.findViewById(R.id.play_screen_card_neighbours);

            adjacentCountries.setOnItemClickListener(this);
            cardView.setOnClickListener(this);
        }

        /**
         * This method holds the click evenets of every view
         * {@inheritDoc}
         *
         */
        @Override
        public void onClick(View v) {
            if(v == cardView && gamePlay.getCurrentPlayer().isHuman()){
                switch (gamePlay.getCurrentPhase()) {
                    case GamePlayConstants.REINFORCEMENT_PHASE:
                        GamePlayConstants.PHASE_IN_PROGRESS = true;
                        gamePlay.getCurrentPlayer().reinforcementPhase(gamePlay, countries, countries.get(getAdapterPosition()));
                        break;

                    case GamePlayConstants.ATTACK_PHASE:
                        GamePlayConstants.PHASE_IN_PROGRESS = true;
                        Toast.makeText(context, "Select Country from adjacent country list", Toast.LENGTH_SHORT).show();
                        break;

                    case GamePlayConstants.FORTIFICATION_PHASE:
                        GamePlayConstants.PHASE_IN_PROGRESS = true;
                        gamePlay.getCurrentPlayer().fortificationPhase(gamePlay, countries, countries.get(getAdapterPosition()));
                        break;
                }
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(gamePlay.getCurrentPlayer().isHuman()) {
                switch (gamePlay.getCurrentPhase()) {
                    case GamePlayConstants.ATTACK_PHASE:
                        final Country attackingCountry = countries.get(getAdapterPosition());
                        final Country defendingCountry = gamePlay.getCountries().get(adjacentCountries.getAdapter().getItem(position));
                        gamePlay.getCurrentPlayer().attackPhase(gamePlay, countries, attackingCountry, defendingCountry);
                        break;
                }
            }
        }
    }

}
