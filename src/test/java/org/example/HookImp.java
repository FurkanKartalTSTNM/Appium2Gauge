package org.example;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.AfterStep;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.Gauge;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidBatteryInfo;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSBatteryInfo;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.example.selector.SelectorFactory;
import org.example.selector.SelectorType;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
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
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.example.selector.Selector;
public class HookImp {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static AndroidDriver androidDriver;
    protected static IOSDriver iosDriver;
    URL hubUrl;

    protected static AppiumDriver appiumDriver;
    static EventFiringWebDriver eventDriver;
    protected static FluentWait<AppiumDriver> appiumFluentWait;
    public static boolean isDeviceAnd=false;
    protected static Selector selector ;


    @BeforeScenario
    public void beforeScenario(){
        try {
            logger.info("hubUrl: ",System.getenv("hubURL"));
            logger.info("platform: ",System.getenv("platform"));
            logger.info("udid: ",System.getenv("udid"));
            logger.info("sessionid: ",System.getenv("sessionid"));
            logger.info("appiumVersion: ",System.getenv("appiumVersion"));

            hubUrl = new URL("https://dev-devicepark-appium-gw-service.testinium.io/wd/hub");
            //hubUrl = new URL("http://192.168.1.167:4723");
            logger.info("----------BeforeScenario--------------");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            HashMap<String, Object> deviceParkOptions = new HashMap<>();
            deviceParkOptions.put("sessionId", System.getenv("sessionid"));
            //deviceParkOptions.put("sessionId", "ec12edc2-704f-4185-9348-0b4287a3519c");
            deviceParkOptions.put("appiumVersion", System.getenv("appiumVersion"));
            capabilities.setCapability("dp:options", deviceParkOptions);

            if (isDeviceAnd){
                capabilities.setCapability("platformName",System.getenv("platform"));
                capabilities.setCapability("udid", System.getenv("udid"));
                //capabilities.setCapability("platformName", "ANDROID");       //Local
                //capabilities.setCapability("udid","LGH870d82f54fb");
                capabilities.setCapability("automationName", "UiAutomator2");       //Local
                capabilities.setCapability("appPackage","com.gratis.android");
                capabilities.setCapability("appActivity", "com.app.gratis.ui.splash.SplashActivity");
                capabilities.setCapability("autoGrantPermissions", true);
                capabilities.setCapability("appium:newCommandTimeout", 60000);

                capabilities.setCapability("app", "https://gmt-spaces.ams3.cdn.digitaloceanspaces.com/documents/devicepark/Gratis-3.3.0_141.apk");
                androidDriver = new AndroidDriver(hubUrl, capabilities);

                AndroidBatteryInfo info= androidDriver.getBatteryInfo();
                logger.info(String.valueOf(info.getLevel()));

                /*androidDriver.startRecordingScreen(new AndroidStartScreenRecordingOptions()
                        .withTimeLimit(Duration.ofMinutes(5))*/

            }
            else {
                capabilities.setCapability("platformName",System.getenv("platform"));
                capabilities.setCapability("udid", System.getenv("udid"));
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("bundleId","com.apple.Preferences");
                //capabilities.setCapability("app", "https://gmt-spaces.ams3.cdn.digitaloceanspaces.com/documents/devicepark/Gratis-68c16a02.ipa");
                //capabilities.setCapability("app", "/Users/n100922/Downloads/FordTrucksUat__36_-52ad12e7.ipa");

                iosDriver = new IOSDriver(hubUrl,capabilities);
                IOSBatteryInfo info = iosDriver.getBatteryInfo();
                logger.info(String.valueOf(info.getLevel()));

            }

            selector = SelectorFactory
                    .createElementHelper(isDeviceAnd ? SelectorType.ANDROID: SelectorType.IOS);
            iosDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            if (isDeviceAnd){
                appiumFluentWait = new FluentWait<AppiumDriver>(androidDriver);
            }
            else {
                appiumFluentWait = new FluentWait<AppiumDriver>(iosDriver);

            }
            appiumFluentWait.withTimeout(Duration.ofSeconds(8))
                    .pollingEvery(Duration.ofMillis(350))
                    .ignoring(NoSuchElementException.class);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    @AfterStep
    public void takeScreenshotAfterStep() {
        logger.info("📸 Step tamamlandı, screenshot alınıyor...");

        File screenshot = null;

        try {
            if (isDeviceAnd) {
                if (androidDriver instanceof TakesScreenshot) {
                    File srcFile = androidDriver.getScreenshotAs(OutputType.FILE);
                } else {
                    logger.warn("⚠️ Android driver screenshot almayı desteklemiyor!");
                }
            } else {
                if (iosDriver instanceof TakesScreenshot) {
                    screenshot = ((TakesScreenshot) iosDriver).getScreenshotAs(OutputType.FILE);
                } else {
                    logger.warn("⚠️ iOS driver screenshot almayı desteklemiyor!");
                }
            }

            if (screenshot != null) {
                String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
                String filePath = "screenshots/step-" + timestamp + ".png";

                Files.createDirectories(Paths.get("screenshots"));
                Files.copy(screenshot.toPath(), Paths.get(filePath));

                logger.info("✅ Screenshot kaydedildi: {}", filePath);
            } else {
                logger.warn("⚠️ Screenshot alınamadı, screenshot değişkeni null!");
            }
        } catch (IOException e) {
            logger.error("🚨 Screenshot kaydedilirken IO hatası oluştu!", e);
        } catch (Exception e) {
            logger.error("🚨 Screenshot alınırken beklenmedik bir hata oluştu!", e);
        }
    }





    @AfterScenario
    public  void AfterScenario(){
        /*String base64Video = androidDriver.stopRecordingScreen();
        byte[] videoBytes = Base64.getDecoder().decode(base64Video);

        File videoFile = new File("test-video-" + System.currentTimeMillis() + ".mp4");
        try (FileOutputStream fos = new FileOutputStream(videoFile)) {
            fos.write(videoBytes);
            logger.info("🎥 Video kaydedildi: {}",videoFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            if (isDeviceAnd){
                androidDriver.quit();
            }
            else {
                iosDriver.quit();
            }

        } catch (Exception e){
            logger.error(e.getMessage());
        }

    }




}
