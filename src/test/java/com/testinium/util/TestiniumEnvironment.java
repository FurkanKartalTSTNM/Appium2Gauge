package com.testinium.util;

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
        profile = "testinium";
        String envProfile = System.getenv("profile");
        System.out.println("PH_FOR_TARGET_TEST" +System.getenv("phForTargetTest"));

        if (envProfile.equals("testinium")) {
            sessionId = System.getenv("sessionId") != null ? System.getenv("sessionId") : "b044c078-d8f4-4a9e-bcbe-ff8ad92fcd87";
            appiumVersion = System.getenv("appiumVersion") != null ? System.getenv("appiumVersion") : "2.5.4";
            takeScreenRecording = System.getenv("takeScreenRecording") != null ? System.getenv("takeScreenRecording") : "true";
            takeScreenshot = System.getenv("takeScreenshot") != null ? System.getenv("takeScreenRecording") : "true";
            app = System.getenv("app") != null ? System.getenv("app") : "null";
            udid = System.getenv("udid") != null ? System.getenv("udid") : "null";
            appPackage = System.getenv("appPackage") != null ? System.getenv("appPackage") : "null";
            appActivity = System.getenv("appActivity") != null ? System.getenv("appActivity") : "null";
            bundleId = System.getenv("bundleId") != null ? System.getenv("bundleId") : "null";

            hubUrl = System.getenv("hubUrl");


            System.out.println("sessionId:" +System.getenv("sessionId") );
            System.out.println("appiumVersion:" +System.getenv("appiumVersion") );
            System.out.println("sessionId:" +System.getenv("sessionId") );
            System.out.println("sessionId:" +System.getenv("sessionId") );
            System.out.println("app:" +System.getenv("app") );
            System.out.println("udid:" +System.getenv("udid") );
            System.out.println("takeScreenshot:" +System.getenv("takeScreenshot") );

        }

        System.out.println("Environment Variables Initialized");
    }


    public static Boolean isPlatformAndroid() {
        platform = System.getenv("platform");
        return "Android".equals(platform);
    }


}
