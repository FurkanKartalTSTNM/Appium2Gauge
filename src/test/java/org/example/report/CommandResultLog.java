package org.example.report;

import java.io.Serializable;
import java.util.Date;

public class CommandResultLog implements Serializable {

    private String screenShotFilePath;

    private String method;

    private String requestData;

    private String responseData;

    private String requestPath;

    private Date startDate;

    private Long runtime;

    private Date endDate;


    private String level;

    public String getScreenShotFilePath() {
        return screenShotFilePath;
    }

    public void setScreenShotFilePath(String resultFile) {
        this.screenShotFilePath = resultFile;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public Long getRuntime() {
        return runtime;
    }

    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
