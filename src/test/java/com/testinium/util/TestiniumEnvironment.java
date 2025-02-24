package com.testinium.util;

import com.testinium.reader.ConfigReader;

public class TestiniumEnvironment {

    public static String sessionId;
    public static String appiumVersion;
    public static String profile;
    public static String takeScreenRecording;
    public static String platform;
    public static String app;
    public static String udid;
    public static String takeScreenshot;
    public static String appPackage;
    public static String appActivity;
    public static String bundleId;
    public static String hubUrl;


    public void init() {
        ConfigReader configReader = new ConfigReader();
        System.out.println("demo:" +configReader.getPropertyValue("furkan"));
        profile = "testinium";
        String envProfile = System.getenv("profile");
        System.out.println("configReader platform:" +configReader.getPropertyValue("platform"));
        System.out.println("platform with env:" +System.getenv("platform"));


        if (envProfile.equals("testinium")) {
            sessionId = configReader.getPropertyValue("sessionId") != null ? configReader.getPropertyValue("sessionId") : "b044c078-d8f4-4a9e-bcbe-ff8ad92fcd87";
            appiumVersion = configReader.getPropertyValue("appiumVersion") != null ? configReader.getPropertyValue("appiumVersion") : "2.5.4";
            takeScreenRecording = configReader.getPropertyValue("takeScreenRecording") != null ? configReader.getPropertyValue("takeScreenRecording") : "true";
            takeScreenshot = configReader.getPropertyValue("takeScreenshot") != null ? configReader.getPropertyValue("takeScreenRecording") : "true";
            app = configReader.getPropertyValue("app") != null ? configReader.getPropertyValue("app") : "null";
            udid = configReader.getPropertyValue("udid") != null ? configReader.getPropertyValue("udid") : "null";
            appPackage = configReader.getPropertyValue("appPackage") != null ? configReader.getPropertyValue("appPackage") : "null";
            appActivity = configReader.getPropertyValue("appActivity") != null ? configReader.getPropertyValue("appActivity") : "null";
            bundleId = configReader.getPropertyValue("bundleId") != null ? configReader.getPropertyValue("bundleId") : "null";

            hubUrl = System.getenv("hubUrl");


            System.out.println("sessionId:" +sessionId );
            System.out.println("appiumVersion:" +appiumVersion );
            System.out.println("sessionId:" +takeScreenRecording );
            System.out.println("sessionId:" +takeScreenshot );
            System.out.println("app:" +app );
            System.out.println("udid:" +udid );
            System.out.println("takeScreenshot:" +appPackage );

        }

        System.out.println("Environment Variables Initialized");
    }


    public static Boolean isPlatformAndroid() {
        platform = System.getenv("platform");
        return "Android".equals(platform);
    }


}
