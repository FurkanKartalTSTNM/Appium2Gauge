package com.testinium.util;

import org.openqa.selenium.remote.Response;

public class StringUtil {

    public static String subStringWithMaximumLength(Response response, Integer maximumLength) {
        if (response == null || response.getValue() == null) {
            return "";
        }

        String valueStr = response.getValue().toString();
        return valueStr.length() < maximumLength ? valueStr : valueStr.substring(0, maximumLength);
    }

}
