package com.cataas.cataasapi.service;

import com.cataas.cataasapi.CatAasApiTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CatAasApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private CatAasApiService catAasApiService;
    private Path tempDirectory;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        catAasApiService = new CatAasApiService();
        tempDirectory = CatAasApiTestUtils.createTempDirectory("test_directory//");
        catAasApiService.setRootDirectory(tempDirectory.toString());
    }

    @Test
    void testDownloadImageFile() throws Exception {
        String catImageUrlStr = "https://cataas.com/cat";

        byte[] imageBytes = {1, 2, 3}; // Mock image bytes

        URI catImageUri = new URL(catImageUrlStr).toURI();

        Mockito.when(restTemplate.getForObject(catImageUri, byte[].class)).thenReturn(imageBytes);

        ResponseEntity<Resource> expectedResponse = createExpectedResponse(imageBytes);

        ResponseEntity<Resource> actualResponse = catAasApiService.downloadImageFile(catImageUrlStr, "test_file", "//test_folder");

        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        Assertions.assertEquals(expectedResponse.getHeaders().getContentType(), actualResponse.getHeaders().getContentType());
        Assertions.assertTrue(compareResources(expectedResponse.getBody(), actualResponse.getBody()));

        Path filePath = tempDirectory.resolve("test_folder").resolve("test_file.jpg");
        Assertions.assertTrue(Files.exists(filePath));
        CatAasApiTestUtils.deleteTempFolder(tempDirectory);
    }

    private ResponseEntity<Resource> createExpectedResponse(byte[] imageBytes) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        Resource resource = new FileSystemResource(createTempFile(imageBytes));
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    private File createTempFile(byte[] imageBytes) {
        try {
            Path testFolder = tempDirectory.resolve("test_folder");
            Files.createDirectory(testFolder);
            Path testFile = Files.createFile(Paths.get(testFolder.toString(), "test_file.jpg"));

            File tempFile = testFile.toFile();
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                outputStream.write(imageBytes);
            }
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean compareResources(Resource expectedResource, Resource actualResource) {
        try {
            byte[] expectedBytes = Files.readAllBytes(expectedResource.getFile().toPath());
            byte[] actualBytes = Files.readAllBytes(actualResource.getFile().toPath());
            return expectedBytes.length == actualBytes.length && java.util.Arrays.equals(expectedBytes, actualBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
