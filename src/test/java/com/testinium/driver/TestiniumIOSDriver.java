package com.testinium.driver;

import com.testinium.util.Constants;
import com.testinium.util.Environment;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static com.testinium.util.Constants.DEFAULT_PROFILE;
import static com.testinium.util.Constants.UDID;
import static com.testinium.util.DeviceParkUtil.setDeviceParkOptions;
import static com.testinium.util.MediaUtil.startScreenRecord;


public class TestiniumIOSDriver extends IOSDriver {


    public TestiniumIOSDriver(URL hubUrl, DesiredCapabilities capabilities) {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        com.testinium.driver.TestiniumDriver.registerDriver(this.getSessionId(), this);
        startScreenRecord(this);
    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        if (!DEFAULT_PROFILE.equals(Environment.profile)) {
            return capabilities;
        }

        DesiredCapabilities overridden = new DesiredCapabilities(capabilities);
        overridden.setCapability(Constants.PLATFORM_NAME, Platform.IOS);
        overridden.setCapability(UDID, "723DDD46-03E1-488B-860B-7AAF64EC44E1");
        overridden.setCapability("automationName", "XCUITest");
        overridden.setCapability("bundleId", "com.apple.Preferences");
        overridden.setCapability("startIWDP", true);
        //capabilities.setCapability("app", "https://testinium-dev-cloud.s3.eu-west-1.amazonaws.com/enterpriseMobileApps/3.2.15_1720_-82c49ca8.ipa");
        overridden.setCapability("autoAcceptAlerts", true);
        setDeviceParkOptions(overridden);
        return overridden;
    }

    @Override
    public void quit() {
        TestiniumDriver.postQuit(this);
        super.quit();
    }
}
