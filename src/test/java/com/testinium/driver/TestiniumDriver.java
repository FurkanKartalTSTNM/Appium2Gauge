package com.testinium.driver;

import com.testinium.report.CommandResultLog;
import com.testinium.util.*;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.ScreenshotException;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.http.HttpRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.testinium.util.Constants.ignoredCommands;
import static com.testinium.util.StringUtil.subStringWithMaximumLength;

public class TestiniumDriver {

    private static final Map<SessionId, AppiumDriver> DRIVER_MAP = new ConcurrentHashMap<>();

    public static final List<CommandResultLog> commandResultLogs = new ArrayList<>();

    public static final Environment ENVIRONMENT = new Environment();

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
        if (!Constants.DEFAULT_PROFILE.equals(Environment.profile)) {
            return;
        }
        FileUtil.saveListOfElementToFile(commandResultLogs, Constants.REPORT_FILE_NAME);
        removeDriver(driver.getSessionId());
    }

    public static void addLog(CommandResultLog log) {
        commandResultLogs.add(log);
    }

    public static void start(){
        ENVIRONMENT.init();
    }

    public static void buildCommandResultLogs(Command command, Date startDate, Response response, HttpRequest encodedCommand) {
        if (!Constants.DEFAULT_PROFILE.equals(Environment.profile) ||
                Boolean.FALSE.equals(CommandUtil.isAcceptable(encodedCommand.getUri(), ignoredCommands))
        ) {
            return;
        }

        CommandResultLog commandResultLog = new CommandResultLog();
        Date endDate = new Date();
        long runtime = endDate.getTime() - startDate.getTime();

        commandResultLog.setStartDate(startDate);
        commandResultLog.setEndDate(endDate);
        commandResultLog.setLevel(response.getState());
        commandResultLog.setRequestData(command.getParameters().toString());
        commandResultLog.setRequestPath(command.getName());
        commandResultLog.setMethod(encodedCommand.getMethod().name());
        commandResultLog.setResponseData(subStringWithMaximumLength(response, 2000));
        commandResultLog.setRuntime(runtime);

        try {
            String screenShotFilePath = MediaUtil.takeScreenShot(command);
            commandResultLog.setScreenShotFilePath(screenShotFilePath);
        } catch (IOException e) {
            throw new ScreenshotException(command.getName());
        }
        TestiniumDriver.addLog(commandResultLog);
    }

}
