package org.example;

import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;

import java.io.IOException;
import java.net.URL;


public class LoggingCommandExecutor extends HttpCommandExecutor {


    public LoggingCommandExecutor(URL remoteServer) {
        super(remoteServer);
    }

    @Override
    public Response execute(Command command) throws IOException {
        System.out.println("Request: " + command.getName() + " | Payload: " + command.getParameters());

        Response response = super.execute(command);

        System.out.println("Response: " + response + " | Result: " + response.getValue());
        return response;
    }

}
