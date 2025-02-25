package com.testinium.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.testinium.util.Constants.SESSION;

public class CommandUtil {

    public static Boolean isAcceptable(String pathInfo, List<String> commands) {
        List<String> path = new ArrayList<>(Arrays.asList(pathInfo.split("/")));
        if (path.size() >= 3 && path.get(1).equals(SESSION) && path.size() > 3) {
            path = path.subList(3, path.size());
            String commandPath = StringUtils.join(path, "/");
            return !(commands.contains(commandPath) || commands.contains(path.get(path.size() - 1)));
        }
        return false;
    }

}
