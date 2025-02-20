package com.testinium.driver;

import com.testinium.util.Constants;
import com.testinium.util.TestiniumEnvironment;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static com.testinium.util.Constants.DEFAULT_PROFILE;
import static com.testinium.util.Constants.UDID;
import static com.testinium.util.DeviceParkUtil.setDeviceParkOptions;
import static com.testinium.util.MediaUtil.recordingAllowed;
import static com.testinium.util.MediaUtil.startScreenRecord;


public class TestiniumIOSDriver extends IOSDriver {


    public TestiniumIOSDriver(URL hubUrl, DesiredCapabilities capabilities) {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        com.testinium.driver.TestiniumDriver.registerDriver(this.getSessionId(), this);
        if (recordingAllowed()){
            startScreenRecord(this);
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
        overridden.setCapability(UDID, TestiniumEnvironment.udid);
        overridden.setCapability("automationName", "XCUITest");
        overridden.setCapability("bundleId", "com.apple.Preferences");
        capabilities.setCapability("app", TestiniumEnvironment.app);
        overridden.setCapability("autoAcceptAlerts", true);
        setDeviceParkOptions(overridden);

        System.out.println("deneme"+overridden);

        return overridden;
    }

    @Override
    public void quit() {
        TestiniumDriver.postQuit(this);
        super.quit();
    }
}
