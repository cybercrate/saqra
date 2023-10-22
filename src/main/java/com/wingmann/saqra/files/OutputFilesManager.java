package com.wingmann.saqra.files;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OutputFilesManager implements FilesManager {
    @Override
    public File createFile(String path) {
        return new File(generateFilename(path));
    }

    private String generateFilename(String path) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
        String meta = LocalDateTime.now().format(formatter);

        return String.format("%s/qr-%s.png", path, meta);
    }

    @Override
    public boolean isInvalidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException e) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filesNotExists(String path) {
        return Files.notExists(Path.of(path));
    }

    @Override
    public boolean createDirectories(String path) {
        return new File(path).mkdirs();
    }
}
