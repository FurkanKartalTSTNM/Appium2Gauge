package org.example;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.example.driver.TestiniumAndroidDriver;
import org.example.driver.TestiniumDriver;
import org.example.selector.Selector;
import org.example.selector.SelectorFactory;
import org.example.selector.SelectorType;
import org.example.util.DeviceParkUtil;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.example.util.Constants.PLATFORM_NAME;
import static org.example.util.Constants.UDID;

public class HookImp {

    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static AndroidDriver androidDriver;
    protected static IOSDriver iosDriver;
    protected URL hubUrl;

    protected static FluentWait<AppiumDriver> appiumFluentWait;
    protected static Selector selector ;

    @BeforeScenario
    public void beforeScenario() {
        try {
            TestiniumDriver.initializeEnvironmentVariables();
            hubUrl = new URL("http://127.0.0.1:4723/");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(PLATFORM_NAME, Platform.ANDROID);
            capabilities.setCapability(UDID, "R68R902ETFR");
            capabilities.setCapability("automationName", "UiAutomator2");
            capabilities.setCapability("appPackage", "com.gratis.android");
            capabilities.setCapability("appActivity", "com.app.gratis.ui.splash.SplashActivity");
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("appium:newCommandTimeout", 60000);
            capabilities.setCapability("app", "https://gmt-spaces.ams3.cdn.digitaloceanspaces.com/documents/devicepark/Gratis-3.3.0_141.apk");
            DeviceParkUtil.setDeviceParkOptions(capabilities);
            androidDriver = new TestiniumAndroidDriver(hubUrl, capabilities);
            androidDriver.startRecordingScreen();


            selector = SelectorFactory
                    .createElementHelper(SelectorType.ANDROID);

            androidDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            appiumFluentWait = new FluentWait<AppiumDriver>(androidDriver);


            appiumFluentWait.withTimeout(Duration.ofSeconds(8))
                    .pollingEvery(Duration.ofMillis(350))
                    .ignoring(NoSuchElementException.class);


        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        }
    }

    @AfterScenario
    public void afterScenario() {
        try {
            androidDriver.stopRecordingScreen();
            androidDriver.quit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
