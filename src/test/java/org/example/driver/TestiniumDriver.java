package org.example.driver;

import io.appium.java_client.AppiumDriver;
import org.example.report.CommandResultLog;
import org.example.util.Constants;
import org.example.util.EnvironmentVariables;
import org.example.util.FileUtil;
import org.openqa.selenium.remote.SessionId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestiniumDriver {

    private static final Map<SessionId, AppiumDriver> DRIVER_MAP = new ConcurrentHashMap<>();

    public static final List<CommandResultLog> commandResultLogs = new ArrayList<>();

    public static final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    public static void registerDriver(SessionId sessionId, AppiumDriver driver) {
        DRIVER_MAP.put(sessionId, driver);
    }

    public static AppiumDriver getDriver(SessionId sessionId) {
        return DRIVER_MAP.get(sessionId);
    }
    public static void removeDriver(SessionId sessionId) {
        DRIVER_MAP.remove(sessionId);
    }

    public static void postQuit(AppiumDriver driver) {
        FileUtil.saveListOfElementToFile(commandResultLogs, Constants.REPORT_FILE_NAME);
        removeDriver(driver.getSessionId());
    }

    public static void addLog(CommandResultLog log) {
        commandResultLogs.add(log);
    }

    public static void initializeEnvironmentVariables(){
        environmentVariables.init();
    }

}
