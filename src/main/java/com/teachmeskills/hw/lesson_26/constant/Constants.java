package com.teachmeskills.hw.lesson_26.constant;

import com.teachmeskills.hw.lesson_26.util.PropertiesLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public interface Constants {

    String PROPERTIES_FILE_PATH = "C:\\Users\\mozhe\\IdeaProjects\\TeachMeSkills_C32_HW_Lesson_26\\src\\main\\resources\\config.properties";
    SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Path INFO_LOG_PATH = Paths.get(PropertiesLoader.loadProperties().getProperty("logs.path"));
}
