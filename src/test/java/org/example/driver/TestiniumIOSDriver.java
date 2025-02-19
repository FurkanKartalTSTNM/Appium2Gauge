package org.example.driver;

import io.appium.java_client.ios.IOSDriver;
import org.example.util.Constants;
import org.example.util.EnvironmentVariables;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.example.util.Constants.DEFAULT_PROFILE;
import static org.example.util.Constants.UDID;
import static org.example.util.DeviceParkUtil.setDeviceParkOptions;
import static org.example.util.MediaUtil.recordingAllowed;

public class TestiniumIOSDriver extends IOSDriver {


    public TestiniumIOSDriver(URL hubUrl, DesiredCapabilities capabilities) {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        TestiniumDriver.registerDriver(this.getSessionId(), this);
        startRecordingScreen();
    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        if (!DEFAULT_PROFILE.equals(EnvironmentVariables.profile)) {
            return capabilities;
        }

        DesiredCapabilities overridden = new DesiredCapabilities(capabilities);
        capabilities.setCapability(Constants.PLATFORM_NAME, Platform.IOS);
        capabilities.setCapability(UDID, "f57820360927d404db9f5147acae9f02a5518fc6");
        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("bundleId", "com.pharos.Gratis");
        capabilities.setCapability("app", "https://testinium-dev-cloud.s3.eu-west-1.amazonaws.com/enterpriseMobileApps/3.2.15_1720_-82c49ca8.ipa");
        capabilities.setCapability("autoAcceptAlerts", true);
        setDeviceParkOptions(capabilities);
        return overridden;
    }

    @Override
    public String startRecordingScreen() {
        if (recordingAllowed()) {
            return super.startRecordingScreen();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("timeLimit", 180);
        Object result = this.executeScript(Constants.Command.START_RECORDING, params);
        return result != null ? result.toString() : "Recording failed";
    }


    @Override
    public String stopRecordingScreen() {
        if (recordingAllowed()) {
            return super.stopRecordingScreen();
        }
        Map<String, Object> params = new HashMap<>();
        Object result = this.executeScript(Constants.Command.STOP_RECORDING, params);
        return result != null ? result.toString() : "Stopping failed";
    }

    @Override
    public void quit() {
        stopRecordingScreen();
        TestiniumDriver.postQuit(this);
        super.quit();
    }
}
