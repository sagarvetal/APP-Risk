package com.app.risk.utility;

import android.content.Context;
import android.os.Environment;

import com.app.risk.constants.FileConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class logs the all activities done during game play.
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 06/10/2018)
 */
public class ActivityLogger {

    /**
     * This method logs the all activities done during game play.
     * @param activity An activity performed during game play..
     */
    public static void log(final String activity) {
        try {
            final FileWriter fileWriter = new FileWriter(getFilePath(),true);
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(activity);
            bufferedWriter.newLine();
            fileWriter.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method finds the path of log file and return file object.
     * If log folder is not present, it will create the log folder.
     * @return file object
     */
    public static File getFilePath() {
        final String rootPath = Environment.getExternalStorageDirectory().toString();
        final File logDir = new File(rootPath + File.separator + FileConstants.LOG_FILE_PATH);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        return new File(logDir, FileConstants.LOG_FILE_NAME);
    }
}
