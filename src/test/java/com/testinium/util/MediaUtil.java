package com.testinium.util;

import com.testinium.driver.TestiniumDriver;
import io.appium.java_client.AppiumDriver;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.testinium.util.Constants.VIDEO;
import static com.testinium.util.FileUtil.saveVideo;
import static com.testinium.util.FileUtil.saveFile;
public class MediaUtil {

    public static String takeScreenShot(Command command) throws IOException {
        AppiumDriver driver = TestiniumDriver.getDriver(command.getSessionId());
        File screenShotFile = driver.getScreenshotAs(OutputType.FILE);
        return saveFile(screenShotFile, command.getName(), "png");
    }

    public static boolean recordingAllowed() {
        return Constants.DEFAULT_PROFILE.equals(TestiniumEnvironment.profile) || Constants.DEFAULT_VIDEO_ENABLED.equals(TestiniumEnvironment.takeScreenRecording);
    }

    public static void startScreenRecord(RemoteWebDriver driver) {
        if (!TestiniumEnvironment.profile.equals("testinium")){
            return;
        }
        Map<String, Object> params = new HashMap<>();
        driver.executeScript(Constants.Command.START_RECORDING, params);
    }
    public static void startScreenRecordingForIOS(URL remoteUrl, SessionId sessionId) throws Exception {
        String url = remoteUrl +"/session/"+ sessionId + "/appium/start_recording_screen";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-Type", "application/json");

            String jsonBody = "{}";
            request.setEntity(new StringEntity(jsonBody));

            client.execute(request);
            System.out.println("🎥 Ekran kaydı başladı...");
        }
    }

    public static void stopScreenRecordingForIOS(URL remoteUrl, String sessionId) throws Exception {
        String url = remoteUrl +"/session/"+ sessionId + "/appium/stop_recording_screen";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-Type", "application/json");

            String jsonBody = "{}";
            request.setEntity(new StringEntity(jsonBody));

            try (CloseableHttpResponse response = client.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = new JSONObject(responseBody);
                String base64Json = jsonResponse.getString("value");

                System.out.println("📥 Kayıt tamamlandı!");
                saveVideo(base64Json, "VIDEO");

            }

        }
    }

    public static void saveScreenRecord(RemoteWebDriver driver) {
        if (!TestiniumEnvironment.profile.equals("testinium")){
            return;
        }
        Object result = driver.executeScript(Constants.Command.STOP_RECORDING, new HashMap<>());
        try {
            FileUtil.saveVideo((String) result, VIDEO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
