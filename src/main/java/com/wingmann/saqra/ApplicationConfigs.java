package com.wingmann.saqra;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ApplicationConfigs {
    public static File createFile() {
        return new File(generateFilename());
    }

    private static String generateFilename() {
        var path = loadConfig();
        var formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");

        return String.format("%s/qr-%s.png", path, LocalDateTime.now().format(formatter));
    }

    private static String getConfigPath() {
        return "./config.txt";
    }

    private static void setConfig(String path) {
        try (var writer = new FileWriter(getConfigPath())) {
            writer.write(path);
        } catch (IOException e) {
            System.err.printf("[error]: %s%n", e.getMessage());
        }
    }

    public static String loadConfig() {
        try {
            var config = new File(getConfigPath());
            var reader = new BufferedReader(new FileReader(config));
            var path = reader.readLine();

            reader.close();
            return path;
        } catch (IOException e) {
            System.err.printf("[error]: %s%n", e.getMessage());
            return "";
        }
    }

    public static void setup() {
        if (filesNotExists(getConfigPath())) {
            configure();
        }
        var path = loadConfig();

        if (isInvalidPath(path)) {
            System.out.print("[error]: path is not correct\n\n");
            throw new RuntimeException();
        }

        if (filesNotExists(path)) {
            if (createDirectories(path)) {
                System.out.print("[exec]: directories was created\n\n");
            }
        }
    }

    private static void configure() {
        System.out.println("[exec]: configuration");

        var scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("[path]: ");
            input = scanner.nextLine();

            if (input.isBlank() || isInvalidPath(input)) {
                System.err.print("[error]: path is invalid\n\n");
                continue;
            }
            break;
        }

        setConfig(input);
        System.out.print("[exec]: done.\n\n");
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
