package org.example;

import io.appium.java_client.remote.AppiumCommandExecutor;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;


public class LoggingCommandExecutor extends AppiumCommandExecutor {

    public LoggingCommandExecutor(URL remoteServer) {
        super(Collections.emptyMap(), remoteServer);
    }

    @Override
    public Response execute(Command command) {
        System.out.println("Request: " + command.getName() + " | Payload: " + command.getParameters());

        Response response = super.execute(command);

        System.out.println("Response: " + response + " | Result: " + response.getValue());
        return response;
    }
}
