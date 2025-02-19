package org.example;

import com.thoughtworks.gauge.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidBatteryInfo;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSBatteryInfo;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.serverevents.ServerEvents;
import org.example.selector.SelectorFactory;
import org.example.selector.SelectorType;
import org.openqa.grid.internal.TestSession;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringDecorator;
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
import java.util.stream.Collectors;

import org.example.selector.Selector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HookImp {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static AndroidDriver androidDriver;
    protected static IOSDriver iosDriver;
    protected URL hubUrl;
    private List<TestiniumLogs> testiniumLogs = new ArrayList();
    private Integer previousLogs = 0;


    protected static AppiumDriver appiumDriver;
    protected static FluentWait<AppiumDriver> appiumFluentWait;
    public static boolean isDeviceAnd = true;
    protected static Selector selector;

    @BeforeScenario
    public void beforeScenario() {
        String environment = System.getenv("profile");
        logger.info("profile: {}", System.getenv("profile"));

        if (!"testinium".equalsIgnoreCase(environment)) {
            setupRemote();
        } else {
            setupLocal();
        }
        
    }

    private void setupLocal() {
        try {
            hubUrl = new URL("http://127.0.0.1:4723/");
            logger.info("----------BeforeScenario (Local)--------------");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            HashMap<String, Object> deviceParkOptions = new HashMap<>();
            deviceParkOptions.put("sessionId",System.getenv("sessionId"));
            deviceParkOptions.put("appiumVersion",System.getenv("appiumVersion"));
            capabilities.setCapability("dp:options", deviceParkOptions);

            if (isDeviceAnd) {
                capabilities.setCapability("platformName", "ANDROID");
                capabilities.setCapability("udid", "denem");
                capabilities.setCapability("automationName", "deneme");
                capabilities.setCapability("appPackage", "com.gratis.android");
                capabilities.setCapability("appActivity", "com.app.gratis.ui.splash.SplashActivity");
                capabilities.setCapability("autoGrantPermissions", true);
                capabilities.setCapability("appium:newCommandTimeout", 60000);
                capabilities.setCapability("app", "https://gmt-spaces.ams3.cdn.digitaloceanspaces.com/documents/devicepark/Gratis-3.3.0_141.apk");

                LoggingCommandExecutor loggingCommand = new LoggingCommandExecutor(hubUrl);
                androidDriver = new TestiniumAndroidDriver(loggingCommand, capabilities);
                androidDriver = new AndroidDriver(hubUrl,capabilities);
                Capabilities capabilities1 = androidDriver.getCapabilities();

                //WebDriver decoratedDriver = new EventFiringDecorator<>(new Listener(androidDriver)).decorate(androidDriver);

                //androidDriver = new EventFiringDecorator<>(AndroidDriver.class, new Listener()).decorate(driver);
                //webDriver.getBatteryInfo();




                AndroidBatteryInfo info = androidDriver.getBatteryInfo();
                logger.info("Batarya seviyesi: " + info.getLevel());

                /*androidDriver.startRecordingScreen(new AndroidStartScreenRecordingOptions()
                        .withTimeLimit(Duration.ofMinutes(5)));*/

                androidDriver.startRecordingScreen();

                //((AndroidDriver) androidDriver).executeScript("mobile: startMediaProjectionRecording", params);





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

    public static AndroidDriver getDriver(DesiredCapabilities capabilities, URL hubUrl) {
        if (androidDriver == null) {
            try {
                AndroidDriver driver = new AndroidDriver(hubUrl, capabilities);

                androidDriver = new EventFiringDecorator<>(AndroidDriver.class, new Listener()).decorate(driver);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize driver", e);
            }
        }
        return androidDriver;
    }

    private void setupRemote() {
        try {

            logger.info("--------Remote Sessions---------");

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


                Set<String> logTypes = androidDriver.manage().logs().getAvailableLogTypes();
                logger.info("Logtypes: {}",logTypes);

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

    @BeforeStep
    public void beforeStep(ExecutionContext executionContext){
        List<String> stepNames = testiniumLogs.stream().map(TestiniumLogs::getStepName).collect(Collectors.toList());
        String currentStep = executionContext.getCurrentStep().getText();
        String stepStackTrace =executionContext.getCurrentStep().getStackTrace();
        if (stepNames.contains(currentStep)) {
            return;
        }
        TestiniumLogs log = new TestiniumLogs();
        log.setStepName(currentStep);

        previousLogs = androidDriver.manage().logs().get("server").getAll().size();
        testiniumLogs.add(log);
    }


    @AfterStep
    public void takeScreenshotAfterStep(ExecutionContext executionContext) {
        String currentStepName = executionContext.getCurrentStep().getText();
        Optional<TestiniumLogs> stepLogsOptional = testiniumLogs.stream().filter(tl -> tl.getStepName().equals(currentStepName)).findFirst();
        if (stepLogsOptional.isPresent()) {
            LogEntries allAvailableLogs = androidDriver.manage().logs().get("server");
            int logCount = allAvailableLogs.getAll().size();
            int logIndexStart = previousLogs - 1;
            int logIndexEnd = logCount - 1;
            List<LogEntry> previousStepLogs = allAvailableLogs.getAll().subList(logIndexStart, logIndexEnd);
            stepLogsOptional.get().setLogEntries(previousStepLogs);
        }
        logger.info("📸 Step tamamlandı, screenshot alınıyor...");

        File screenshot = null;

        try {
                if (androidDriver instanceof TakesScreenshot) {
                    LogEntries logcat = androidDriver.manage().logs().get("server");

                    screenshot = androidDriver.getScreenshotAs(OutputType.FILE);
                    LogEntries logcat1 = androidDriver.manage().logs().get("server");


                    List<LogEntry> logEntryList = new ArrayList<>();
                    for (LogEntry logEntry : logcat1.getAll()) {
                        if (logcat.getAll().contains(logEntry)) {
                            logEntryList.add(logEntry);
                        }
                    }
                    Logs logs = androidDriver.manage().logs();
                    System.out.println("log");


                } else {
                    logger.warn("⚠️ Android driver screenshot almayı desteklemiyor!");
                }

                if (iosDriver instanceof TakesScreenshot) {
                    screenshot = iosDriver.getScreenshotAs(OutputType.FILE);
                } else {
                    logger.warn("⚠️ iOS driver screenshot almayı desteklemiyor!");
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
}
