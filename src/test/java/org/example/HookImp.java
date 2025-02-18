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
    protected static Selector selector ;



    @BeforeScenario
    public void beforeScenario() {
        String environment = System.getenv("PROFILE");
        logger.info("profile: {}", System.getenv("PROFILE"));

        if ("testinium".equalsIgnoreCase(environment)) {
            setupRemote();
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

                if ("true".equals(System.getenv("SCREENRECORD"))){
                    startScreenRecord();
                }


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
    @AfterStep
    public void afterStep(){
        logger.info("takeScreenshot: {}", System.getenv("SCREENSHOT"));
        if ("true".equals(System.getenv("SCREENSHOT"))){
            takeScreenshotAfterStep();
        }
    }

    public void startScreenRecord(){
        logger.info("Starting ScreenRecord");
        androidDriver.startRecordingScreen(new AndroidStartScreenRecordingOptions()
                .withTimeLimit(Duration.ofMinutes(5)));
    }

    public void stopScreenRecord(){

        String base64Video = "";

        try {
            if (System.getenv("platform").equals("Android")) {
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

    }

    public void takeScreenshotAfterStep() {
        logger.info("📸 Step tamamlandı, screenshot alınıyor...");

        File screenshot = null;

        try {
            if (System.getenv("platform").equals("Android")) {
                if (androidDriver instanceof TakesScreenshot) {
                    screenshot = androidDriver.getScreenshotAs(OutputType.FILE);
                } else {
                    logger.warn("⚠️ Android driver screenshot almayı desteklemiyor!");
                }
            } else {
                if (iosDriver instanceof TakesScreenshot) {
                    screenshot = iosDriver.getScreenshotAs(OutputType.FILE);
                } else {
                    logger.warn("⚠️ iOS driver screenshot almayı desteklemiyor!");
                }
            }

            if (screenshot != null) {
                String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
                String filePath = "reports/step-" + timestamp + ".png";

                Files.createDirectories(Paths.get("reports"));
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

        if ("true".equals(System.getenv("SCREENRECORD"))){
            stopScreenRecord();
        }

        try {
            if (System.getenv("platform").equals("Android")) {
                androidDriver.quit();
            } else {
                iosDriver.quit();
            }
        } catch (Exception e) {
            logger.error("🚨 Driver kapatma hatası: ", e);
        }
    }
}
