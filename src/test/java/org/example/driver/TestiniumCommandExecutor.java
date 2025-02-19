package org.example.driver;

import io.appium.java_client.remote.AppiumCommandExecutor;
import org.example.report.CommandResultLog;
import org.example.util.CommandUtil;
import org.example.util.Constants;
import org.example.util.MediaUtil;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.ScreenshotException;
import org.openqa.selenium.remote.http.HttpRequest;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;

import static org.example.util.Constants.ignoredCommands;
import static org.example.util.StringUtil.subStringWithMaximumLength;

/**
 * Command Executor of Android and IOS driver
 */
public class TestiniumCommandExecutor extends AppiumCommandExecutor {

    public TestiniumCommandExecutor(URL remoteServer) {
        super(Collections.emptyMap(), remoteServer);
    }

    @Override
    public Response execute(Command command) {
        Date startDate = new Date();
        Response response = super.execute(command);
        buildCommandResultLogs(command, startDate, response);
        return response;
    }

    /**
     * Builds command result logs
     * @param command Command
     * @param startDate start date of execute command
     * @param response response of the command execution
     */
    private void buildCommandResultLogs(Command command, Date startDate, Response response) {

        if (Constants.DEFAULT_PROFILE.equals(System.getProperty(Constants.PROFILE))) {
            return;
        }

        CommandResultLog commandResultLog = new CommandResultLog();
        HttpRequest encodedCommand = super.getCommandCodec().encode(command);
        if (!CommandUtil.isAcceptable(encodedCommand.getUri(), ignoredCommands) ) {
            return;
        }

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
