package com.cataas.cataasapi.service;

import com.cataas.cataasapi.CatAasApiTestUtils;
import com.cataas.cataasapi.entity.FileDetails;
import com.cataas.cataasapi.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class CatAasApiReportServiceTest {
    private CatAasApiReportService catAasApiReportService;

    private Path tempDirectory;

    private Path temp2;

    @Mock
    private FileRepository fileRepository;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);

        tempDirectory = CatAasApiTestUtils.createTempDirectory("test_directory");
        temp2 = CatAasApiTestUtils.createTempDirectory("test_directory2");
        CatAasApiTestUtils.createFiles(tempDirectory);
        CatAasApiTestUtils.createFiles(temp2);

        catAasApiReportService = new CatAasApiReportService(fileRepository);
        catAasApiReportService.setRootDirectory(tempDirectory.toString());

    }

    @Test
    void testCountImageFilesInDirectories() throws IOException {
        try {
            int count = catAasApiReportService.countImageFilesInDirectories(tempDirectory.toString());
            assertEquals(2, count, "Incorrect count of image files");
        } finally {
            CatAasApiTestUtils.deleteTempFolder(tempDirectory);
            CatAasApiTestUtils.deleteTempFolder(temp2);
        }
    }

    @Test
    void testWriteSubfolderDetails() throws IOException {
        String expectedOutput = "sub1 - 1\n" +
                "- image1.jpg\n" +
                "\n" +
                "sub2 - 1\n" +
                "- image2.jpg\n" +
                "\n";

        File tempFile = File.createTempFile("temp.txt", null);

        catAasApiReportService.writeSubfolderDetails(new FileWriter(tempFile), catAasApiReportService.getRootDirectory());

        String actualOutput = readFile(tempFile);
        assertEquals(expectedOutput, actualOutput);

        CatAasApiTestUtils.deleteTempFolder(tempDirectory);
        CatAasApiTestUtils.deleteTempFolder(temp2);
    }

    @Test
    public void testWriteCreatedFileDetailsToDb() throws IOException {
        String fileNameWithPath = "path/to/file.txt";
        String fileName = "file.txt";
        long size = 112;
        catAasApiReportService.writeCreatedFileDetailsToDb(fileNameWithPath, fileName, size);
        FileDetails expectedFileDetails = new FileDetails(fileName, fileNameWithPath, size);

        verify(fileRepository).save(Mockito.refEq(expectedFileDetails));
    }


    private String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }


}
