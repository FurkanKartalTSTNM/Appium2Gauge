package com.testinium.driver;

import com.testinium.util.Constants;
import com.testinium.util.DeviceParkUtil;
import com.testinium.util.TestiniumEnvironment;
import io.appium.java_client.CommandExecutionHelper;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.screenrecording.BaseStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.BaseStopScreenRecordingOptions;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.testinium.util.Constants.PLATFORM_NAME;
import static com.testinium.util.Constants.UDID;
import static com.testinium.util.MediaUtil.*;
import static io.appium.java_client.MobileCommand.startRecordingScreenCommand;
import static io.appium.java_client.MobileCommand.stopRecordingScreenCommand;

public class TestiniumAndroidDriver extends AndroidDriver {

    public TestiniumAndroidDriver(URL hubUrl, DesiredCapabilities capabilities) {
        super(new TestiniumCommandExecutor(hubUrl), overrideCapabilities(capabilities));
        TestiniumDriver.registerDriver(this.getSessionId(), this);
        if (recordingAllowed()){
            System.out.println("recordingAllowed");
            startScreenRecord(this);
        }
    }

    private static DesiredCapabilities overrideCapabilities(DesiredCapabilities capabilities) {
        if (!Constants.DEFAULT_PROFILE.equals(TestiniumEnvironment.profile)) {
            return capabilities;
        }

        DesiredCapabilities overridden = new DesiredCapabilities(capabilities);
        overridden.setCapability(PLATFORM_NAME, Platform.ANDROID);
        overridden.setCapability(UDID, TestiniumEnvironment.udid);
        overridden.setCapability("appium:automationName", "UiAutomator2");
        overridden.setCapability("appium:appPackage", TestiniumEnvironment.appPackage);
        overridden.setCapability("appium:appActivity", TestiniumEnvironment.appActivity);
        overridden.setCapability("appium:autoGrantPermissions", true);
        overridden.setCapability("appium:newCommandTimeout", 60000);
        overridden.setCapability("app",TestiniumEnvironment.app);
        DeviceParkUtil.setDeviceParkOptions(overridden);
        System.out.println("Appium session creation request:" +overridden);
        return overridden;
    }

    @Override
    public String startRecordingScreen() {
        Map<String, Object> params = new HashMap<>();
        params.put("remotePath", "");
        params.put("videoType", "mpeg4");
        params.put("timeLimit", "180");
        params.put("bitRate", "4000000");
        params.put("videoQuality", "high");

        return this.executeScript(Constants.Command.START_RECORDING, params).toString();
    }

    @Override
    public String stopRecordingScreen() {
        Map<String, Object> params = new HashMap<>();
        return this.executeScript(Constants.Command.STOP_RECORDING, params).toString();
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
   }
}
