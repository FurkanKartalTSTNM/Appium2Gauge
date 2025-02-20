package com.testinium.util;

public class TestiniumEnvironment {

    public static String sessionId;
    public static String appiumVersion;
    public static String profile;
    public static String takeScreenRecording;
    public static String platform;
    public static String app;


    public void init() {
        profile = "testinium";
        String envProfile = System.getenv("profile");

        if (envProfile.equals("testinium")) {
            sessionId = System.getenv("sessionId") != null ? System.getenv("sessionId") : "default-session-id";
            appiumVersion = System.getenv("appiumVersion") != null ? System.getenv("appiumVersion") : "2.5.4";
            takeScreenRecording = System.getenv("takeScreenRecording") != null ? System.getenv("takeScreenRecording") : "true";
            app = System.getenv("app") != null ? System.getenv("app") : "null";



        } else {
            sessionId = "a9446477-30da-4f42-833a-82de39967f97";
            appiumVersion = "2.5.4";
            takeScreenRecording = "true";
            profile = "testinium";
        }

        System.out.println("Environment Variables Initialized");
    }


    public static Boolean isPlatformAndroid() {
        platform = System.getenv("platform");
        return "Android".equals(platform);
    }


}
