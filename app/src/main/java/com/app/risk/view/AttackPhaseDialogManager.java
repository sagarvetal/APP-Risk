package com.app.risk.view;

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
import java.util.Collection;
import java.util.Collections;

public class AttackPhaseDialogManager implements View.OnClickListener {

    private Context context;
    private GamePlay gamePlay;
    private Country attackingCountry,defendingCountry;
    private NumberPicker attackerNumberPicker,defenderNumberPicker;
    private final RecyclerView recyclerView;
    private AlertDialog mainAlertDialog;
    private ArrayList<Country> countries;

    private TextView attackerDiceTextView, defenderDiceTextView;

    private Button rollButton,allOutButton;

    private ArrayList<Integer> attackRollsArrayList, defendRollsArrayList;

    public AttackPhaseDialogManager(Context context, GamePlay gamePlay, Country attackingCountry, Country defendingCountry, RecyclerView recyclerView, ArrayList<Country> countries){
        this.context = context;
        this.gamePlay = gamePlay;
        this.attackingCountry = attackingCountry;
        this.defendingCountry = defendingCountry;
        this.recyclerView = recyclerView;
        this.countries = countries;
    }

    public void initiateAttack(){
        final View view = View.inflate(context,R.layout.attack_alert_dialog,null);


        AlertDialog.Builder mainAlertDialogBuilder = new AlertDialog.Builder(context);
        mainAlertDialogBuilder.setView(view);
        mainAlertDialogBuilder.create();

        mainAlertDialog = mainAlertDialogBuilder.show();

        setUpViews(view);
    }

    public void setUpViews(View view){

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

    public void performAttack(){

        Collections.sort(attackRollsArrayList,Collections.<Integer>reverseOrder());
        Collections.sort(defendRollsArrayList,Collections.<Integer>reverseOrder());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Before Attack : \n");
        stringBuilder.append("Attacker : " + attackingCountry.getNoOfArmies() +
        " Defender : " + defendingCountry.getNoOfArmies() + "\n\n");

        while(!defendRollsArrayList.isEmpty() && !attackRollsArrayList.isEmpty() ){

            if(defendRollsArrayList.get(0) >= attackRollsArrayList.get(0)){
                attackingCountry.decrementReinforcementArmies(1);
                stringBuilder.append("Defender : " + defendRollsArrayList.get(0) + " Attacker : " + attackRollsArrayList.get(0));
                stringBuilder.append("\n Defender wins \n");
            }
            else{
                defendingCountry.decrementReinforcementArmies(1);
                stringBuilder.append("Defender : " + defendRollsArrayList.get(0) + " Attacker : " + attackRollsArrayList.get(0));
                stringBuilder.append("\n Attacker wins \n");
            }
            defendRollsArrayList.remove(0);
            attackRollsArrayList.remove(0);
        }


        stringBuilder.append("\nAfter Attack : \n");
        stringBuilder.append("Attacker : " + attackingCountry.getNoOfArmies() +
                " Defender : " + defendingCountry.getNoOfArmies() + "\n");

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
                                    attackingCountry.decrementReinforcementArmies(numberPicker.getValue());
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

    @Override
    public void onClick(View v) {

        if(v == rollButton){

           if(attackingCountry.getPlayer() != defendingCountry.getPlayer()){
               if(attackingCountry.getNoOfArmies() > 1){
                   int attackDice = attackerNumberPicker.getValue();
                   int defendDice = defenderNumberPicker.getValue();

                   attackRollsArrayList.clear();
                   defendRollsArrayList.clear();
                   for(int i=0;i<attackDice;i++){
                       attackRollsArrayList.add(generateRandom(1,6));
                   }
                   for(int i=0;i<defendDice;i++){
                       defendRollsArrayList.add(generateRandom(1,6));
                   }

                   attackerDiceTextView.setText(attackRollsArrayList.toString());
                   defenderDiceTextView.setText(defendRollsArrayList.toString());

                   performAttack();

               }
               else{
                   Toast.makeText(context, "Attacker must have greater than one armies", Toast.LENGTH_SHORT).show();
               }
           }
           else{
               Toast.makeText(context, "You already won the country", Toast.LENGTH_SHORT).show();
           }
        }
        else if(v == allOutButton){

        }
    }

    public int generateRandom(int lower,int upper){
        return (int)((Math.random() * upper) + lower);
    }
}
