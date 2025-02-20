package com.testinium.util;

import com.testinium.driver.TestiniumDriver;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.testinium.util.Constants.VIDEO;

public class MediaUtil {

    public static String takeScreenShot(Command command) throws IOException {
        AppiumDriver driver = TestiniumDriver.getDriver(command.getSessionId());
        File screenShotFile = driver.getScreenshotAs(OutputType.FILE);
        return com.testinium.util.FileUtil.saveFile(screenShotFile, command.getName(), "png");
    }

    public static boolean recordingAllowed() {
        return Constants.DEFAULT_PROFILE.equals(TestiniumEnvironment.profile) || Constants.DEFAULT_VIDEO_ENABLED.equals(TestiniumEnvironment.takeScreenRecording);
    }

    public static void startScreenRecord(RemoteWebDriver driver) {
        if (!TestiniumEnvironment.profile.equals("testinium")){
            return;
        }
        Map<String, Object> params = new HashMap<>();
        driver.executeScript(Constants.Command.START_RECORDING, params);
    }

    public static void saveScreenRecord(RemoteWebDriver driver) {
        if (!TestiniumEnvironment.profile.equals("testinium")){
            return;
        }
        Object result = driver.executeScript(Constants.Command.STOP_RECORDING, new HashMap<>());
        try {
            FileUtil.saveVideo((String) result, VIDEO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
