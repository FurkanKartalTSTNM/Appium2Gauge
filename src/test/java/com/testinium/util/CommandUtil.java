package com.testinium.util;

import java.util.Arrays;
import java.util.List;

import static com.testinium.util.Constants.SESSION;

public class CommandUtil {

    public static Boolean isAcceptable(String pathInfo, List<String> commands) {
        List<String> pathSegments = Arrays.asList(pathInfo.split("/"));

        if (pathSegments.size() >= 4 && SESSION.equals(pathSegments.get(1))) {
            String commandPath = String.join("/", pathSegments.subList(3, pathSegments.size()));
            return !commands.contains(commandPath) && !commands.contains(pathSegments.getLast());
        }

        return false;
    }

}
