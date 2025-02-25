package com.testinium.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.testinium.util.Constants.ENV_DEFAULT_PROPERTIES_PATH;

public class ConfigReader {

    public String getPropertyValue(String key) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(ENV_DEFAULT_PROPERTIES_PATH)) {
            properties.load(input);
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
