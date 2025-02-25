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
        System.out.println("profffile:" + configReader.getPropertyValue("profile"));
        profile = (configReader != null && configReader.getPropertyValue("profile") != null) ?
                configReader.getPropertyValue("profile") : null;


        if (profile.equals("testinium")) {
            sessionId = configReader.getPropertyValue("sessionId") != null ? configReader.getPropertyValue("sessionId") : "b044c078-d8f4-4a9e-bcbe-ff8ad92fcd87";
            appiumVersion = configReader.getPropertyValue("appiumVersion") != null ? configReader.getPropertyValue("appiumVersion") : "2.5.4";
            takeScreenRecording = configReader.getPropertyValue("takeScreenRecording") != null ? configReader.getPropertyValue("takeScreenRecording") : "true";
            takeScreenshot = configReader.getPropertyValue("takeScreenshot") != null ? configReader.getPropertyValue("takeScreenRecording") : "true";
            app = configReader.getPropertyValue("app") != null ? configReader.getPropertyValue("app") : null;
            udid = configReader.getPropertyValue("udid") != null ? configReader.getPropertyValue("udid") : "D55BA86F-809A-4713-92F0-AC1E573FE77F";
            appPackage = configReader.getPropertyValue("appPackage") != null ? configReader.getPropertyValue("appPackage") : "com.gratis.android";
            appActivity = configReader.getPropertyValue("appActivity") != null ? configReader.getPropertyValue("appActivity") : "com.app.gratis.ui.splash.SplashActivity";
            bundleId = configReader.getPropertyValue("bundleId") != null ? configReader.getPropertyValue("bundleId") : "com.pharos.Gratis";
            hubUrl = configReader.getPropertyValue("hubUrl") != null ? configReader.getPropertyValue("hubUrl") : "https://dev-devicepark-appium-gw-service.testinium.io/wd/hub";


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
        System.out.println("gettingPlatform"+ configReader.getPropertyValue("platform"));
        platform = configReader.getPropertyValue("platform");
        System.out.println("return:" +"Android".equals(platform));
        return "Android".equals(platform);
    }


}
