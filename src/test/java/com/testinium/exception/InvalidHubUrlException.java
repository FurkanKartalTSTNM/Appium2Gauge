package com.testinium.exception;


import com.testinium.util.Error;

public class InvalidHubUrlException extends RuntimeException {

    public InvalidHubUrlException(String url) {
        super(String.format(Error.INVALID_HUB_URL_EXCEPTION, url));
    }
}
