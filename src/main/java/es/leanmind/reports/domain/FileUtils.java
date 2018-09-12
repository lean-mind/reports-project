package es.leanmind.reports.domain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static java.nio.file.FileSystems.getDefault;

public class FileUtils {

    private static String processedFilesFolderName = "Processed";

    public static void createFolder(String path) throws IOException {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static void moveProcessedFile(String currentFilePath, String processedFilesFolder) throws IOException {
        File file = new File(currentFilePath);
        if(file.exists() && !isSamePath(file, processedFilesFolder)){
            copyProcessedFile(file, processedFilesFolder);
            file.delete();
        }
    }

    private static boolean isSamePath(File processedFile, String nameFolderDest) {
        return processedFile.getParentFile().getName().equals(nameFolderDest);
    }

    private static void copyProcessedFile(File processedFile, String nameFolderDest) throws IOException {
        String destinationFolder = processedFile.getParent() + getDefault().getSeparator() + processedFilesFolderName;
        createFolder(destinationFolder);
        File movedFile = new File(destinationFolder + getDefault().getSeparator() + processedFile.getName());
        Files.move(processedFile.toPath(), movedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
