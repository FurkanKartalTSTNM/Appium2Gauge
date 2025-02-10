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
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import org.apache.commons.io.FileUtils;
import org.example.selector.SelectorFactory;
import org.example.selector.SelectorType;
import org.junit.jupiter.api.AfterAll;
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
    protected static FluentWait<AppiumDriver> appiumFluentWait;
    public static boolean isDeviceAnd=false;
    protected static Selector selector ;


    @BeforeScenario
    public void beforeScenario(){
        try {

            //hubUrl = new URL("http://192.168.1.167:4723/");
            hubUrl = new URL("https://dev-devicepark-appium-gw-service.testinium.io/wd/hub");
            logger.info("----------BeforeScenario--------------");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            HashMap<String, Object> deviceParkOptions = new HashMap<>();
            deviceParkOptions.put("sessionId", "764f1110-cca4-4528-97a1-82687a6c71df");
            deviceParkOptions.put("appiumVersion", "2.5.4");
            capabilities.setCapability("dp:options", deviceParkOptions);

            if (isDeviceAnd){
                //capabilities.setCapability("platformName",System.getenv("platform"));
                //capabilities.setCapability("udid", System.getenv("udid"));
                capabilities.setCapability("platformName", "ANDROID");       //Local
                capabilities.setCapability("udid","LGH870d82f54fb");
                capabilities.setCapability("automationName", "UiAutomator2");       //Local
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
                capabilities.setCapability("platformName","iOS");
                capabilities.setCapability("udid","f57820360927d404db9f5147acae9f02a5518fc6");
                capabilities.setCapability("automationName", "XCUITest");
                //capabilities.setCapability("bundleId","com.apple.Preferences");

                capabilities.setCapability("bundleId","com.pharos.Gratis");
                capabilities.setCapability("app", "https://testinium-dev-cloud.s3.eu-west-1.amazonaws.com/enterpriseMobileApps/3.2.15_1720_-82c49ca8.ipa");
                //capabilities.setCapability("app", "/Users/n100922/Downloads/FordTrucksUat__36_-52ad12e7.ipa");
                capabilities.setCapability("autoAcceptAlerts", true);
                iosDriver = new IOSDriver(hubUrl,capabilities);
                IOSBatteryInfo info = iosDriver.getBatteryInfo();
                logger.info(String.valueOf(info.getLevel()));
                iosDriver.startRecordingScreen(new IOSStartScreenRecordingOptions()
                        .withTimeLimit(Duration.ofMinutes(5)));

            }

            selector = SelectorFactory
                    .createElementHelper(isDeviceAnd ? SelectorType.ANDROID: SelectorType.IOS);

            if (isDeviceAnd){
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

    @AfterStep
    public void takeScreenshotAfterStep() {
        logger.info("📸 Step tamamlandı, screenshot alınıyor...");

        File screenshot = null;

        try {
            if (isDeviceAnd) {
                if (androidDriver instanceof TakesScreenshot) {
                    screenshot = androidDriver.getScreenshotAs(OutputType.FILE);
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
    public void AfterScenario() {
        String base64Video = "";

        try {
            if (isDeviceAnd) {
                base64Video = androidDriver.stopRecordingScreen();
            } else {
                base64Video = iosDriver.stopRecordingScreen();
            }

            byte[] videoBytes = Base64.getDecoder().decode(base64Video);
            File videoFile = new File("test-video-" + System.currentTimeMillis() + ".mp4");

            try (FileOutputStream fos = new FileOutputStream(videoFile)) {
                fos.write(videoBytes);
                logger.info("🎥 Video kaydedildi: {}", videoFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("🚨 Video kaydedilirken hata oluştu!", e);
            }

        } catch (Exception e) {
            logger.error("🚨 Ekran kaydı alınırken hata oluştu!", e);
        }

        // Driver'ı kapatma
        try {
            if (isDeviceAnd) {
                androidDriver.quit();
            } else {
                iosDriver.quit();
            }
        } catch (Exception e) {
            logger.error("🚨 Driver kapatma hatası: ", e);
        }
    }

    /*@AfterAll
    public static void takeRecodVideos(){

        String base64Video = iosDriver.stopRecordingScreen();
        byte[] videoBytes = Base64.getDecoder().decode(base64Video);

        File videoFile = new File("test-video-" + System.currentTimeMillis() + ".mp4");
        try (FileOutputStream fos = new FileOutputStream(videoFile)) {
            fos.write(videoBytes);
            System.out.println("🎥 Video kaydedildi:");
            //logger.info("🎥 Video kaydedildi: {}",videoFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/





}
