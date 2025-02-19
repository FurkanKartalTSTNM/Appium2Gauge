package org.example.driver;

import io.appium.java_client.android.AndroidDriver;
import org.example.util.Constants;
import org.example.util.DeviceParkUtil;
import org.example.util.EnvironmentVariables;
import org.example.util.FileUtil;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.example.util.Constants.*;
import static org.example.util.MediaUtil.recordingAllowed;

public class TestiniumAndroidDriver extends AndroidDriver {


    public TestiniumAndroidDriver(URL hubUrl, DesiredCapabilities capabilities) {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        TestiniumDriver.registerDriver(this.getSessionId(), this);
        startRecordingScreen();
    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        if (!Constants.DEFAULT_PROFILE.equals(EnvironmentVariables.profile)) {
            return capabilities;
        }
        DesiredCapabilities overridden = new DesiredCapabilities(capabilities);
        overridden.setCapability(PLATFORM_NAME, Platform.ANDROID);
        overridden.setCapability(UDID, "R68R902ETFR");
        overridden.setCapability("automationName", "UiAutomator2");
        overridden.setCapability("appPackage", "com.gratis.android");
        overridden.setCapability("appActivity", "com.app.gratis.ui.splash.SplashActivity");
        overridden.setCapability("autoGrantPermissions", true);
        overridden.setCapability("appium:newCommandTimeout", 60000);
        overridden.setCapability("app", "https://gmt-spaces.ams3.cdn.digitaloceanspaces.com/documents/devicepark/Gratis-3.3.0_141.apk");
        DeviceParkUtil.setDeviceParkOptions(capabilities);
        return overridden;
    }

    @Override
    public String startRecordingScreen() {
        Map<String, Object> params = new HashMap<>();
        params.put(TIME_LIMIT, DEFAULT_TIME_SCREEN_RECORD_TIME);
        Object result = this.executeScript(Constants.Command.START_RECORDING, params);
        return result != null ? result.toString() : "Recording failed";
    }

    @Override
    public String stopRecordingScreen() {
        Map<String, Object> params = new HashMap<>();
        Object result = this.executeScript(Constants.Command.STOP_RECORDING, params);
        try {
            return FileUtil.saveVideo((String) result, "video");
        } catch (IOException e) {
            throw new RuntimeException("Error while saving video", e);
        }
    }

   @Override
   public void quit() {
       stopRecordingScreen();
       TestiniumDriver.postQuit(this);
       super.quit();
   }
}
