package com.wingmann.saqra;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Directory {
    public static File createFile(String path) {
        return new File(generateFilename(path));
    }

    private static String generateFilename(String path) {
        var formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
        var meta = LocalDateTime.now().format(formatter);

        return String.format("%s/qr-%s.png", path, meta);
    }

    public static boolean isInvalidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException e) {
            return true;
        }
        return false;
    }

    public static boolean filesNotExists(String path) {
        return Files.notExists(Path.of(path));
    }

    public static boolean createDirectories(String path) {
        return new File(path).mkdirs();
    }
}
