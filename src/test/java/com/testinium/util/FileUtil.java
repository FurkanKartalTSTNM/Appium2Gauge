package com.testinium.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.testinium.model.Folder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
public class FileUtil {

    public static String saveFile(File file, String fileName, String fileType) throws IOException {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String sanitizedFileName = fileName.replaceAll("\\s+", "_");
        String name = String.format("%s-%s.%s", sanitizedFileName, timeStamp, fileType);
        String filePath = Paths.get(Folder.SCREENHOTS.getFolderName(), name).toString();
        Files.createDirectories(Paths.get(Folder.SCREENHOTS.getFolderName()));
        Files.copy(file.toPath(), Paths.get(filePath));
        log.info("File saved successfully to {}", filePath);
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
            log.error("Error occurred while saving list {} to file  {} ", list, fileName, e);
        }
    }
}
