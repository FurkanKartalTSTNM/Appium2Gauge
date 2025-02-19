package com.testinium;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import org.assertj.core.api.Assertions;
import com.testinium.model.SelectorInfo;
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

}
