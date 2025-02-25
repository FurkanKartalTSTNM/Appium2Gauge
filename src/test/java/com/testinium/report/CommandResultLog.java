package com.testinium.report;

import com.testinium.util.MediaUtil;
import com.testinium.util.TestiniumEnvironment;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.ScreenshotException;
import org.openqa.selenium.remote.http.HttpRequest;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import static com.testinium.util.StringUtil.subStringWithMaximumLength;

public class CommandResultLog implements Serializable {

    private String screenshotName;

    private String method;

    private String requestData;

    private String responseData;

    private String requestPath;

    private Date startDate;

    private Long runtime;

    private Date endDate;

    private String level;

    public void setScreenshotName(String resultFile) {
        this.screenshotName = resultFile;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getScreenshotName() {
        return screenshotName;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestData() {
        return requestData;
    }

    public String getResponseData() {
        return responseData;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Long getRuntime() {
        return runtime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getLevel() {
        return level;
    }

    public static CommandResultLog build(Command command, Date startDate, Response response, HttpRequest encodedCommand) {
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
            if (TestiniumEnvironment.isAllowedToTakeScreenshot()){
                String screenshotName = MediaUtil.takeScreenShot(command);
                commandResultLog.setScreenshotName(screenshotName);
            }

        } catch (IOException e) {
            throw new ScreenshotException(command.getName());
        }
        return commandResultLog;
    }
}
