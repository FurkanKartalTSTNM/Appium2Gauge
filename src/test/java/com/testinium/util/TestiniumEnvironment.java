package com.testinium.util;

import com.testinium.reader.ConfigReader;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Platform;

import java.util.Objects;

import static com.testinium.util.Constants.CapabilityConstants.TAKE_SCREEN_RECORDING;

@Slf4j
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

    public static void init() {
        ConfigReader configReader = new ConfigReader();
        profile = Objects.nonNull(configReader.getPropertyValue("profile")) ?
                configReader.getPropertyValue("profile") : null;

        if (!Constants.DEFAULT_PROFILE.equals(profile)) {
            return;
        }

        sessionId = configReader.getPropertyValue("sessionId");
        appiumVersion = configReader.getPropertyValue("appiumVersion");
        takeScreenRecording = configReader.getPropertyValue(TAKE_SCREEN_RECORDING);
        takeScreenshot = configReader.getPropertyValue("takeScreenshot");
        app = configReader.getPropertyValue("app");
        udid = configReader.getPropertyValue("udid");
        appPackage = configReader.getPropertyValue("appPackage");
        appActivity = configReader.getPropertyValue("appActivity");
        bundleId = configReader.getPropertyValue("bundleId");
        hubUrl = configReader.getPropertyValue("hubURL");

        System.out.println("sessionId: " + sessionId);
        System.out.println("appiumVersion: " + appiumVersion);
        System.out.println("takeScreenRecording: " + takeScreenRecording);
        System.out.println("takeScreenshot: " + takeScreenshot);
        System.out.println("app: " + app);
        System.out.println("udid: " + udid);
        System.out.println("appPackage: " + appPackage);
        System.out.println("appActivity: " + appActivity);
        System.out.println("bundleId: " + bundleId);
        System.out.println("hubUrl: " + hubUrl);
        System.out.println("profile: " + profile);
        System.out.println("Environment Variables Initialized");

    }

    public static Boolean isProfileTestinium(){
        return Constants.DEFAULT_PROFILE.equals(profile);
    }

    public static Boolean isAndroid(){
        return Platform.ANDROID.name().equals(platform);
    }

    public static Boolean isPlatformAndroid() {
        ConfigReader configReader = new ConfigReader();
        log.info("gettingPlatform: {}", configReader.getPropertyValue("platform"));
        platform = configReader.getPropertyValue("platform");
        return Platform.ANDROID.name().equalsIgnoreCase(platform);
    }

    public static Boolean isAllowedToTakeScreenshot() {
        return Constants.DEFAULT_SCREENSHOT_ENABLED.equalsIgnoreCase(takeScreenshot) || Constants.DEFAULT_SCREENSHOT_ONLY_FAILURE.equalsIgnoreCase(takeScreenshot);
    }
}
