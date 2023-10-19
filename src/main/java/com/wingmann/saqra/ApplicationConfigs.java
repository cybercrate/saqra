package com.wingmann.saqra;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ApplicationConfigs {
    private static final String CONFIG_PATH = "./path.config";

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
            System.err.println("[error]: io error");
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
            System.err.println("[error]: io error");
            return "";
        }
    }

    public static boolean setup() {
        String input;

        if (Files.notExists(Path.of(CONFIG_PATH))) {
            System.out.println("[exec]: configuration");

            var scanner = new Scanner(System.in);

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
        }
        var path = loadConfig();

        if (Files.notExists(Path.of(path))) {
            return new File(path).mkdirs();
        }
        return false;
    }
}
