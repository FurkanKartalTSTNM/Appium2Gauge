package org.example.util;

import io.appium.java_client.AppiumDriver;
import org.apache.pdfbox.util.filetypedetector.FileType;
import org.example.driver.TestiniumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.Command;

import java.io.File;
import java.io.IOException;

public class MediaUtil {

    public static String takeScreenShot(Command command) throws IOException {
        AppiumDriver driver = TestiniumDriver.getDriver(command.getSessionId());
        File screenShotFile = driver.getScreenshotAs(OutputType.FILE);
        return FileUtil.saveFile(screenShotFile, command.getName(), FileType.PNG);
    }

    public static boolean recordingAllowed() {
        return !Constants.DEFAULT_PROFILE.equals(EnvironmentVariables.profile) || !EnvironmentVariables.takeScreenRecording;
    }
}
