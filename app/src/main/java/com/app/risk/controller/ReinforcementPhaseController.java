package com.app.risk.controller;

import android.content.Context;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.GamePlay;

/**
 * This class is used to start the reinforcement phase.
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
        gamePlay.setCurrentPhase(GamePlayConstants.REINFORCEMENT_PHASE);
        gamePlay.setCurrentPlayer();
        gamePlay.getCurrentPlayer().reinforcementPhase(gamePlay);
    }

}
