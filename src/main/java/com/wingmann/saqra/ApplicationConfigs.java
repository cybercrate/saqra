package com.wingmann.saqra;

import java.io.*;
import java.util.Scanner;

public class ApplicationConfigs {
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
        if (Directory.filesNotExists(getConfigPath())) {
            configure();
        }
        var path = loadConfig();

        if (Directory.isInvalidPath(path)) {
            System.out.print("[error]: path is not correct\n\n");
            throw new RuntimeException();
        }

        if (Directory.filesNotExists(path)) {
            if (Directory.createDirectories(path)) {
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

            if (input.isBlank() || Directory.isInvalidPath(input)) {
                System.err.print("[error]: path is invalid\n\n");
                continue;
            }
            break;
        }

        setConfig(input);
        System.out.print("[exec]: done.\n\n");
    }
}
