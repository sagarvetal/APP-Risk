package com.app.risk.controller;


import android.content.Context;

import com.app.risk.constants.FileConstants;
import com.app.risk.model.PhaseModel;
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
<<<<<<< HEAD

    /**
     * It is a singleton instance of the controller
     */
    private static PhaseViewController phaseViewController;

    /**
     * It is a directory path to store logs
     */
    private String dirPath;

    /**
     * It is a phase model to contain a list of actions performed by player.
=======
    /**
     * phaseViewController holds the instance of the phase view controller.
     *
     */
    private static PhaseViewController phaseViewController;
    /**
     * dirPath stores the file path where the log file is stored.
     *
     */
    private String dirPath;
    /**
     * logmsg stores the latest log that has to displayed on the phase view.
     *
>>>>>>> 353a44f938ab8e772f2e42666d31336588ae2d7f
     */
    public static PhaseModel logmsg;

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
    public PhaseViewController init(final String dirPath, final PlayScreenActivity view) {
        getInstance();
        phaseViewController.dirPath = dirPath;
        phaseViewController.logmsg = new PhaseModel();
        phaseViewController.logmsg.addObserver(view);
        return phaseViewController;
    }
    /**
     * This is a method that writes the given log in the file and log object
     * @param action message of the log that would be written into file and displayed on the view
     */
    public void addAction(String action) {
        BufferedWriter output = null;
        try {
            File logDirectory = new File(dirPath);
            if (!logDirectory.exists())
                logDirectory.mkdirs();
            File file = new File(logDirectory, FileConstants.LOG_FILE_NAME);
            FileOutputStream f = new FileOutputStream(file, true);
            output = new BufferedWriter(new OutputStreamWriter(f));
            output.write(action);
            output.newLine();

        } catch (Exception e) {
            System.out.println("Error writing log");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {

                }

                logmsg.setAction(action);
            }
        }
    }

    /**
     * This is a method that reads the logs from the file
     * @return List<String> list of the messages that have been logged into the file
     */
    public ArrayList<String> readLog(final Context context) {
        Scanner scanner = null;
        ArrayList<String> logs = new ArrayList<String>();
        File logDirectory = new File(context.getFilesDir() + File.separator + FileConstants.LOG_FILE_PATH);
        if (!logDirectory.exists())
            logDirectory.mkdirs();
        try {
            File file = new File(logDirectory, FileConstants.LOG_FILE_NAME);
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                logs.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(scanner != null){
                scanner.close();
            }
        }
        return logs;
    }

    /**
     * This is a method that delete the log from the file
     * and the clearPhaseView the log from the UI
     */
    public void clearPhaseView() {
        logmsg.clear();
    }


}
