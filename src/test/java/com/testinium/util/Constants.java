package com.testinium.util;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String DEFAULT_PROFILE = "testinium";
    public static final String DEFAULT_VIDEO_ENABLED = "true";
    public static final String DEFAULT_SCREENSHOT_ENABLED = "true";
    public static final String DEFAULT_SCREENSHOT_ONLY_FAILURE = "only_failure";
    public static final String REPORT_FILE_NAME = "command-result";
    public static final String SESSION = "session";
    public static final String PLATFORM_NAME = "platformName";
    public static final String VIDEO = "video-record";
    public static final String DEFAULT_SCREENSHOT_MEDIA_TYPE = "png";
    public static final String ENV_DEFAULT_PROPERTIES_PATH = "env/default/default.properties";

    public static final List<String> ignoredCommands = Arrays.asList("screenshot", "start_recording_screen", "stop_recording_screen", "cookie", "window_handle", "window_handles", "window/current/maximize", "url", "title",
            "timeouts/implicit_wait", "timeouts",
            "clear", "displayed", "enabled", "location", "appium/device/pull_file");

    public interface Command {
        String START_RECORDING = "mobile: startMediaProjectionRecording";
        String STOP_RECORDING = "mobile: stopMediaProjectionRecording";
    }

    public interface EnvironmentConstants {
        String SESSION_ID = "sessionId";
        String APPIUM_VERSION = "appiumVersion";
        String DP_OPTIONS = "dp:options";
    }

   public interface CapabilityConstants {
        String UDID = "appium:udid";
        String TAKE_SCREEN_RECORDING = "takeScreenRecording";
        String APPIUM_AUTOMATION_NAME = "appium:automationName";
        String APPIUM_APP_PACKAGE = "appium:appPackage";
        String APPIUM_APP_ACTIVITY = "appium:appActivity";
        String APPIUM_AUTO_GRANT_PERMISSIONS = "appium:autoGrantPermissions";
        String APPIUM_NEW_COMMAND_TIMEOUT = "appium:newCommandTimeout";
        String APP = "app";
        String UI_AUTOMATOR_2 = "UiAutomator2";
        int COMMAND_TIMEOUT_DEFAULT_VALUE = 60000;
        String APPIUM_BUNDLE_ID = "appium:bundleId";
        String XCUI_TEST = "XCUITest";
        String APPIUM_AUTO_ACCEPT_ALERTS = "appium:autoAcceptAlerts";
    }
}
