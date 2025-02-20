package com.testinium.selector;

import com.testinium.model.ElementInfo;
import org.openqa.selenium.By;

import static io.appium.java_client.AppiumBy.*;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;


public class IOSSelector implements Selector {

    @Override
    public By getElementInfoToBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getIosType().equals("css")) {
            by = cssSelector(elementInfo.getIosValue());
        } else if (elementInfo.getIosValue().equals("id")) {
            by = id(elementInfo.getIosValue());
        } else if (elementInfo.getIosType().equals("xpath")) {
            by = xpath(elementInfo.getIosValue());
        } else if (elementInfo.getIosType().equals("class")) {
            by = className(elementInfo.getIosValue());
        } else if (elementInfo.getIosType().equals("name")) {
            by = name(elementInfo.getIosValue());
        } else if (elementInfo.getIosType().equals("classChain")) {
            by = iOSClassChain(elementInfo.getIosValue());
        }
        return by;
    }

    @Override
    public int getElementInfoToIndex(ElementInfo elementInfo) {
        return elementInfo.getIosIndex();
    }
}
