package com.testinium.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.testinium.model.Folder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class FileUtil {

    public static String saveFile(File file, String fileName, String fileType) throws IOException {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String sanitizedFileName = fileName.replaceAll("\\s+", "_");
        String name = String.format("%s-%s.%s", sanitizedFileName, timeStamp, fileType);
        String filePath = Paths.get(Folder.REPORTS.getFolderName(), name).toString();

        Files.createDirectories(Paths.get(Folder.REPORTS.getFolderName()));
        Files.copy(file.toPath(), Paths.get(filePath));

        return name;
    }


    public static void saveVideo(String base64Video, String fileName) throws IOException, InterruptedException {
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
