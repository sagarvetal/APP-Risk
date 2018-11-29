package com.app.risk.controller;


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
    private static PhaseViewController phaseViewController;
    private String dirPath;
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
    public List<String> readLog() {
        Scanner scanner = null;
        ArrayList<String> logs = new ArrayList<String>();
        File logDirectory = new File(PhaseViewController.getInstance().dirPath);
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
