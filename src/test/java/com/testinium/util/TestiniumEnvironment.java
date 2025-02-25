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
        profile = (configReader != null && configReader.getPropertyValue("profile") != null) ?
                configReader.getPropertyValue("profile") : null;


        if (profile.equals("testinium")) {
            sessionId = configReader.getPropertyValue("sessionId") != null ? configReader.getPropertyValue("sessionId") : null;
            appiumVersion = configReader.getPropertyValue("appiumVersion") != null ? configReader.getPropertyValue("appiumVersion") : null;
            takeScreenRecording = configReader.getPropertyValue("takeScreenRecording") != null ? configReader.getPropertyValue("takeScreenRecording") : null;
            takeScreenshot = configReader.getPropertyValue("takeScreenshot") != null ? configReader.getPropertyValue("takeScreenRecording") : null;
            app = configReader.getPropertyValue("app") != null ? configReader.getPropertyValue("app") : null;
            udid = configReader.getPropertyValue("udid") != null ? configReader.getPropertyValue("udid") : null;
            appPackage = configReader.getPropertyValue("appPackage") != null ? configReader.getPropertyValue("appPackage") : null;
            appActivity = configReader.getPropertyValue("appActivity") != null ? configReader.getPropertyValue("appActivity") : null;
            bundleId = configReader.getPropertyValue("bundleId") != null ? configReader.getPropertyValue("bundleId") : null;
            hubUrl = configReader.getPropertyValue("hubUrl") != null ? configReader.getPropertyValue("hubUrl") : null;


            System.out.println("sessionId:" +sessionId );
            System.out.println("appiumVersion:" +appiumVersion );
            System.out.println("takeScreenRecording:" +takeScreenRecording );
            System.out.println("takeScreenshot:" +takeScreenshot );
            System.out.println("app:" +app );
            System.out.println("udid:" +udid );
            System.out.println("appPackage:" +appPackage );
            System.out.println("appActivity:" +appActivity );
            System.out.println("bundleId:" +bundleId );
            System.out.println("hubUrl:" +hubUrl );


        }

        System.out.println("Environment Variables Initialized");
    }


    public static Boolean isPlatformAndroid() {
        ConfigReader configReader = new ConfigReader();
        platform = configReader.getPropertyValue("platform");
        return "Android".equals(platform);
    }


}
