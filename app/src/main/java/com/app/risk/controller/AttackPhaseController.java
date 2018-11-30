package com.app.risk.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.impl.AggressivePlayerStrategy;
import com.app.risk.impl.BenevolentPlayerStrategy;
import com.app.risk.impl.CheaterPlayerStrategy;
import com.app.risk.impl.HumanPlayerStrategy;
import com.app.risk.impl.RandomPlayerStrategy;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;
import com.app.risk.view.MainScreenActivity;
import com.app.risk.view.PlayScreenActivity;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is used for the attack phase.
 * Player can attack from a country he owns to adjacent country owned by another player.
 * @author Sagar Vetal and Himanshu Kohli
 * @version 1.0.0 (Date: 06/11/2018)
 */
public class AttackPhaseController implements View.OnClickListener {


    /**
     * attackPhaseController: singleton instance of the controller
     * gamePlay: To manage the state and retrieve data
     * context: instance of the invoking activity
     */
    private static AttackPhaseController attackPhaseController;
    private GamePlay gamePlay;
    private Context context;

    /**
     * contries: List of countries
     * attackingCountry: holds the data of attacking country
     * defendingCountry: holds the data of the defending country
     */
    private ArrayList<Country> countries;
    private Country attackingCountry, defendingCountry;

    /**
     * mainAlertDialog: To represent data in dialogbox
     * rollButton: To perform dice roll
     * allOutButton: To perform all out operation
     * attackerNumberPicker: Allows user to select the number of attacking dice
     * defendingNumberPicker: Allows user to select the number of defending dice
     * defenderDice: minimum number of defending dice value
     */
    private AlertDialog mainAlertDialog;
    private Button rollButton,allOutButton;
    private NumberPicker attackerNumberPicker, defenderNumberPicker;
    private int defenderDices = 1;

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
    public void initiateAttack(final ArrayList<Country> countries, final Country attackingCountry, final Country defendingCountry){
        this.countries = countries;
        this.attackingCountry = attackingCountry;
        this.defendingCountry = defendingCountry;
        final Player attacker = gamePlay.getCurrentPlayer();
        final Player defender = defendingCountry.getPlayer();

        PhaseViewController.getInstance().addAction(gamePlay.getCurrentPlayer().getName()+" wants to attack from "+attackingCountry.getNameOfCountry()+" to "+defendingCountry.getNameOfCountry());
        if(attacker.equals(defender)){
            PhaseViewController.getInstance().addAction("Attacker can not attack on their own country");
            Toast.makeText(context, "Attacker can not attack on their own country", Toast.LENGTH_SHORT).show();
        } else{
            if(attackingCountry.getNoOfArmies() > 1){
                showAttackView();
            } else{
                PhaseViewController.getInstance().addAction("Attacking country must have more than one armies");
                Toast.makeText(context, "Attacking country must have more than one armies", Toast.LENGTH_SHORT).show();
            }
        }

        if(gamePlay.getCurrentPlayer().isPlayerWon(gamePlay.getCountries())) {
            PhaseViewController.getInstance().addAction(gamePlay.getCurrentPlayer().getName() + " has won the game.");
            Toast.makeText(context, "Congratulations!!! You won the game.", Toast.LENGTH_SHORT).show();
            gamePlay.getCurrentPlayer().setPlayerWon(true);
            getActivity().changePhase(GamePlayConstants.FORTIFICATION_PHASE);

        } else if(!gamePlay.getCurrentPlayer().isMoreAttackPossible(gamePlay, countries)) {
            PhaseViewController.getInstance().addAction(gamePlay.getCurrentPlayer().getName() + " can not do more attacks as do not have enough armies.");
            Toast.makeText(context, "You can not do more attacks as you do not have enough armies.", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(context)
                    .setMessage("You can not do more attacks as you do not have enough armies.")
                    .setNeutralButton( "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().changePhase(GamePlayConstants.FORTIFICATION_PHASE);
                        }
                    })
                    .setTitle("Alert")
                    .setCancelable(false)
                    .create().show();
        }

    }

    /**
     * This method gets the reference of the elements of dialog box
     */
    private void showAttackView(){
        final View view = View.inflate(context,R.layout.attack_alert_dialog,null);
        final AlertDialog.Builder mainAlertDialogBuilder = new AlertDialog.Builder(context);
        mainAlertDialogBuilder.setView(view);
        mainAlertDialogBuilder.create();
        mainAlertDialog = mainAlertDialogBuilder.show();

        attackerNumberPicker = (NumberPicker) view.findViewById(R.id.attack_alert_dialog_attacker_picker);
        attackerNumberPicker.setMinValue(1);
        attackerNumberPicker.setMaxValue(attackingCountry.getNoOfArmies() > 3 ? 3: attackingCountry.getNoOfArmies() - 1);
        attackerNumberPicker.setWrapSelectorWheel(false);

        defenderNumberPicker = (NumberPicker) view.findViewById(R.id.attack_alert_dialog_defender_picker);
        defenderNumberPicker.setMinValue(1);
        defenderNumberPicker.setMaxValue(defendingCountry.getNoOfArmies() >2 ? 2:defendingCountry.getNoOfArmies());
        defenderNumberPicker.setValue(getNoOfDicesForDefender(defendingCountry));
        defenderNumberPicker.setWrapSelectorWheel(false);

        if(defendingCountry.getPlayer().getStrategy() instanceof HumanPlayerStrategy) {
            defenderNumberPicker.setEnabled(true);
        } else {
            defenderNumberPicker.setEnabled(false);
        }

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

                final String result = gamePlay.getCurrentPlayer().performAttack(attackingCountry, defendingCountry, noOfAttackerDice, noOfDefenderDice).toString();
                attackResult.append(result);

                attackerNumberPicker.setMaxValue(attackingCountry.getNoOfArmies() > 3 ? 3 : attackingCountry.getNoOfArmies() - 1);
                defenderNumberPicker.setMaxValue(defendingCountry.getNoOfArmies() > 2 ? 2 : defendingCountry.getNoOfArmies());
                defenderNumberPicker.setValue(getNoOfDicesForDefender(defendingCountry));
            }
            else if(v == allOutButton){
                PhaseViewController.getInstance().addAction("Player has selected all out option for attack.");
                final String result = gamePlay.getCurrentPlayer().performAllOutAttack(attackingCountry, defendingCountry).toString();
                attackResult.append(result);
            }

            PhaseViewController.getInstance().addAction(attackResult.toString());

            if(defendingCountry.getNoOfArmies() == 0) {
                PhaseViewController.getInstance().addAction(gamePlay.getCurrentPlayer().getName() + " conquered " + defendingCountry.getNameOfCountry());
                attackResult.append("\n\n You won the country " + defendingCountry.getNameOfCountry() + "\n");
            } else if(attackingCountry.getNoOfArmies() == 1) {
                PhaseViewController.getInstance().addAction(gamePlay.getCurrentPlayer().getName() + " lost the attack on " + defendingCountry.getNameOfCountry());
                attackResult.append("\n\n You lost the attack on " + defendingCountry.getNameOfCountry() + "\n");
            }

            final boolean isCountryConquered = defendingCountry.getNoOfArmies() == 0 ? true : false;
            showAttackResultDialogBox(isCountryConquered, attackResult.toString());
            getActivity().notifyPlayScreenRVAdapter();
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
                    getActivity().notifyPlayScreenRVAdapter();
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

                PhaseViewController.getInstance().addAction(numberPicker.getValue() +" armies moved from " + attackingCountry.getNameOfCountry() +
                        " to " + defendingCountry.getNameOfCountry());

                getActivity().notifyPlayScreenRVAdapter();
                Toast.makeText(context, numberPicker.getValue() +" armies moved from " + attackingCountry.getNameOfCountry() +
                        " to " + defendingCountry.getNameOfCountry(), Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Select armies to move on newly conquered country");
        alertDialog.create().show();
    }

    /**
     * This method cast the context and returns PlayScreenActivity object.
     * @return The PlayScreenActivity object.
     */
    public PlayScreenActivity getActivity() {
        return (PlayScreenActivity) context;
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
        return fc.isCountriesConnected(fromCountry,toCountry);
    }

    /**
     * It returns the no of dices for defender based on its strategy.
     * @param defendingCountry
     * @return The no of dices for defender based on its strategy.
     */
    public int getNoOfDicesForDefender(final Country defendingCountry){
        final Random random = new Random();
        final Strategy strategy = defendingCountry.getPlayer().getStrategy();
        if(strategy instanceof AggressivePlayerStrategy || strategy instanceof CheaterPlayerStrategy) {
            return defendingCountry.getNoOfArmies() >= 2 ? 2 : 1;
        } else if(strategy instanceof RandomPlayerStrategy){
            return random.nextInt(defendingCountry.getNoOfArmies() > 2 ? 2 : 1);
        } else {
            return 1;
        }
    }

    /**
     * It shows the dialog box for selecting defender dices for human strategy.
     * @param defendingCountry The defending country.
     * @return The selected no of dices for defender.
     */
    public int showDiceSelectionDialogBox(final Country defendingCountry){
        final View view = View.inflate(context,R.layout.human_player_selection_dialog,null);

        final NumberPicker numberPicker = view.findViewById(R.id.human_player_selection_dialog_number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(defendingCountry.getNoOfArmies() >= 2 ? 2 : 1);
        numberPicker.setWrapSelectorWheel(false);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                throw  new RuntimeException();
            }
        };

        new AlertDialog.Builder(context)
                .setView(view)
                .setTitle("Defend Country : " + defendingCountry.getNameOfCountry())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        defenderDices = numberPicker.getValue();
                        handler.sendMessage(handler.obtainMessage());
                    }
                }).setCancelable(false)
                .create().show();

        try {
            Looper.loop();
        }
        catch (RuntimeException e2){}
        return defenderDices;
    }

    /**
     * It returns the no of dices for defender based on its strategy.
     * @param defendingCountry
     * @return The no of dices for defender based on its strategy.
     */
    public int getDefenderDices(final Country defendingCountry){
        final Random random = new Random();
        int defenderDices = 1;
        final Strategy strategy =  defendingCountry.getPlayer().getStrategy();

        if (strategy instanceof HumanPlayerStrategy)
            defenderDices = showDiceSelectionDialogBox(defendingCountry);
        else if (strategy instanceof AggressivePlayerStrategy || strategy instanceof CheaterPlayerStrategy)
            defenderDices = defendingCountry.getNoOfArmies() >= 2 ? 2 : 1;
        else if (strategy instanceof BenevolentPlayerStrategy)
            defenderDices = 1;
        else if (strategy instanceof RandomPlayerStrategy)
            defenderDices = random.nextInt(defendingCountry.getNoOfArmies() > 2 ? 2 : 1);

        return defenderDices;
    }

}
