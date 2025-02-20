package com.testinium.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.testinium.model.Folder;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class FileUtil {

    public static String saveFile(File file,  String fileName, String fileType) throws IOException {
        String filePath = String.format("%s%s - %s.%s", Folder.REPORTS.getFolderName(), fileName, (new Date()).getTime(), fileType);
        Files.createDirectories(Paths.get(Folder.REPORTS.getFolderName()));
        Files.copy(file.toPath(), Paths.get(filePath));
        return filePath;
    }

    public static void saveVideoIOS(String base64Json, String fileName) throws IOException {
        JSONObject json = new JSONObject(base64Json);
        String base64Video = json.getString("value");
        if (base64Video.startsWith("data:video/mp4;base64,")) {
            base64Video = base64Video.replace("data:video/mp4;base64,", "");
        }
        base64Video = base64Video.replaceAll("[^A-Za-z0-9+/=]", "");

        try {
            byte[] videoBytes = Base64.getDecoder().decode(base64Video);
            String folderPath = Folder.REPORTS.getFolderName();
            Files.createDirectories(Paths.get(folderPath));
            String sanitizedFileName = fileName.replaceAll("[^a-zA-Z0-9_-]", "_");
            String filePath = String.format("%s/%s-%d.mp4", folderPath, sanitizedFileName, new Date().getTime());
            File file = new File(filePath);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(videoBytes);
                System.out.println("Video başarıyla kaydedildi: " + filePath);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Geçersiz Base64 verisi: " + e.getMessage());
            throw new IOException("Video kaydedilemedi, geçersiz Base64 verisi.", e);
        }
    }

    public static void saveVideoAndroid(String base64Video, String fileName) throws IOException {
        byte[] videoBytes = Base64.getDecoder().decode(base64Video);

        String folderPath = Folder.REPORTS.getFolderName();
        Files.createDirectories(Paths.get(folderPath));

        String filePath = String.format("%s/%s-%d.mp4", folderPath, fileName, new Date().getTime());
        File file = new File(filePath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(videoBytes);
        }
    }



    /**
     * Saves list of object as a json string
     */
    public static <T> void saveListOfElementToFile(List<T> list, String fileName) {
        if (list.isEmpty()) {
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            String folderPath = Folder.REPORTS.getFolderName();
            Files.createDirectories(Paths.get(folderPath));

            String filePath = String.format("%s/%s-%d.json", folderPath, fileName, new Date().getTime());
            objectMapper.writeValue(new File(filePath), list);
        } catch (IOException e) {
            System.err.println("Error saving JSON: " + e.getMessage());
        }
    }
}
