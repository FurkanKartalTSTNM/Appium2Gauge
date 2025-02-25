package com.testinium.exception;


import com.testinium.util.Error;

public class ScreenRecordingException extends RuntimeException {

    public ScreenRecordingException(String sessionId) {
        super(String.format(Error.SCREEN_RECORDING_EXCEPTION, sessionId));
    }
}
