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


public class TestiniumIOSDriver extends IOSDriver implements CanRecordScreen {


    public TestiniumIOSDriver(URL hubUrl, DesiredCapabilities capabilities) throws Exception {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        com.testinium.driver.TestiniumDriver.registerDriver(this.getSessionId(), this);
        if (recordingAllowed()){
            startScreenRecordingForIOS(this.getRemoteAddress(),this.getSessionId());
        }
    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        if (!DEFAULT_PROFILE.equals(TestiniumEnvironment.profile)) {
            return capabilities;
        }

        System.out.println("Hub:"+System.getenv("hubURL"));

        System.out.println("UDID:"+System.getenv("udid"));

        DesiredCapabilities overridden = new DesiredCapabilities(capabilities);
        overridden.setCapability(Constants.PLATFORM_NAME, Platform.IOS);
        overridden.setCapability(UDID, "00008020-001A02DC1E69002E");
        overridden.setCapability("appium:automationName", "XCUITest");
        overridden.setCapability("appium:bundleId", "com.apple.Preferences");
        //capabilities.setCapability("app", TestiniumEnvironment.app);
        overridden.setCapability("appium:autoAcceptAlerts", true);
        setDeviceParkOptions(overridden);

        System.out.println("deneme"+overridden);

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
