package com.testinium.util;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static String DEFAULT_PROFILE = "testinium";
    public static String DEFAULT_VIDEO_ENABLED = "true";
    public static String DEFAULT_SCREENSHOT_ENABLED = "true";
    public static String DEFAULT_SCREENSHOT_ONLY_FAILURE = "true";
    public static String REPORT_FILE_NAME = "command-result";
    public static final String SESSION = "session";
    public static final String PLATFORM_NAME = "platformName";
    public static final String UDID = "appium:udid";
    public static final String VIDEO = "video-record";

    public static final List<String> ignoredCommands = Arrays.asList("screenshot", "start_recording_screen", "stop_recording_screen", "cookie", "window_handle", "window_handles", "window/current/maximize", "url", "title",
            "timeouts/implicit_wait", "timeouts",
            "clear", "displayed", "enabled", "location", "appium/device/pull_file");

    public interface Command {
        String START_RECORDING = "mobile: startMediaProjectionRecording";
        String STOP_RECORDING = "mobile: stopMediaProjectionRecording";
    }

    interface EnvironmentConstants {
        String SESSION_ID = "sessionId";
        String APPIUM_VERSION = "appiumVersion";
        String DP_OPTIONS = "dp:options";
    }
}
