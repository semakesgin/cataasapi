package com.cataas.cataasapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
public class CatAasApiReportService {
    @Value("${root.directory}")
    private String rootDirectory;
    public ResponseEntity<Resource> generateReport() {
        String fileName=null;
        try {
            fileName = rootDirectory+ getCurrentTimestampForReportName()+"CatsReport.txt";
            FileWriter writer = new FileWriter(fileName);

            writer.write("Total assets: " + countImageFilesInDirectories(rootDirectory) + "\n");
            writer.write("Date of report: " + getCurrentTimestamp() + "\n\n");

            writeSubfolderDetails(writer, rootDirectory);
            writer.close();
            return downloadCreatedReportFile(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Resource> downloadCreatedReportFile(String fileName) throws IOException {
        File reportFile = new File(fileName);
        Resource resource = new FileSystemResource(reportFile);

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", reportFile.getName());

        // Copy the report file to a temporary location
        Path tempFilePath = Files.createTempFile("temp", reportFile.getName());
        Files.copy(reportFile.toPath(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
        //Files.delete(reportFile.toPath());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private void writeSubfolderDetails(FileWriter writer, String directory) throws IOException {
        File[] subFolders = new File(directory).listFiles(File::isDirectory);

        if (subFolders != null) {
            for (File subFolder : subFolders) {
                writer.write(subFolder.getName() + " - " + countImageFilesInDirectories(subFolder.getPath()) + "\n");

                File[] files = subFolder.listFiles(File::isFile);
                if (files != null) {
                    Arrays.stream(files)
                            .forEach(file -> {
                                try {
                                    writer.write("- " + file.getName() + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                }

                writer.write("\n");
            }
        }
    }
    private String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    private String getCurrentTimestampForReportName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }
    private int countImageFilesInDirectories(String directory) throws IOException {
        return (int) Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().endsWith(".jpg"))
                .count();
    }
}
