package com.testinium.driver;

import com.testinium.exception.InvalidHubUrlException;
import com.testinium.util.Constants;
import com.testinium.util.TestiniumEnvironment;
import io.appium.java_client.remote.AppiumCommandExecutor;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.http.HttpRequest;

import java.net.URL;
import java.util.Collections;
import java.util.Date;

import static com.testinium.driver.TestiniumDriver.buildCommandResultLogs;

public class TestiniumCommandExecutor extends AppiumCommandExecutor {

    public TestiniumCommandExecutor(URL remoteServer) {
        super(Collections.emptyMap(), setRemoteUrl(remoteServer));

    }

    @Override
    public Response execute(Command command) {
        Date startDate = new Date();
        Response response = super.execute(command);
        HttpRequest encodedCommand = super.getCommandCodec().encode(command);
        buildCommandResultLogs(command, startDate, response, encodedCommand);
        return response;
    }

    public static URL setRemoteUrl(URL url){
        TestiniumEnvironment.init();

        if (!Constants.DEFAULT_PROFILE.equals(TestiniumEnvironment.profile)){
            return url;
        }
        try {
            return new URL(TestiniumEnvironment.hubUrl);
        } catch (Exception e) {
            throw new InvalidHubUrlException(TestiniumEnvironment.hubUrl);
        }
    }

}
