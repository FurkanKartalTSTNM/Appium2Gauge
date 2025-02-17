package org.example;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.AfterStep;
import com.thoughtworks.gauge.BeforeScenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidBatteryInfo;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSBatteryInfo;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import org.example.selector.SelectorFactory;
import org.example.selector.SelectorType;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.example.selector.Selector;

public class HookImp {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static AndroidDriver androidDriver;
    protected static IOSDriver iosDriver;
    protected URL hubUrl;

    protected static AppiumDriver appiumDriver;
    protected static FluentWait<AppiumDriver> appiumFluentWait;
    public static boolean isDeviceAnd = true;
    protected static Selector selector;

    @BeforeScenario
    public void beforeScenario() {
        String environment = System.getenv("profile");

        if (!"Testinium".equalsIgnoreCase(environment)) {
            setupRemote();
        } else {
            setupLocal();
        }
    }

    private void setupLocal() {
        try {
            hubUrl = new URL("http://192.168.1.89:4723/");
            logger.info("----------BeforeScenario (Local)--------------");
            DesiredCapabilities capabilities = new DesiredCapabilities();

            if (isDeviceAnd) {
                capabilities.setCapability("platformName", "ANDROID");
                capabilities.setCapability("udid", "R68R902ETFR");
                capabilities.setCapability("automationName", "UiAutomator2");
                capabilities.setCapability("appPackage", "com.gratis.android");
                capabilities.setCapability("appActivity", "com.app.gratis.ui.splash.SplashActivity");
                capabilities.setCapability("autoGrantPermissions", true);
                capabilities.setCapability("appium:newCommandTimeout", 60000);
                capabilities.setCapability("app", "https://gmt-spaces.ams3.cdn.digitaloceanspaces.com/documents/devicepark/Gratis-3.3.0_141.apk");

                androidDriver = new AndroidDriver(hubUrl, capabilities);

                AndroidBatteryInfo info = androidDriver.getBatteryInfo();
                logger.info("Batarya seviyesi: " + info.getLevel());

                androidDriver.startRecordingScreen(new AndroidStartScreenRecordingOptions()
                        .withTimeLimit(Duration.ofMinutes(5)));

            } else {
                capabilities.setCapability("platformName", "iOS");
                capabilities.setCapability("udid", "f57820360927d404db9f5147acae9f02a5518fc6");
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("bundleId", "com.pharos.Gratis");
                capabilities.setCapability("app", "https://testinium-dev-cloud.s3.eu-west-1.amazonaws.com/enterpriseMobileApps/3.2.15_1720_-82c49ca8.ipa");
                capabilities.setCapability("autoAcceptAlerts", true);

                iosDriver = new IOSDriver(hubUrl, capabilities);
                IOSBatteryInfo info = iosDriver.getBatteryInfo();
                logger.info("Batarya seviyesi: " + info.getLevel());

                iosDriver.startRecordingScreen(new IOSStartScreenRecordingOptions()
                        .withTimeLimit(Duration.ofMinutes(5)));
            }

            setupFluentWait();

        } catch (Exception e) {
            logger.error("Local ortamda hata oluştu: " + e.getMessage());
        }
    }

    private void setupRemote() {
        try {

            logger.info("hubUrl: {}",System.getenv("hubURL"));
            logger.info("platform: {}",System.getenv("platform"));
            logger.info("udid: {}",System.getenv("udid"));
            logger.info("sessionId: {}",System.getenv("sessionId"));
            logger.info("appiumVersion: {}",System.getenv("appiumVersion"));

            hubUrl = new URL(System.getenv("hubURL"));
            logger.info("----------BeforeScenario--------------");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            HashMap<String, Object> deviceParkOptions = new HashMap<>();
            deviceParkOptions.put("sessionId",System.getenv("sessionId"));
            deviceParkOptions.put("appiumVersion",System.getenv("appiumVersion"));
            capabilities.setCapability("dp:options", deviceParkOptions);

            if (System.getenv("platform").equals("Android")){
                capabilities.setCapability("platformName",System.getenv("platform"));
                capabilities.setCapability("udid", System.getenv("udid"));
                capabilities.setCapability("automationName", "UiAutomator2");
                capabilities.setCapability("appPackage","com.gratis.android");
                capabilities.setCapability("appActivity", "com.app.gratis.ui.splash.SplashActivity");
                capabilities.setCapability("autoGrantPermissions", true);
                capabilities.setCapability("appium:newCommandTimeout", 60000);

                capabilities.setCapability("app", "https://gmt-spaces.ams3.cdn.digitaloceanspaces.com/documents/devicepark/Gratis-3.3.0_141.apk");
                androidDriver = new AndroidDriver(hubUrl, capabilities);

                AndroidBatteryInfo info= androidDriver.getBatteryInfo();
                logger.info(String.valueOf(info.getLevel()));

                androidDriver.startRecordingScreen(new AndroidStartScreenRecordingOptions()
                        .withTimeLimit(Duration.ofMinutes(5)));

            }
            else {
                capabilities.setCapability("platformName",System.getenv("platform"));
                capabilities.setCapability("udid", System.getenv("udid"));
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("bundleId","com.pharos.Gratis");
                capabilities.setCapability("app", "https://testinium-dev-cloud.s3.eu-west-1.amazonaws.com/enterpriseMobileApps/3.2.15_1720_-82c49ca8.ipa");
                capabilities.setCapability("autoAcceptAlerts", true);
                iosDriver = new IOSDriver(hubUrl,capabilities);
                IOSBatteryInfo info = iosDriver.getBatteryInfo();
                logger.info(String.valueOf(info.getLevel()));
                iosDriver.startRecordingScreen(new IOSStartScreenRecordingOptions()
                        .withTimeLimit(Duration.ofMinutes(5)));
            }
            selector = SelectorFactory
                    .createElementHelper(System.getenv("platform").equals("Android") ? SelectorType.ANDROID: SelectorType.IOS);
            if (System.getenv("platform").equals("Android")){
                androidDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                appiumFluentWait = new FluentWait<AppiumDriver>(androidDriver);
            }
            else {
                iosDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                appiumFluentWait = new FluentWait<AppiumDriver>(iosDriver);
            }

            appiumFluentWait.withTimeout(Duration.ofSeconds(8))
                    .pollingEvery(Duration.ofMillis(350))
                    .ignoring(NoSuchElementException.class);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    private void setupFluentWait() {
        selector = SelectorFactory.createElementHelper(isDeviceAnd ? SelectorType.ANDROID : SelectorType.IOS);

        if (isDeviceAnd) {
            androidDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            appiumFluentWait = new FluentWait<>(androidDriver);
        } else {
            iosDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            appiumFluentWait = new FluentWait<>(iosDriver);
        }

        appiumFluentWait.withTimeout(Duration.ofSeconds(8))
                .pollingEvery(Duration.ofMillis(350))
                .ignoring(NoSuchElementException.class);
    }

    @AfterScenario
    public void afterScenario() {
        try {
            if (isDeviceAnd) {
                androidDriver.quit();
            } else {
                iosDriver.quit();
            }
        } catch (Exception e) {
            logger.error("Driver kapatma hatası: ", e);
        }
    }
}
