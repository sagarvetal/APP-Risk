package com.app.risk.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.app.risk.Interfaces.PhaseManager;
import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import java.util.ArrayList;

public class PlayScreenRVAdapter extends RecyclerView.Adapter<PlayScreenRVAdapter.PlayScreenViewHolder> {


    private GamePlay gamePlay;
    private ArrayList<Country> countries;
    private Context context;
    private ArrayList<String> neighbouringCountries;
    private PhaseManager phaseManager;

    /**
     * The constructor of the class which sets the context and arraylist
     * of for the adapter
     * @param context It is used to call any activity methods using reference
     * @param gamePlay The GamePlay object
     */
    public PlayScreenRVAdapter(Context context, GamePlay gamePlay) {
        this.context = context;
        this.gamePlay = gamePlay;
        this.countries = gamePlay.getCountryListByPlayerId(gamePlay.getCurrentPlayer().getId());
        this.neighbouringCountries = new ArrayList<>();
    }

    /**
     *ViewHolder method inflates the view to be represented in recyclerview
     *
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public PlayScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_screen_card,parent,false);
        final PlayScreenViewHolder adapter = new PlayScreenViewHolder(view);
        return adapter;
    }

    /**
     * BindViewHolder binds the data with the particular layout and
     * displays on the recyclerview
     *
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull PlayScreenViewHolder holder, int position) {
        holder.countryName.setText(countries.get(position).getNameOfCountry());
        holder.continentName.setText(countries.get(position).getBelongsToContinent().getNameOfContinent());
        holder.armies.setText(""+countries.get(position).getNoOfArmies());

        neighbouringCountries = gamePlay.getCountries().get(countries.get(position).getNameOfCountry()).getAdjacentCountries();
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, neighbouringCountries);
        holder.adjacentCountries.setAdapter(adapter);

        ViewGroup.LayoutParams layoutParams = holder.adjacentCountries.getLayoutParams();
        layoutParams.height = neighbouringCountries.size() * 173;
        holder.adjacentCountries.setLayoutParams(layoutParams);
        holder.adjacentCountries.requestLayout();

    }

    /**
     * Returns the number of items in recyclerview adapter
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
    public class PlayScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cardView;
        private TextView countryName,armies, continentName;
        private ListView adjacentCountries;
        private View view;

        public PlayScreenViewHolder(final View itemView) {
            super(itemView);
            view = itemView;

            cardView = itemView.findViewById(R.id.play_screen_card_carview);
            countryName = itemView.findViewById(R.id.play_screen_card_country_name);
            armies = itemView.findViewById(R.id.play_screen_card_armies);
            continentName = itemView.findViewById(R.id.play_screen_card_continent);
            adjacentCountries = itemView.findViewById(R.id.play_screen_card_neighbours);

            cardView.setOnClickListener(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View v) {
            if(v == cardView){
                switch (gamePlay.getCurrentPhase()) {
                    case GamePlayConstants.REINFORCEMENT_PHASE:
                        showReinforcementDialogBox(getAdapterPosition());
                        break;

                    case GamePlayConstants.ATTACK_PHASE:
                        break;

                    case GamePlayConstants.FORTIFICATION_PHASE:



                        break;
                }
            }
        }
    }

    /**
     * This method shows the dailog box to place the reinforcement armies.
     * @param adapterPostion The position of country owned by the player.
     */
    public void showReinforcementDialogBox(final int adapterPostion){
        final AlertDialog.Builder reinforcementDialogBox = new AlertDialog.Builder(context);
        reinforcementDialogBox.setTitle("Place Armies");

        final View view = View.inflate(context,R.layout.play_screen_reinforcement_option,null);
        reinforcementDialogBox.setView(view);

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(gamePlay.getCurrentPlayer().getReinforcementArmies());
        numberPicker.setWrapSelectorWheel(false);

        reinforcementDialogBox.setPositiveButton("Place", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gamePlay.getCurrentPlayer().decrementReinforcementArmies(numberPicker.getValue());
                countries.get(adapterPostion).incrementArmies(numberPicker.getValue());
                notifyDataSetChanged();
                if(gamePlay.getCurrentPlayer().getReinforcementArmies() == 0){
                    phaseManager.changePhase(GamePlayConstants.ATTACK_PHASE);
                }
            }
        });
        reinforcementDialogBox.setNegativeButton("Cancel",null);
        reinforcementDialogBox.create();
        reinforcementDialogBox.show();
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public void setPhaseManager(PhaseManager phaseManager) {
        this.phaseManager = phaseManager;
    }
}
