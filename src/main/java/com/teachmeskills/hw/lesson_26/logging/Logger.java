package com.teachmeskills.hw.lesson_26.logging;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import static com.teachmeskills.hw.lesson_26.constant.Constants.INFO_LOG_PATH;
import static com.teachmeskills.hw.lesson_26.constant.Constants.SIMPLE_DATE_FORMAT;

public class Logger {

    public static void log(String message) {
        String formattedDate = SIMPLE_DATE_FORMAT.format(new Date());
        String logMessage = "[INFO] " + formattedDate + " -> " + message;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INFO_LOG_PATH, true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write to log file (" + INFO_LOG_PATH + "): " + e.getMessage());
        }
    }
}
