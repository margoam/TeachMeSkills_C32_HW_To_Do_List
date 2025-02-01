package com.teachmeskills.hw.lesson_26.util;

import com.teachmeskills.hw.lesson_26.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/config.properties"));
            properties.load(inputStream);
        } catch (IOException e) {
            Logger.log("Properties file isn't found: " + e.getMessage());
        }
        return properties;
    }
}
