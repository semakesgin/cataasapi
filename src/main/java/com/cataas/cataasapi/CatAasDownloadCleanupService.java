package com.cataas.cataasapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CatAasDownloadCleanupService {
    @Value("${root.directory}")
    private String rootDirectory;

    @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight
    public void cleanupDownloads() {
        File downloadsDirectory = new File(rootDirectory);
        deleteContents(downloadsDirectory);
    }

    private void deleteContents(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteContents(file);
                }
                file.delete();
            }
        }
    }
}