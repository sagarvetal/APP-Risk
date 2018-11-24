package com.app.risk.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.app.risk.Interfaces.PhaseManager;
import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;
import com.app.risk.utility.LogManager;
import com.app.risk.view.MainScreenActivity;

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
    private PhaseManager phaseManager;
    private boolean flagTransfer = true;
    private final RecyclerView recyclerView;


    /**
     * This is the paramerized constructor
     * @param context It is used to call any activity methods using reference
     * @param gamePlay The GamePlay object
     * @param recyclerView
     */
    public PlayScreenRVAdapter(Context context, GamePlay gamePlay, RecyclerView recyclerView) {
        this.context = context;
        this.gamePlay = gamePlay;
        this.countries = gamePlay.getCountryListByPlayerId(gamePlay.getCurrentPlayer().getId());
        this.neighbouringCountries = new ArrayList<>();
        this.recyclerView = recyclerView;
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
            if(v == cardView){
                switch (gamePlay.getCurrentPhase()) {
                    case GamePlayConstants.REINFORCEMENT_PHASE:
                        ReinforcementPhaseController.getInstance().showReinforcementDialogBox(getAdapterPosition(), countries);
                        break;

                    case GamePlayConstants.ATTACK_PHASE:
                        Toast.makeText(context, "Select Country from List", Toast.LENGTH_SHORT).show();
                        break;

                    case GamePlayConstants.FORTIFICATION_PHASE:
                        FortificationPhaseController.getInstance().init(context, gamePlay).showFortificationDialogBox(getAdapterPosition(),countries);
                        break;
                }
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (gamePlay.getCurrentPhase()) {
                case GamePlayConstants.ATTACK_PHASE:
                    final Player attacker = gamePlay.getCurrentPlayer();
                    final Player defender = gamePlay.getCountries().get(adjacentCountries.getAdapter().getItem(position)).getPlayer();
                    final Country attackingCountry = countries.get(getAdapterPosition());
                    final Country defendingCountry = gamePlay.getCountries().get(adjacentCountries.getAdapter().getItem(position));

                    LogManager.getInstance().writeLog(gamePlay.getCurrentPlayer().getName()+" wants to attack from "+attackingCountry.getNameOfCountry()+" to "+defendingCountry.getNameOfCountry());
                    if(attacker.equals(defender)){
                        LogManager.getInstance().writeLog("Attacker can not attack on their own country");
                        Toast.makeText(context, "Attacker can not attack on their own country", Toast.LENGTH_SHORT).show();
                    } else{
                        if(attackingCountry.getNoOfArmies() > 1){
                            AttackPhaseController.getInstance().init(context, gamePlay).initiateAttack(attackingCountry, defendingCountry, recyclerView, countries);
                        } else{
                            LogManager.getInstance().writeLog("Attacking country must have more than one armies");
                            Toast.makeText(context, "Attacking country must have more than one armies", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(gamePlay.getCurrentPlayer().isPlayerWon(gamePlay.getCountries())) {
                        LogManager.getInstance().writeLog(gamePlay.getCurrentPlayer().getName() + " has won the game.");
                        Toast.makeText(context, "Congratulations!!! You won the game.", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(context)
                                .setMessage("You won the game!!!")
                                .setNeutralButton( "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        context.startActivity(new Intent(context.getApplicationContext(), MainScreenActivity.class));
                                    }
                                })
                                .setTitle("Congratulations!!!")
                                .setCancelable(false)
                                .create().show();

                    } else if(!gamePlay.getCurrentPlayer().isMoreAttackPossible(gamePlay, countries)) {
                        LogManager.getInstance().writeLog(gamePlay.getCurrentPlayer().getName() + " can not do more attacks as do not have enough armies.");
                        Toast.makeText(context, "You can not do more attacks as you do not have enough armies.", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(context)
                                .setMessage("You can not do more attacks as you do not have enough armies.")
                                .setNeutralButton( "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        phaseManager.changePhase(GamePlayConstants.FORTIFICATION_PHASE);
                                    }
                                })
                                .setTitle("Alert")
                                .setCancelable(false)
                                .create().show();
                    }

                    break;
            }
        }
    }

    /**
     * This method returns the current phase of the game
     */
    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    /**
     * This method sets the current phase of the game
     */
    public void setPhaseManager(PhaseManager phaseManager) {
        this.phaseManager = phaseManager;
    }

}
