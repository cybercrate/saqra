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
    private static final String CONFIG_PATH = "./config.txt";

    public static File createFile() {
        return new File(generateFilename());
    }

    private static String generateFilename() {
        var path = loadConfig();
        var formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");

        return String.format("%s/qr-%s.png", path, LocalDateTime.now().format(formatter));
    }

    private static void setConfig(String path) {
        try (var writer = new FileWriter(CONFIG_PATH)) {
            writer.write(path);
        } catch (IOException e) {
            System.err.printf("[error]: %s%n", e.getMessage());
        }
    }

    private static String loadConfig() {
        try {
            var config = new File(CONFIG_PATH);
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
        if (Files.notExists(Path.of(CONFIG_PATH))) {
            setupNew();
        }
        var path = loadConfig();

        if (!isValidPath(path)) {
            System.out.print("[error]: path is not correct\n\n");
            throw new RuntimeException();
        }

        if (Files.notExists(Path.of(path))) {
            if (createDirectories(path)) {
                System.out.print("[exec]: directories was created\n\n");
            }
        }
    }

    private static void setupNew() {
        System.out.println("[exec]: configuration");

        var scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("[path]: ");
            input = scanner.nextLine();

            if (input.isBlank()) {
                System.err.print("[error]: input is blank\n\n");
                continue;
            }
            break;
        }

        setConfig(input);
        System.out.print("[exec]: done.\n\n");
    }

    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException e) {
            return false;
        }
        return true;
    }

    private static boolean createDirectories(String path) {
        return new File(path).mkdirs();
    }
}
