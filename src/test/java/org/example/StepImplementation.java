package org.example;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.assertj.core.api.Assertions;
import org.example.model.SelectorInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;

public class StepImplementation extends HookImp {

    private HashSet<Character> vowels;
    private Logger logger = LoggerFactory.getLogger(getClass());


    public boolean doesElementExistByKeyAndroid(String key, int time) {
        if (selector == null) {
            throw new IllegalStateException("Selector nesnesi null. Başlatıldığından emin olun.");
        }

        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        if (selectorInfo == null) {
            logger.error(key + " için SelectorInfo nesnesi null döndü.");
            return false;
        }

        try {
            WebDriverWait elementExist = new WebDriverWait(androidDriver, Duration.ofSeconds(time));
            elementExist.until(ExpectedConditions.visibilityOfElementLocated(selectorInfo.getBy()));
            return true;
        } catch (Exception e) {
            logger.info(key + " aranan elementi bulamadı");
            return false;
        }
    }

    public boolean doesElementExistByKeyIOS(String key, int time) {
        if (selector == null) {
            throw new IllegalStateException("Selector nesnesi null. Başlatıldığından emin olun.");
        }

        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        if (selectorInfo == null) {
            logger.error(key + " için SelectorInfo nesnesi null döndü.");
            return false;
        }

        try {
            WebDriverWait elementExist = new WebDriverWait(iosDriver, Duration.ofSeconds(time));
            elementExist.until(ExpectedConditions.visibilityOfElementLocated(selectorInfo.getBy()));
            return true;
        } catch (Exception e) {
            logger.info(key + " aranan elementi bulamadı");
            return false;
        }
    }


    public List findElements(By by) throws Exception {
        List webElementList = null;
        try {
            webElementList = appiumFluentWait.until(new ExpectedCondition<List>() {
                @Nullable
                @Override
                public List apply(@Nullable WebDriver driver) {
                    List elements = driver.findElements(by);
                    return elements.size() > 0 ? elements : null;
                }
            });

            if (webElementList == null) {
                throw new NullPointerException(String.format("by = %s Web element list not found", by.toString()));
            }

        } catch (Exception e) {
            throw e;
        }
        return webElementList;
    }
    public WebElement findElement(By by) throws Exception {
        WebElement WebElement;
        try {
            WebElement = (org.openqa.selenium.WebElement) findElements(by).get(0);
        } catch (Exception e) {
            throw e;
        }
        return WebElement;
    }

    public WebElement findElementByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        WebElement WebElement = null;
        try {
            WebElement = selectorInfo.getIndex() > 0 ? (org.openqa.selenium.WebElement) findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Element not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();
        }
        return WebElement;
    }


    @Step({"<seconds> saniye bekle", "Wait <second> seconds"})
    public void waitBySecond(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
            logger.info("{} saniye beklendi",seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("The word <word> has <expectedCount> vowels.")
    public void verifyVowelsCountInWord(String word, int expectedCount) {
        int actualCount = countVowels(word);
        assertThat(expectedCount).isEqualTo(actualCount);
    }

    @Step("Almost all words have vowels <wordsTable>")
    public void verifyVowelsCountInMultipleWords(Table wordsTable) {
        for (TableRow row : wordsTable.getTableRows()) {
            String word = row.getCell("Word");
            int expectedCount = Integer.parseInt(row.getCell("Vowel Count"));
            int actualCount = countVowels(word);

            assertThat(expectedCount).isEqualTo(actualCount);
        }
    }

    @Step({"Elementine tıkla Android <key>", "Click element by Android <key>"})
    public void clickByKeyAndroid(String key) {
        doesElementExistByKeyAndroid(key, 5);
        WebElement element = findElementByKey(key);
        logger.info("Sending click request for key: " + key);
        element.click();
        logger.info(key + " elemente tıkladı");
    }

    @Step({"Elementine tıkla IOS <key>", "Click element by IOS <key>"})
    public void clickByKeyIOS(String key) {
        doesElementExistByKeyIOS(key, 5);
        findElementByKey(key).click();
        logger.info(key + " elemente tıkladı");
    }

    @Step({"Check if element <key> exists",
            "Wait for element to load with key <key>",
            "Element var mı kontrol et <key>",
            "Elementin yüklenmesini bekle <key>"})
    public WebElement getElementWithKeyIfExists(String key) throws InterruptedException {
        WebElement element;
        try {
            element = findElementByKey(key);
            logger.info(key + " elementi bulundu.");
            System.out.println(key+"Element bulundu");
        } catch (Exception ex) {
            logger.info("Element: '" + key + "' doesn't exist.");
            return null;
        }
        return element;
    }

    @Step("<key> elementinin hizasından sağdan sola <times> kere kaydır")
    public void swipeFromLeftToRightAligned(String key, int times) throws InterruptedException {
        Dimension d = appiumDriver.manage().window().getSize();

        int height = d.height;
        int width = d.width;
        Point elementLocation = findElementByKeyWithoutAssert(key).getLocation();
        pointToPointSwipeWithCoordinats(width - 65, elementLocation.getY(), 40, elementLocation.getY(), times);
    }

    @Step("<key> elementinin hizasından soldan sağa <times> kere kaydır")
    public void swipeFromRightToLeftAligned(String key, int times) throws InterruptedException {
        Dimension d = appiumDriver.manage().window().getSize();

        int height = d.height;
        int width = d.width;
        Point elementLocation = findElementByKeyWithoutAssert(key).getLocation();
        pointToPointSwipeWithCoordinats(40, elementLocation.getY(),  width - 50, elementLocation.getY(), times);
    }

    @Step({"<key> li ve değeri <text> e eşit olan elementli bulana kadar swipe et ve tıkla",
            "Find element by <key> text equals <text> swipe and click"})
    public void clickByKeyWithSwipe(String key, String text) throws InterruptedException {
        boolean find = false;
        int maxRetryCount = 10;
        while (!find && maxRetryCount > 0) {
            List<WebElement> elements = findElemenstByKey(key);
            for (WebElement element : elements) {
                if (element.getText().contains(text)) {
                    element.click();
                    find = true;
                    break;
                }
            }
            if (!find) {
                maxRetryCount--;
                if (appiumDriver instanceof AndroidDriver) {
                    swipeUpAccordingToPhoneSize();
                    waitBySecond(1);
                } else {
                    swipeDownAccordingToPhoneSize();
                    waitBySecond(1);
                }
            }
        }
    }

    public void swipeUpAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;
            System.out.println(width + "  " + height);

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 75) / 100;
            int swipeEndHeight = (height * 40) / 100;
            System.out.println("Start width: " + swipeStartWidth + " - Start height: " + swipeStartHeight + " - End height: " + swipeEndHeight);
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction((AndroidDriver) appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeEndHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeStartHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 75) / 100;
            int swipeEndHeight = (height * 20) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction((PerformsTouchActions) appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeEndHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeStartHeight))
                    .release()
                    .perform();
        }
    }


    public void swipeDownAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 75) / 100;
            int swipeEndHeight = (height * 25) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction((PerformsTouchActions) appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 75) / 100;
            int swipeEndHeight = (height * 40) / 100;
            // appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction((PerformsTouchActions) appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        }
    }

    private int countVowels(String word) {
        int count = 0;
        for (char ch : word.toCharArray()) {
            if (vowels.contains(ch)) {
                count++;
            }
        }
        return count;
    }

    public WebElement findElementByKeyWithoutAssert(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        WebElement WebElement = null;
        try {
            WebElement = selectorInfo.getIndex() > 0 ? (org.openqa.selenium.WebElement) findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return WebElement;
    }

    public List<WebElement> findElemenstByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        List<WebElement> WebElements = null;
        try {
            WebElements = findElements(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Elements not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();
        }
        return WebElements;
    }

    @Step("<StartX>,<StartY> coordinatından <EndX>,<EndY> coordinatına <times> kere swipe et")
    public void pointToPointSwipeWithCoordinats(int startX, int startY, int endX, int endY, int count) throws InterruptedException {
        Dimension d = appiumDriver.manage().window().getSize();


        for (int i = 0; i < count; i++) {
            waitBySecond(1);
            TouchAction action = new TouchAction((PerformsTouchActions) appiumDriver);
            action.press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(endX, endY))
                    .release().perform();
        }


    }
}
