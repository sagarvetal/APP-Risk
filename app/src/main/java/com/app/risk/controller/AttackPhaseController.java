package com.app.risk.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.utility.LogManager;

import java.util.ArrayList;

/**
 * This class is used for the attack phase.
 * Player can attack from a country he owns to adjacent country owned by another player.
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 06/11/2018)
 */
public class AttackPhaseController implements View.OnClickListener {

    private static AttackPhaseController attackPhaseController;
    private GamePlay gamePlay;
    private Context context;

    private ArrayList<Country> countries;
    private Country attackingCountry, defendingCountry;

    private RecyclerView recyclerView;
    private AlertDialog mainAlertDialog;
    private Button rollButton,allOutButton;
    private int variable = -1;

    private NumberPicker attackerNumberPicker, defenderNumberPicker;

    /**
     * This is default constructor.
     */
    private AttackPhaseController() {
    }

    /**
     * This method implements the singleton pattern for AttackPhaseController
     * @return The static reference of AttackPhaseController.
     */
    public static AttackPhaseController getInstance() {
        if(attackPhaseController == null) {
            attackPhaseController = new AttackPhaseController();
        }
        return attackPhaseController;
    }

    /**
     * This method implements the singleton pattern for AttackPhaseController and
     * also sets GamePlay and Context object.
     * @param gamePlay The GamePlay object
     * @param context The Context object
     * @return The static reference of AttackPhaseController.
     */
    public static AttackPhaseController init(final Context context, final GamePlay gamePlay) {
        getInstance();
        attackPhaseController.context = context;
        attackPhaseController.gamePlay = gamePlay;
        return attackPhaseController;
    }

    /**
     * This method inflates the Attack dialog box
     */
    public void initiateAttack(final Country attackingCountry, final Country defendingCountry, final RecyclerView recyclerView, final ArrayList<Country> countries){
        this.attackingCountry = attackingCountry;
        this.defendingCountry = defendingCountry;
        this.recyclerView = recyclerView;
        this.countries = countries;

        final View view = View.inflate(context,R.layout.attack_alert_dialog,null);
        final AlertDialog.Builder mainAlertDialogBuilder = new AlertDialog.Builder(context);
        mainAlertDialogBuilder.setView(view);
        mainAlertDialogBuilder.create();
        mainAlertDialog = mainAlertDialogBuilder.show();
        setUpViews(view);
    }

    /**
     * This method gets the reference of the elements of dialog box
     * @param view It gets the reference main dialog view
     */
    private void setUpViews(final View view){
        attackerNumberPicker = (NumberPicker) view.findViewById(R.id.attack_alert_dialog_attacker_picker);
        attackerNumberPicker.setMinValue(1);
        attackerNumberPicker.setMaxValue(attackingCountry.getNoOfArmies() > 3 ? 3: attackingCountry.getNoOfArmies() - 1);
        attackerNumberPicker.setWrapSelectorWheel(false);

        defenderNumberPicker = (NumberPicker) view.findViewById(R.id.attack_alert_dialog_defender_picker);
        defenderNumberPicker.setMinValue(1);
        defenderNumberPicker.setMaxValue(defendingCountry.getNoOfArmies() >2 ? 2:defendingCountry.getNoOfArmies());
        defenderNumberPicker.setWrapSelectorWheel(false);

        rollButton = view.findViewById(R.id.attack_alert_dialog_roll);
        allOutButton = view.findViewById(R.id.attack_alert_dialog_all_out);
        rollButton.setOnClickListener(this);
        allOutButton.setOnClickListener(this);
    }

    /**
     * {@inheritDoc}
     * @param v: invoking view of the layout
     */
    @Override
    public void onClick(View v) {

        if(v == rollButton || v == allOutButton){
            final StringBuilder attackResult = new StringBuilder();
            if(v == rollButton){
                final int noOfAttackerDice = attackerNumberPicker.getValue();
                final int noOfDefenderDice = defenderNumberPicker.getValue();

                LogManager.getInstance().writeLog("No of dice selected for attacker : " + noOfAttackerDice);
                LogManager.getInstance().writeLog("No of dice selected for defender : " + noOfDefenderDice);

                final String result = gamePlay.getCurrentPlayer().performAttack(attackingCountry, defendingCountry, noOfAttackerDice, noOfDefenderDice).toString();
                attackResult.append(result);

                attackerNumberPicker.setMaxValue(attackingCountry.getNoOfArmies() > 3 ? 3 : attackingCountry.getNoOfArmies() - 1);
                defenderNumberPicker.setMaxValue(defendingCountry.getNoOfArmies() > 2 ? 2 : defendingCountry.getNoOfArmies());
            }
            else if(v == allOutButton){
                LogManager.getInstance().writeLog("Player has selected all out option for attack.");
                final String result = gamePlay.getCurrentPlayer().performAllOutAttack(attackingCountry, defendingCountry).toString();
                attackResult.append(result);
            }

            if(defendingCountry.getNoOfArmies() == 0) {
                LogManager.getInstance().writeLog("Player won the country " + defendingCountry.getNameOfCountry());
                attackResult.append("\n\n You won the country " + defendingCountry.getNameOfCountry() + "\n");
            } else if(attackingCountry.getNoOfArmies() == 1) {
                LogManager.getInstance().writeLog("Player lost the attack on " + defendingCountry.getNameOfCountry());
                attackResult.append("\n\n You lost the attack on " + defendingCountry.getNameOfCountry() + "\n");
            }

            final boolean isCountryConquered = defendingCountry.getNoOfArmies() == 0 ? true : false;
            showAttackResultDialogBox(isCountryConquered, attackResult.toString());
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }


    /**
     * This method displays the dialog box to show attack result.
     * @param isCountryConquered Boolean flag whether country is conquered or not.
     * @param message The attack result to show into dialog box.
     */
    private void showAttackResultDialogBox(final boolean isCountryConquered, final String message) {
        new AlertDialog.Builder(context)
            .setMessage(message.trim())
            .setNeutralButton(isCountryConquered ? "Move Armies" : "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(defendingCountry.getNoOfArmies() == 0){
                        mainAlertDialog.dismiss();
                        Toast.makeText(context, "You won the country " + defendingCountry.getNameOfCountry(), Toast.LENGTH_SHORT).show();
                        defendingCountry.getPlayer().decrementCountries(1);
                        defendingCountry.setPlayer(attackingCountry.getPlayer());
                        defendingCountry.getPlayer().incrementCountries(1);
                        countries.add(defendingCountry);
                        gamePlay.getCurrentPlayer().setNewCountryConquered(true);
                        showDialogBoxToMoveArmiesAfterAttack();
                    } else if(attackingCountry.getNoOfArmies() == 1) {
                        mainAlertDialog.dismiss();
                        Toast.makeText(context, "You lose the attack on", Toast.LENGTH_SHORT).show();
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            })
            .setTitle("Dice Rolls Result")
            .setCancelable(false)
            .create().show();
    }


    /**
     * This method displays the dialog box to move the armies after attack.
     */
    private void showDialogBoxToMoveArmiesAfterAttack(){
        final View view = View.inflate(context,R.layout.play_screen_reinforcement_option,null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(view);

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(attackerNumberPicker.getValue());
        numberPicker.setMaxValue(attackingCountry.getNoOfArmies() - 1);
        numberPicker.setWrapSelectorWheel(false);

        alertDialog.setPositiveButton("Move", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                attackingCountry.decrementArmies(numberPicker.getValue());
                defendingCountry.incrementArmies(numberPicker.getValue());

                LogManager.getInstance().writeLog(numberPicker.getValue() +" armies moved from " + attackingCountry.getNameOfCountry() +
                        " to " + defendingCountry.getNameOfCountry());

                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(context, numberPicker.getValue() +" armies moved from " + attackingCountry.getNameOfCountry() +
                        " to " + defendingCountry.getNameOfCountry(), Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Select armies to move on newly conquered country");
        alertDialog.create().show();
    }

    /**
     * Check if a country can attack based on the number of armies of the attacking country and defending country
     * @param defenderCountry defending country
     * @param attackerCountry attacking country
     * @return true if the defending country has atleast 1 army and attacking country has more than 1 army, false otherwise
     */
    public boolean canCountryAttack(Country defenderCountry,Country attackerCountry) {
        if(defenderCountry.getNoOfArmies()>=1 && attackerCountry.getNoOfArmies()>1) {
            return true;
        }
        return false;
    }

    /**
     * Check if a country is connected to another country
     * @param fromCountry from country
     * @param toCountry to country
     * @return true if a path exists between from country and to country, false otherwise
     */
    public boolean isCountryAdjacent(Country fromCountry,Country toCountry) {
        FortificationPhaseController fc = FortificationPhaseController.getInstance().init(context, gamePlay);
        return fc.isCountriesConneted(fromCountry,toCountry);
    }

    public int setUpDiceRollView(int maxDiceValue){
        final View view = View.inflate(context,R.layout.play_screen_reinforcement_option,null);

        variable = -1;
        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.human_player_selection_dialog_number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(maxDiceValue);
        numberPicker.setWrapSelectorWheel(false);
        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        variable = numberPicker.getValue();
                    }
                }).setCancelable(false);

        return variable;
    }

}
