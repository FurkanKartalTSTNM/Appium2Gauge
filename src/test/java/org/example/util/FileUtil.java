package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.pdfbox.util.filetypedetector.FileType;
import org.example.model.Folder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class FileUtil {

    public static String saveFile(File file,  String fileName, FileType fileType) throws IOException {
        String filePath = String.format("%s%s - %s.%s", Folder.REPORTS.getFolderName(), fileName, (new Date()).getTime(), fileType.name());
        Files.createDirectories(Paths.get(Folder.REPORTS.getFolderName()));
        Files.copy(file.toPath(), Paths.get(filePath));
        return filePath;
    }
    public static String saveVideo(String result, String fileName) throws IOException {
        byte[] videoBytes = Base64.getDecoder().decode(result);

        String directory = Folder.REPORTS.getFolderName();
        Files.createDirectories(Paths.get(directory));

        String filePath = String.format("%s/%s_%d.mp4", directory, fileName, System.currentTimeMillis());
        File file = new File(filePath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(videoBytes);
        }

        return filePath;
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
