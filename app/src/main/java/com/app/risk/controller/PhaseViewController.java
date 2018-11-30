package com.app.risk.controller;


import android.content.Context;

import com.app.risk.constants.FileConstants;
import com.app.risk.model.PhaseModel;
import com.app.risk.utility.LogManager;
import com.app.risk.view.PlayScreenActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * This class manages the operate on the logs
 *like reading,writing and deleting the log
 * @author Akhila Chilukuri
 * @version 1.0.0
 */

public class PhaseViewController {
    /**
     * It is a singleton instance of the controller
     */
    private static PhaseViewController phaseViewController;

    /**
     * It is a directory path to store logs
     */
    private String dirPath;
    /**
     * logmsg stores the latest log that has to displayed on the phase view.
     *
     */
    public static PhaseModel logmsg;

    /**
     * It is an instance of the invoking activity
     */
    private Context context;

    /**
     * This is a default constructor.
     *
     */
    private PhaseViewController() {
    }

    /**
     * This is a method that returns the same single instance of the PhaseViewController class
     * when it is called
     * @return PhaseViewController single instance of the class
     */
    public static PhaseViewController getInstance() {
        if (phaseViewController == null) {
            phaseViewController = new PhaseViewController();
        }
        return phaseViewController;
    }


    /**
     * This is a method that returns the same single instance of the PhaseViewController class
     * when it is called
     * @param dirPath path in which the file is generated with all the logs
     * @param view on which the observer pattern is implemented
     * @return PhaseViewController single instance of the class
     */
    public PhaseViewController init(final Context context) {
        getInstance();
        phaseViewController.context = context;
        phaseViewController.logmsg = new PhaseModel();
        phaseViewController.logmsg.addObserver(getActivity());
        return phaseViewController;
    }

    /**
     * This is a method that writes the given log in the file and log object
     * @param action message of the log that would be written into file and displayed on the view
     */
    public void addAction(String action) {
        logmsg.setAction(action);
        LogManager.writeLog(action, context);
    }

    /**
     * This is a method that delete the log from the file
     * and the clearPhaseView the log from the UI
     */
    public void clearPhaseView() {
        logmsg.clear();
    }

    /**
     * This method cast the context and returns PlayScreenActivity object.
     * @return The PlayScreenActivity object.
     */
    public PlayScreenActivity getActivity() {
        return (PlayScreenActivity) context;
    }

}
