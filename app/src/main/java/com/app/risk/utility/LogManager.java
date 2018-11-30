package com.app.risk.utility;

import android.content.Context;

import com.app.risk.constants.FileConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class logs the all activities done during game play.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 28/11/2018)
 */
public class LogManager {

    /**
     * This method logs the given action done during game play.
     * @param action An activity performed during game play.
     * @param context It is an instance of the invoking activity.
     */
    public static void writeLog(final String action, final Context context) {
        BufferedWriter output = null;
        try {
            final File logDirectory = new File(context.getFilesDir() + File.separator + FileConstants.LOG_FILE_PATH);
            if (!logDirectory.exists())
                logDirectory.mkdirs();
            final File file = new File(logDirectory, FileConstants.LOG_FILE_NAME);
            final FileOutputStream f = new FileOutputStream(file, true);
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
            }
        }
    }

    /**
     * This is a method that reads the logs from the file
     * @param context The instance of the invoking activity
     * @return The list of the actions of type string that have been logged into the file
     */
    public static ArrayList<String> readLog(final Context context) {
        Scanner scanner = null;
        final ArrayList<String> logs = new ArrayList<String>();
        final File logDirectory = new File(context.getFilesDir() + File.separator + FileConstants.LOG_FILE_PATH);
        if (!logDirectory.exists())
            logDirectory.mkdirs();
        try {
            final File file = new File(logDirectory, FileConstants.LOG_FILE_NAME);
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
     * This method clears the logs of previous game.
     * @param context The instance of the invoking activity
     */
    public static void clearLog(final Context context) {
        try{
            final String logDir = context.getFilesDir() + File.separator + FileConstants.LOG_FILE_PATH;
            final File logFile = new File(logDir, FileConstants.LOG_FILE_NAME);
            logFile.delete();
        } catch (Exception e) {
            System.out.println("Failed to clear the log");
        }
    }

}
