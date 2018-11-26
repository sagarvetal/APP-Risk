package com.app.risk.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;

import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.utility.LogManager;
import com.app.risk.view.PlayScreenActivity;

import java.util.ArrayList;

/**
 * This class is used for the reinforcement phase.
 * It calculates the reinforcement armies for current player whose turn is going on.
 *
 * @author Sagar Vetal
 * @version 2.0.0 (Date: 05/11/2018)
 */
public class ReinforcementPhaseController {

    private GamePlay gamePlay;
    private Context context;
    private static ReinforcementPhaseController reinforcementPhaseController;

    /**
     * This is default constructor.
     */
    private ReinforcementPhaseController() {
    }

    /**
     * This method implements the singleton pattern for ReinforcementPhaseController
     * @return The static reference of ReinforcementPhaseController.
     */
    public static ReinforcementPhaseController getInstance(){
        if(reinforcementPhaseController == null){
            reinforcementPhaseController = new ReinforcementPhaseController();
        }
        return reinforcementPhaseController;
    }

    /**
     * This method implements the singleton pattern for ReinforcementPhaseController and
     * also sets GamePlay and Context object.
     * @param gamePlay The GamePlay object
     * @param context The Context object
     * @return The static reference of ReinforcementPhaseController.
     */
    public static ReinforcementPhaseController init(final Context context, final GamePlay gamePlay) {
        getInstance();
        reinforcementPhaseController.context = context;
        reinforcementPhaseController.gamePlay = gamePlay;
        return reinforcementPhaseController;
    }

    /**
     * This method starts the reinforcement phase.
     */
    public void start() {
        gamePlay.getCurrentPlayer().setTotalReinforcementArmies(gamePlay);
    }

    /**
     * This method shows the dailog box to place the reinforcement armies.
     * @param toCountry The country where player wants to place the reinforcement armies.
     * @param countries The list of countries owned by current player.
     */
    public void showReinforcementDialogBox(final Country toCountry, final ArrayList<Country> countries){
        final AlertDialog.Builder reinforcementDialogBox = new AlertDialog.Builder(context);
        reinforcementDialogBox.setTitle("Place Armies");
        LogManager.getInstance().writeLog(gamePlay.getCurrentPlayer().getName() + " is placing reinforcement armies on " + toCountry.getNameOfCountry());

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
                toCountry.incrementArmies(numberPicker.getValue());

                LogManager.getInstance().writeLog(gamePlay.getCurrentPlayer().getName() + " has placed " + numberPicker.getValue() + " armies on " + toCountry.getNameOfCountry());

                getActivity().notifyPlayScreenRVAdapter();
                if(gamePlay.getCurrentPlayer().getReinforcementArmies() == 0){
                    LogManager.getInstance().writeLog(gamePlay.getCurrentPlayer().getName() + " has placed all his reinforcement armies.");
                    getActivity().changePhase(GamePlayConstants.ATTACK_PHASE);
                }
            }
        });
        reinforcementDialogBox.setNegativeButton("Cancel",null);
        reinforcementDialogBox.create();
        reinforcementDialogBox.show();
    }


    /**
     * This method cast the context and returns PlayScreenActivity object.
     * @return The PlayScreenActivity object.
     */
    public PlayScreenActivity getActivity() {
        return (PlayScreenActivity) context;
    }
}
