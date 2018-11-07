package com.app.risk.utility;

import android.content.Context;

import com.app.risk.constants.FileConstants;
//import com.app.risk.controller.PhaseController;
import com.app.risk.view.PlayScreenActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LogManager {
    private static LogManager logManager;
    private String dirPath;
    private LogManager() {
    }
    public static LogManager getInstance() {
        if (logManager == null) {
            logManager = new LogManager();
        }
        return logManager;
    }
    public static LogManager getInstance(String dirPath) {
        if (logManager == null) {
            logManager = new LogManager();
            logManager.dirPath=dirPath;
       /*     File logDirectory = new File(dirPath);
            if (!logDirectory.exists())
                logDirectory.mkdirs();
            File file = new File(logDirectory, FileConstants.LOG_FILE_NAME);*/
        }
        return logManager;
    }
    public void writeLog(String text) {
        BufferedWriter output = null;
        try {

            File logDirectory = new File(dirPath);
            if (!logDirectory.exists())
                logDirectory.mkdirs();
            File file = new File(logDirectory, FileConstants.LOG_FILE_NAME);
            boolean result=false;
            try {
               result=file.createNewFile();
                System.out.println("::::::::::::::::::::::::::::::::write LOg result::::::::::::::::::"+result);
            } catch (IOException e) {
                e.printStackTrace();
            }

                FileOutputStream f = new FileOutputStream(file,true);
           // FileOutputStream f = new FileOutputStream(file);
            output = new BufferedWriter(new OutputStreamWriter(f));
                output.write(text);
                output.newLine();

        } catch (IOException e) {

        }
        catch(Exception e)
        {

        }
        finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {

                }
                PlayScreenActivity.logViewArrayList.add(text);
                PlayScreenActivity.logViewAdapter.notifyDataSetChanged();
               // PhaseController.getInstance().update();
            }
        }
    }


    public List<String> readLog() {
        Scanner scanner = null;
        ArrayList<String> logs = new ArrayList<String>();
        File logDirectory = new File(LogManager.getInstance().dirPath);
        if (!logDirectory.exists())
            logDirectory.mkdirs();
        File file = new File(logDirectory, FileConstants.LOG_FILE_NAME);
        try {
            boolean result=file.createNewFile();
            System.out.println("::::::::::::::::::::::::::::::::read LOG  result::::::::::::::::::"+result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {
            logs.add(scanner.nextLine());
        }
        scanner.close();
        return logs;
    }


    public void deleteLog() {
        File logDirectory = new File(LogManager.getInstance().dirPath);
        if (!logDirectory.exists())
            logDirectory.mkdirs();
        File file = new File(logDirectory, FileConstants.LOG_FILE_NAME);
        if (file.exists()) {
            boolean result=file.delete();
            System.out.println(":::::::::::::::::::::::::delete result::::::::::::::"+result);
            PlayScreenActivity.logViewArrayList.removeAll(PlayScreenActivity.logViewArrayList);
            PlayScreenActivity.logViewAdapter.notifyDataSetChanged();
        }
       // PhaseController.getInstance().deleteUpdate();
    }
}
