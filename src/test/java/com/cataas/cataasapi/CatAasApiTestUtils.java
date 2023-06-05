package com.cataas.cataasapi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CatAasApiTestUtils {
    public static Path createTempDirectory(String tempDirectory) throws IOException {
        Path tempDirectory1 = Paths.get(tempDirectory);
        deleteTempFolder(tempDirectory1);
        return Files.createDirectory(tempDirectory1);
    }

    public static void createFiles(Path tempDirectory) throws IOException {
        Path sub1 = tempDirectory.resolve("sub1");
        Files.createDirectory(sub1);

        Path sub2 = tempDirectory.resolve("sub2");
        Files.createDirectory(sub2);

        Path image1 = Files.createFile(Paths.get(sub1.toString(), "image1.jpg"));
        Path image2 = Files.createFile(Paths.get(sub2.toString(), "image2.jpg"));
        Path nonImageFile = Files.createFile(Paths.get(tempDirectory.toString(), "text.txt"));
    }
    public static void deleteTempFolder(Path tempDirectory) throws IOException {
        deleteContents(tempDirectory.toFile());
        Files.deleteIfExists(tempDirectory);
    }
    private static void deleteContents(File directory) {
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
