package com.app.risk.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import java.util.ArrayList;
import java.util.Collections;

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
    private TextView attackerDiceTextView, defenderDiceTextView;
    private Button rollButton,allOutButton;

    private NumberPicker attackerNumberPicker, defenderNumberPicker;
    private ArrayList<Integer> attackRollsArrayList, defendRollsArrayList;

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

    public void setUpViews(final View view){
        attackerNumberPicker = (NumberPicker) view.findViewById(R.id.attack_alert_dialog_attacker_picker);
        attackerNumberPicker.setMinValue(1);
        attackerNumberPicker.setMaxValue(attackingCountry.getNoOfArmies() > 3 ? 3: attackingCountry.getNoOfArmies() - 1);
        attackerNumberPicker.setWrapSelectorWheel(false);

        defenderNumberPicker = (NumberPicker) view.findViewById(R.id.attack_alert_dialog_defender_picker);
        defenderNumberPicker.setMinValue(1);
        defenderNumberPicker.setMaxValue(defendingCountry.getNoOfArmies() >2 ? 2:defendingCountry.getNoOfArmies());
        defenderNumberPicker.setWrapSelectorWheel(false);

        attackerDiceTextView = view.findViewById(R.id.attack_alert_dialog_attacker_dices);
        defenderDiceTextView = view.findViewById(R.id.attack_alert_dialog_defender_dices);

        rollButton = view.findViewById(R.id.attack_alert_dialog_roll);
        allOutButton = view.findViewById(R.id.attack_alert_dialog_all_out);

        attackRollsArrayList = new ArrayList<>();
        defendRollsArrayList = new ArrayList<>();

        rollButton.setOnClickListener(this);
        allOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == rollButton){

            if(attackingCountry.getPlayer() != defendingCountry.getPlayer()){
                if(attackingCountry.getNoOfArmies() > 1){
                    final int noOfAttackerDice = attackerNumberPicker.getValue();
                    final int noOfDefenderDice = defenderNumberPicker.getValue();

                    attackRollsArrayList.clear();
                    defendRollsArrayList.clear();

                    for(int i=0; i<noOfAttackerDice; i++){
                        attackRollsArrayList.add(generateRandom(1,6));
                    }
                    for(int i=0;i<noOfDefenderDice;i++){
                        defendRollsArrayList.add(generateRandom(1,6));
                    }

                    attackerDiceTextView.setText(attackRollsArrayList.toString());
                    defenderDiceTextView.setText(defendRollsArrayList.toString());

                    performAttack();
                }
                else{
                    Toast.makeText(context, "Attacking country must have more than one armies", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(context, "You already won the country", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v == allOutButton){

        }
    }


    public void performAttack(){
        Collections.sort(attackRollsArrayList, Collections.<Integer>reverseOrder());
        Collections.sort(defendRollsArrayList, Collections.<Integer>reverseOrder());

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Before Attack : \n");
        stringBuilder.append("Attacker : " + attackingCountry.getNoOfArmies() + " Defender : " + defendingCountry.getNoOfArmies() + "\n\n");

        while(!defendRollsArrayList.isEmpty() && !attackRollsArrayList.isEmpty() ){
            if(defendRollsArrayList.get(0) >= attackRollsArrayList.get(0)){
                attackingCountry.decrementArmies(1);
                attackingCountry.getPlayer().decrementArmies(1);
                stringBuilder.append("Defender : " + defendRollsArrayList.get(0) + " Attacker : " + attackRollsArrayList.get(0));
                stringBuilder.append("\n Defender wins \n");
            } else{
                defendingCountry.decrementArmies(1);
                defendingCountry.getPlayer().decrementArmies(1);
                stringBuilder.append("Defender : " + defendRollsArrayList.get(0) + " Attacker : " + attackRollsArrayList.get(0));
                stringBuilder.append("\n Attacker wins \n");
            }
            defendRollsArrayList.remove(0);
            attackRollsArrayList.remove(0);
        }

        stringBuilder.append("\nAfter Attack : \n");
        stringBuilder.append("Attacker : " + attackingCountry.getNoOfArmies() + " Defender : " + defendingCountry.getNoOfArmies() + "\n");

        recyclerView.getAdapter().notifyDataSetChanged();
        new AlertDialog.Builder(context)
            .setMessage(stringBuilder.toString().trim())
            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(defendingCountry.getNoOfArmies() == 0){
                        mainAlertDialog.dismiss();
                        Toast.makeText(context, "You won the country", Toast.LENGTH_SHORT).show();
                        defendingCountry.setPlayer(attackingCountry.getPlayer());
                        countries.add(defendingCountry);

                        recyclerView.getAdapter().notifyDataSetChanged();

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
                                recyclerView.getAdapter().notifyDataSetChanged();
                                Toast.makeText(context, numberPicker.getValue() +" Armies moved from " + attackingCountry.getNameOfCountry() +
                                        " to " + defendingCountry.getNameOfCountry(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.setCancelable(false);
                        alertDialog.setTitle("Select armies to move");
                        alertDialog.create().show();
                    }
                }
            })
            .setTitle("Dice Rolls")
            .setCancelable(false)
            .create().show();

        attackerNumberPicker.setMaxValue(attackingCountry.getNoOfArmies() > 3 ? 3: attackingCountry.getNoOfArmies() - 1);
        defenderNumberPicker.setMaxValue(defendingCountry.getNoOfArmies() >2 ? 2:defendingCountry.getNoOfArmies());
    }


    public int generateRandom(int lower,int upper){
        return (int)((Math.random() * upper) + lower);
    }

}
