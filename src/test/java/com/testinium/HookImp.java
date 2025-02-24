package com.testinium;

import com.testinium.driver.TestiniumAndroidDriver;
import com.testinium.driver.TestiniumIOSDriver;
import com.testinium.selector.SelectorType;
import com.testinium.util.Constants;
import com.testinium.util.TestiniumEnvironment;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import com.testinium.selector.Selector;
import com.testinium.selector.SelectorFactory;
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

import static com.testinium.util.Constants.UDID;

public class HookImp {

    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static AndroidDriver androidDriver;
    protected static IOSDriver iosDriver;
    protected URL hubUrl;

    protected static FluentWait<AppiumDriver> appiumFluentWait;
    protected static Selector selector ;

    Boolean DeviceAndroid =false;
    @BeforeScenario
    public void beforeScenario() {
        try {
                    if(DeviceAndroid || TestiniumEnvironment.isPlatformAndroid()){
                        DesiredCapabilities overridden = new DesiredCapabilities();
                        hubUrl = new URL(System.getenv("hubURL"));
                        DesiredCapabilities capabilities = new DesiredCapabilities();
                        androidDriver = new TestiniumAndroidDriver(hubUrl,capabilities);
                        selector = SelectorFactory
                                .createElementHelper(SelectorType.ANDROID);

                        androidDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                        appiumFluentWait = new FluentWait<AppiumDriver>(androidDriver);


                        appiumFluentWait.withTimeout(Duration.ofSeconds(8))
                                .pollingEvery(Duration.ofMillis(350))
                                .ignoring(NoSuchElementException.class);
                    }
                    else {
                        hubUrl = new URL(System.getenv("hubURL"));
                        DesiredCapabilities overridden = new DesiredCapabilities();
                        overridden.setCapability(Constants.PLATFORM_NAME, Platform.IOS);
                        overridden.setCapability(UDID, "5ADFD78C-520D-4EB0-BCBC-E7293160659A");
                        overridden.setCapability("appium:automationName", "XCUITest");
                        overridden.setCapability("appium:bundleId", "com.apple.Preferences");
                        overridden.setCapability("appium:autoAcceptAlerts", true);
                        iosDriver = new TestiniumIOSDriver(hubUrl, overridden);


                        selector = SelectorFactory
                                .createElementHelper(SelectorType.IOS);
                        iosDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                        appiumFluentWait = new FluentWait<AppiumDriver>(iosDriver);
                        appiumFluentWait.withTimeout(Duration.ofSeconds(8))
                                .pollingEvery(Duration.ofMillis(350))
                                .ignoring(NoSuchElementException.class);
                    }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterScenario
    public void afterScenario() {
        try {
            if(DeviceAndroid || TestiniumEnvironment.isPlatformAndroid()){
                androidDriver.quit();
            }
                iosDriver.quit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
