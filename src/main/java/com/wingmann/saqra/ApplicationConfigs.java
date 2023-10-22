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
            Logger.error(e.getMessage());
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
            Logger.error(e.getMessage());
            return "";
        }
    }

    public static void setup() {
        if (Directory.filesNotExists(getConfigPath())) {
            configure();
        }
        var path = loadConfig();

        if (Directory.isInvalidPath(path)) {
            Logger.errorln("path is not correct");
            throw new RuntimeException();
        }

        if (Directory.filesNotExists(path)) {
            if (Directory.createDirectories(path)) {
                Logger.logln("directories was created");
            }
        }
    }

    private static void configure() {
        Logger.log("configuration");

        var scanner = new Scanner(System.in);
        String input;

        while (true) {
            Logger.input("path");
            input = scanner.nextLine();

            if (input.isBlank() || Directory.isInvalidPath(input)) {
                Logger.errorln("path is invalid");
                continue;
            }
            break;
        }

        setConfig(input);
        Logger.logln("done.");
    }
}
