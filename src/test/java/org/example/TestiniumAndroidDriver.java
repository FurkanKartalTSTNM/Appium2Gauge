package org.example;

import io.appium.java_client.CommandExecutionHelper;
import io.appium.java_client.MobileCommand;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStopScreenRecordingOptions;
import io.appium.java_client.screenrecording.BaseStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.BaseStopScreenRecordingOptions;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;

import java.util.HashMap;
import java.util.Map;

import static io.appium.java_client.MobileCommand.STOP_RECORDING_SCREEN;

public class TestiniumAndroidDriver extends AndroidDriver {

    public TestiniumAndroidDriver(HttpCommandExecutor executor, DesiredCapabilities capabilities) {
        super(executor, overrideCapabilities(capabilities));
    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        DesiredCapabilities overridden = new DesiredCapabilities(capabilities); // Mevcut değerleri kopyalar
        overridden.setCapability("platformName", "ANDROID");
        overridden.setCapability("udid", "R68R902ETFR");
        overridden.setCapability("automationName", "UiAutomator2");
        overridden.setCapability("appPackage", "com.gratis.android");
        overridden.setCapability("appActivity", "com.app.gratis.ui.splash.SplashActivity");
        overridden.setCapability("autoGrantPermissions", true);
        overridden.setCapability("appium:newCommandTimeout", 60000);
        overridden.setCapability("app", "https://gmt-spaces.ams3.cdn.digitaloceanspaces.com/documents/devicepark/Gratis-3.3.0_141.apk");
        return overridden;
    }



    @Override
    public String startRecordingScreen() {
        Map<String, Object> params = new HashMap<>();
        params.put("timeLimit", 180);
        Object result = this.executeScript("mobile: startMediaProjectionRecording", params);
        return result != null ? result.toString() : "Recording failed";
    }

    @Override
    public String stopRecordingScreen() {
        Map<String, Object> params = new HashMap<>();
        Object result = this.executeScript("mobile: stopMediaProjectionRecording", params);
        return result != null ? result.toString() : "Stopping failed";
    }


}
