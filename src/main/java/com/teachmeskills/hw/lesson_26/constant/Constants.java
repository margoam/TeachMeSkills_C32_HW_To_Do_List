package com.teachmeskills.hw.lesson_26.constant;

import com.teachmeskills.hw.lesson_26.util.PropertiesLoader;

import java.text.SimpleDateFormat;

public interface Constants {

    SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String INFO_LOG_PATH = PropertiesLoader.loadProperties().getProperty("logs.path");
}
