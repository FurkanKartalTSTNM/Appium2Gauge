package com.testinium.report;

import java.io.Serializable;
import java.util.Date;

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
}
