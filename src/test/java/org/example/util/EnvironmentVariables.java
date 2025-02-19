package org.example.util;


import javax.annotation.PostConstruct;
import java.util.Objects;

public class EnvironmentVariables {

    public static String sessionId;
    public static String appiumVersion;
    public static String profile;
    public static Boolean takeScreenRecording;

    public void init() {
        sessionId = System.getenv("sessionId");
        appiumVersion = System.getenv("appiumVersion");
        takeScreenRecording = true;

        profile = "testinium";
        System.out.println("Environment Variables Initialized");

    }

}
