package com.testinium.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    public String getPropertyValue(String key) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("env/default/default.properties")) {
            properties.load(input);
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
