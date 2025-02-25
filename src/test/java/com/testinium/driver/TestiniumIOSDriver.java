package com.testinium.driver;

import com.testinium.util.Constants;
import com.testinium.util.TestiniumEnvironment;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static com.testinium.util.Constants.DEFAULT_PROFILE;
import static com.testinium.util.Constants.UDID;
import static com.testinium.util.DeviceParkUtil.setDeviceParkOptions;
import static com.testinium.util.MediaUtil.*;
import static com.testinium.driver.TestiniumDriver.registerDriver;


public class TestiniumIOSDriver extends IOSDriver implements CanRecordScreen {


    public TestiniumIOSDriver(URL hubUrl, DesiredCapabilities capabilities) throws Exception {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        registerDriver(this.getSessionId(), this);
        if (recordingAllowed()){
            startScreenRecordingForIOS(this.getRemoteAddress(),this.getSessionId());
        }
    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        if (!DEFAULT_PROFILE.equals(TestiniumEnvironment.profile)) {
            return capabilities;
        }
        DesiredCapabilities overridden = new DesiredCapabilities(capabilities);
        overridden.setCapability(Constants.PLATFORM_NAME, Platform.IOS);
        overridden.setCapability(UDID, TestiniumEnvironment.udid);
        overridden.setCapability("appium:automationName", "XCUITest");
        overridden.setCapability("appium:bundleId", TestiniumEnvironment.bundleId);
        capabilities.setCapability("app", TestiniumEnvironment.app);
        overridden.setCapability("appium:autoAcceptAlerts", true);
        setDeviceParkOptions(overridden);

        return overridden;
    }



    @Override
    public void quit() {
        try {
            stopScreenRecordingForIOS(this.getRemoteAddress(), String.valueOf(this.getSessionId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        TestiniumDriver.postQuit(this);
        super.quit();
    }
}
