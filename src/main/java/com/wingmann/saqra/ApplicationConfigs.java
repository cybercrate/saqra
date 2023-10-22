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
            Logger.error(e.getMessage(), false);
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
            Logger.error(e.getMessage(), false);
            return "";
        }
    }

    public static void setup() {
        if (Directory.filesNotExists(getConfigPath())) {
            configure();
        }
        var path = loadConfig();

        if (Directory.isInvalidPath(path)) {
            Logger.error("path is not correct", true);
            throw new RuntimeException();
        }

        if (Directory.filesNotExists(path)) {
            if (Directory.createDirectories(path)) {
                Logger.log("directories was created", true);
            }
        }
    }

    private static void configure() {
        Logger.log("configuration", false);

        var scanner = new Scanner(System.in);
        String input;

        while (true) {
            Logger.input();
            input = scanner.nextLine();

            if (input.isBlank() || Directory.isInvalidPath(input)) {
                Logger.error("path is invalid", true);
                continue;
            }
            break;
        }

        setConfig(input);
        Logger.log("done.", true);
    }
}
