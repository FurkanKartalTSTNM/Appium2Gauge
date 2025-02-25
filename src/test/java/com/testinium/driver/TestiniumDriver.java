package com.testinium.driver;

import com.testinium.report.CommandResultLog;
import com.testinium.util.CommandUtil;
import com.testinium.util.Constants;
import com.testinium.util.FileUtil;
import com.testinium.util.TestiniumEnvironment;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.http.HttpRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.testinium.report.CommandResultLog.build;
import static com.testinium.util.Constants.ignoredCommands;

public class TestiniumDriver {

    private static final Map<SessionId, AppiumDriver> DRIVER_MAP = new ConcurrentHashMap<>();

    public static final List<CommandResultLog> commandResultLogs = new ArrayList<>();

    public static final TestiniumEnvironment TESTINIUM_ENVIRONMENT = new TestiniumEnvironment();

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
        if (!TestiniumEnvironment.isProfileTestinium()) {
            return;
        }

        FileUtil.saveListOfElementToFile(commandResultLogs, Constants.REPORT_FILE_NAME);
        removeDriver(driver.getSessionId());
    }

    public static void addLog(CommandResultLog log) {
        commandResultLogs.add(log);
    }

    public static void buildCommandResultLogs(Command command, Date startDate, Response response, HttpRequest encodedCommand) {
        if (!TestiniumEnvironment.isProfileTestinium() ||
                Boolean.FALSE.equals(CommandUtil.isAcceptable(encodedCommand.getUri(), ignoredCommands))
        ) {
            return;
        }
        CommandResultLog commandResultLog = build(command, startDate, response, encodedCommand);
        TestiniumDriver.addLog(commandResultLog);
    }

}
