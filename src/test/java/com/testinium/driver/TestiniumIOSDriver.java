package com.testinium.driver;

import com.testinium.exception.ScreenRecordingException;
import com.testinium.util.Constants;
import com.testinium.util.TestiniumEnvironment;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.screenrecording.CanRecordScreen;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static com.testinium.driver.TestiniumDriver.registerDriver;
import static com.testinium.util.Constants.CapabilityConstants.*;
import static com.testinium.util.Constants.DEFAULT_PROFILE;
import static com.testinium.util.DeviceParkUtil.setDeviceParkOptions;
import static com.testinium.util.MediaUtil.startScreenRecordingForIOS;
import static com.testinium.util.MediaUtil.stopScreenRecordingForIOS;

@Slf4j
public class TestiniumIOSDriver extends IOSDriver implements CanRecordScreen {

    public TestiniumIOSDriver(URL hubUrl, DesiredCapabilities capabilities) throws Exception {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        registerDriver(this.getSessionId(), this);
        startScreenRecordingForIOS(this.getRemoteAddress(),this.getSessionId());

    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        if (!DEFAULT_PROFILE.equals(TestiniumEnvironment.profile)) {
            return capabilities;
        }
        DesiredCapabilities overridden = new DesiredCapabilities(capabilities);
        overridden.setCapability(Constants.PLATFORM_NAME, Platform.IOS);
        overridden.setCapability(UDID, TestiniumEnvironment.udid);
        overridden.setCapability(APPIUM_AUTOMATION_NAME, XCUI_TEST);
        overridden.setCapability(APPIUM_BUNDLE_ID, TestiniumEnvironment.bundleId);
        capabilities.setCapability("app", TestiniumEnvironment.app);
        overridden.setCapability(APPIUM_AUTO_ACCEPT_ALERTS, true);
        setDeviceParkOptions(overridden);

        return overridden;
    }

    @Override
    public void quit() {
        try {
            stopScreenRecordingForIOS(this.getRemoteAddress(), String.valueOf(this.getSessionId()));
        } catch (Exception e) {
            log.error("Error occurred while recording screen for session {}", this.getSessionId(), e);
            throw new ScreenRecordingException(this.getSessionId().toString());
        }

        TestiniumDriver.postQuit(this);
        super.quit();
    }
}
