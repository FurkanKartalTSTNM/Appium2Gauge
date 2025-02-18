package org.example;

import org.openqa.selenium.logging.LogEntry;

import java.util.ArrayList;
import java.util.List;

public class TestiniumLogs {

    private String stepName = "";

    private List<LogEntry> logEntries = new ArrayList<>();


    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }
}
