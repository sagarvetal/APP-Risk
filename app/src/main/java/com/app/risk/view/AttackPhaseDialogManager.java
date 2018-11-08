package com.app.risk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private Button rollButton,allOutButton;
    private ArrayList<Integer> attackRollsArrayList, defendRollsArrayList;


    /**
     * This method is the main parameterized constructor of class
     *
     * @param context: To access the elements of activity
     * @param gamePlay: To access the data of the main model
     * @param attackingCountry: Attacking country object
     * @param defendingCountry: Defending country object
     * @param recyclerView: recyclerview to update the elements of adapter
     * @param countries: To update the recyclerview's arrayList
     */
    public AttackPhaseDialogManager(Context context, GamePlay gamePlay, Country attackingCountry, Country defendingCountry, RecyclerView recyclerView, ArrayList<Country> countries){
        this.context = context;
        this.gamePlay = gamePlay;
        this.attackingCountry = attackingCountry;
        this.defendingCountry = defendingCountry;
        this.recyclerView = recyclerView;
        this.countries = countries;
        Log.i("sample_text_message ","sampleaaa");
    }


    /**
     * This method inflates the Attack alert dialog of the class
     */
    public void initiateAttack(){
        final View view = View.inflate(context,R.layout.attack_alert_dialog,null);
        AlertDialog.Builder mainAlertDialogBuilder = new AlertDialog.Builder(context);
        mainAlertDialogBuilder.setView(view);
        mainAlertDialogBuilder.create();
        mainAlertDialog = mainAlertDialogBuilder.show();
        setUpViews(view);
    }

    /**
     * This method gets the reference of the elements of dialog box
     * @param view: get the reference main dialog view
     */
    public void setUpViews(View view){
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
        attackRollsArrayList = new ArrayList<>();
        defendRollsArrayList = new ArrayList<>();
        rollButton.setOnClickListener(this);
        allOutButton.setOnClickListener(this);
    }

    /**
     * This method performs the attack
     */
    public void performAttack(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Before Attack : \n");
        stringBuilder.append("Attacker : " + attackingCountry.getNoOfArmies() +
        " Defender : " + defendingCountry.getNoOfArmies() + "\n\n");

        while(!defendRollsArrayList.isEmpty() && !attackRollsArrayList.isEmpty() ){

            if(defendRollsArrayList.get(0) >= attackRollsArrayList.get(0)){
                attackingCountry.decrementArmies(1);
                stringBuilder.append("Defender : " + defendRollsArrayList.get(0) + " Attacker : " + attackRollsArrayList.get(0));
                stringBuilder.append("\n Defender wins \n");
            }
            else{
                defendingCountry.decrementArmies(1);
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

                        if(defendingCountry.getNoOfArmies() == 0) {
                            showMoveDialog();
                        }
                    }
                })
                .setTitle("Dice Rolls")
                .setCancelable(false)
                .create().show();


        attackerNumberPicker.setMaxValue(attackingCountry.getNoOfArmies() > 3 ? 3: attackingCountry.getNoOfArmies() - 1);
        defenderNumberPicker.setMaxValue(defendingCountry.getNoOfArmies() >2 ? 2 : defendingCountry.getNoOfArmies());


    }

    public void showMoveDialog(){
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

    /**
     * {@inheritDoc}
     * @param v: invoking view of the layout
     */
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

                   sortDices(attackRollsArrayList,defendRollsArrayList);
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
            if(attackingCountry.getNoOfArmies() > 1){
                performAllOutOperation();
            }
            else{
                Toast.makeText(context, "connot perform attack with one army", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method performs the automation of the attack between attacker
     * and defender
     */

    public void performAllOutOperation() {

        int max_attack = 0;
        int max_defend = 0;
        ArrayList<Integer> attackRollsArrayListAllOut = new ArrayList<>();
        ArrayList<Integer> defendRollsArrayListAllOut = new ArrayList<>();
        while(attackingCountry.getNoOfArmies() > 1 && defendingCountry.getNoOfArmies() != 0){

            attackRollsArrayListAllOut.clear();
            defendRollsArrayListAllOut.clear();

            max_attack = attackingCountry.getNoOfArmies() > 3 ? 3 : attackingCountry.getNoOfArmies() - 1;
            max_defend = defendingCountry.getNoOfArmies() > 2 ? 2 : defendingCountry.getNoOfArmies();
            for(int i=0;i<max_attack;i++){
                attackRollsArrayListAllOut.add(generateRandom(1,6));
            }
            for(int i=0;i<max_defend;i++){
                defendRollsArrayListAllOut.add(generateRandom(1,6));
            }

            performAllOutAttack(attackRollsArrayListAllOut,defendRollsArrayListAllOut);
        }

        if(defendingCountry.getNoOfArmies() == 0){
            showMoveDialog();
        }
        else{
            mainAlertDialog.dismiss();
        }
        recyclerView.getAdapter().notifyDataSetChanged();

    }


    /**
     * This method reduce the number of armies from the attacker and
     * defender seeing as who wins or looses
     * @param attackRollsArrayListAllOut : Attacking dice rolls
     * @param defendRollsArrayListAllOut : Defending dice rolls
     */
    public void performAllOutAttack(ArrayList<Integer> attackRollsArrayListAllOut, ArrayList<Integer> defendRollsArrayListAllOut){
        StringBuilder stringBuilder = new StringBuilder();

        sortDices(attackRollsArrayListAllOut,defendRollsArrayListAllOut);

        stringBuilder.append("\nBefore Attack\n");
        stringBuilder.append("Attacker armies : " + attackingCountry.getNoOfArmies()
         + "  Defending country armies : " +defendingCountry.getNoOfArmies() + "\n");
        while(!defendRollsArrayListAllOut.isEmpty() && !attackRollsArrayListAllOut.isEmpty() ){

            if(defendRollsArrayListAllOut.get(0) >= attackRollsArrayListAllOut.get(0)){
                attackingCountry.decrementArmies(1);
                stringBuilder.append("Defender : " + defendRollsArrayListAllOut.get(0) + " Attacker : " + attackRollsArrayListAllOut.get(0));
                stringBuilder.append("\n Defender wins \n");
            }
            else{
                defendingCountry.decrementArmies(1);
                stringBuilder.append("Defender : " + defendRollsArrayListAllOut.get(0) + " Attacker : " + attackRollsArrayListAllOut.get(0));
                stringBuilder.append("\n Attacker wins \n");
            }


            stringBuilder.append("\nAfter Attack\n");
            stringBuilder.append("Attacker armies : " + attackingCountry.getNoOfArmies()
                    + "  Defending country armies : " +defendingCountry.getNoOfArmies() + "\n");

            defendRollsArrayListAllOut.remove(0);
            attackRollsArrayListAllOut.remove(0);

            Log.i("AllOutMode", stringBuilder.toString().trim());
        }
    }

    /**
     * Generates a random number for dice
     * @param lower : lower bound for the dice
     * @param upper: upper bound for the dice
     * @return
     */
    public int generateRandom(int lower,int upper){
        return (int)((Math.random() * upper) + lower);
    }

    /**
     * Sorts the dice arraylist in increasing order
     */

    public void sortDices(ArrayList<Integer> attackerArrayList,ArrayList<Integer> defenderArrayList){
        Collections.sort(attackerArrayList,Collections.<Integer>reverseOrder());
        Collections.sort(defenderArrayList,Collections.<Integer>reverseOrder());
    }
}
