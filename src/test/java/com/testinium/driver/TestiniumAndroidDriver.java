package com.testinium.driver;

import com.testinium.util.DeviceParkUtil;
import com.testinium.util.TestiniumEnvironment;
import io.appium.java_client.CommandExecutionHelper;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.screenrecording.BaseStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.BaseStopScreenRecordingOptions;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.testinium.util.Constants.CapabilityConstants.*;
import static com.testinium.util.Constants.Command;
import static com.testinium.util.Constants.PLATFORM_NAME;
import static com.testinium.util.MediaUtil.saveScreenRecord;
import static com.testinium.util.MediaUtil.startScreenRecord;
import static io.appium.java_client.MobileCommand.startRecordingScreenCommand;
import static io.appium.java_client.MobileCommand.stopRecordingScreenCommand;

@Slf4j
public class TestiniumAndroidDriver extends AndroidDriver {

    public TestiniumAndroidDriver(URL hubUrl, DesiredCapabilities capabilities) {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        TestiniumDriver.registerDriver(this.getSessionId(), this);
        startScreenRecord(this);

        log.info("Driver initiated successfully");
    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        if (!TestiniumEnvironment.isProfileTestinium()) {
            return capabilities;
        }

        DesiredCapabilities overridden = new DesiredCapabilities(capabilities);
        overridden.setCapability(PLATFORM_NAME, Platform.ANDROID);
        overridden.setCapability(UDID, TestiniumEnvironment.udid);
        overridden.setCapability(APPIUM_AUTOMATION_NAME, UI_AUTOMATOR_2);
        overridden.setCapability(APPIUM_APP_PACKAGE, TestiniumEnvironment.appPackage);
        overridden.setCapability(APPIUM_APP_ACTIVITY, TestiniumEnvironment.appActivity);
        overridden.setCapability(APPIUM_AUTO_GRANT_PERMISSIONS, true);
        overridden.setCapability(APPIUM_NEW_COMMAND_TIMEOUT, COMMAND_TIMEOUT_DEFAULT_VALUE);
        overridden.setCapability(APP,TestiniumEnvironment.app);
        DeviceParkUtil.setDeviceParkOptions(overridden);
        return overridden;
    }

    @Override
    public String startRecordingScreen() {
        Map<String, Object> params = new HashMap<>();
        return this.executeScript(Command.START_RECORDING, params).toString();
    }

    @Override
    public String stopRecordingScreen() {
        Map<String, Object> params = new HashMap<>();
        return this.executeScript(Command.STOP_RECORDING, params).toString();
    }

    @Override
    public <T extends BaseStartScreenRecordingOptions> String startRecordingScreen(T options) {
        return CommandExecutionHelper.execute(this, startRecordingScreenCommand(options));
    }

    @Override
    public <T extends BaseStopScreenRecordingOptions> String stopRecordingScreen(T options) {
        return CommandExecutionHelper.execute(this, stopRecordingScreenCommand(options));
    }

   @Override
   public void quit() {
       saveScreenRecord(this);
       TestiniumDriver.postQuit(this);
       super.quit();
       log.info("Driver quit successfully");
   }
}
