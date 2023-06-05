package com.cataas.cataasapi.service;

import com.cataas.cataasapi.CatAasApiTestUtils;
import com.cataas.cataasapi.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatAasDownloadCleanupServiceTest {
    private CatAasDownloadCleanupService catAasDownloadCleanupService;
    private Path tempDirectory;

    @Mock
    private FileRepository fileRepository;
    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        catAasDownloadCleanupService = new CatAasDownloadCleanupService(fileRepository);
        catAasDownloadCleanupService.setRootDirectory("test_directory");

        tempDirectory = CatAasApiTestUtils.createTempDirectory("test_directory");
        CatAasApiTestUtils.createFiles(tempDirectory);
    }
    @Test
    void testDownloadCleanupService(){
        catAasDownloadCleanupService.cleanupDownloads();
        File downloadsDirectory = new File(tempDirectory.toString());

        assertEquals(downloadsDirectory.listFiles().length, 0);
        Mockito.verify(fileRepository).deleteAll();

    }
}
