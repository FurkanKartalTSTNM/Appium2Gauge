package com.testinium.util;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;

import static com.testinium.util.Constants.EnvironmentConstants.*;

public class DeviceParkUtil {

    /**
     * Sets device park options from environment variables
     * @param capabilities
     */
    public static void setDeviceParkOptions(DesiredCapabilities capabilities) {
        HashMap<String, Object> deviceParkOptions = new HashMap<>();
        deviceParkOptions.put(SESSION_ID, com.testinium.util.Environment.sessionId);
        deviceParkOptions.put(APPIUM_VERSION, Environment.sessionId);
        capabilities.setCapability(DP_OPTIONS, deviceParkOptions);
    }
}
