package com.cataas.cataasapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class CatAasApiService {
    @Value("${root.directory}")
    private String rootDirectory;
    public ResponseEntity<Resource> downloadImageFile(String catImageUrlStr, String fileName, String folderName) {
        try {
            URL catImageUrl = new URL(catImageUrlStr);
            RestTemplate restTemplate = new RestTemplate();
            byte[] imageBytes = restTemplate.getForObject(catImageUrl.toURI(), byte[].class);

            if (imageBytes != null) {
                Path path = Files.createDirectories(Paths.get(rootDirectory+folderName+"/"));
                Path tempFile = Files.createFile(Path.of(path+"//"+ fileName +".jpg"));
                try (OutputStream outputStream = new FileOutputStream(tempFile.toFile())) {
                    outputStream.write(imageBytes);
                }
                Resource resource = new FileSystemResource(tempFile.toFile());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);

                return ResponseEntity.ok().headers(headers).body(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(500).build();
    }
}
