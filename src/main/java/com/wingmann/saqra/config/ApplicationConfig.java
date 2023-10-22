package com.wingmann.saqra.config;

import com.wingmann.saqra.log.ConsoleLogger;
import com.wingmann.saqra.files.FilesManager;
import com.wingmann.saqra.log.Logger;
import com.wingmann.saqra.files.OutputFilesManager;

import java.io.*;
import java.util.Scanner;

public class ApplicationConfig implements Config {
    private final String configPath;
    private final Logger logger;
    private final FilesManager filesManager;

    public ApplicationConfig(String configPath) {
        this.configPath = configPath;
        logger = new ConsoleLogger();
        filesManager = new OutputFilesManager();
    }

    @Override
    public void setConfig(String path) {
        try (FileWriter writer = new FileWriter(configPath)) {
            writer.write(path);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String loadConfig() {
        try {
            File config = new File(configPath);
            BufferedReader reader = new BufferedReader(new FileReader(config));
            String path = reader.readLine();

            reader.close();
            return path;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return "";
        }
    }

    @Override
    public void setup() {
        if (filesManager.filesNotExists(configPath)) {
            configure();
        }
        String path = loadConfig();

        if (filesManager.isInvalidPath(path)) {
            logger.errorln("path is not correct");
            throw new RuntimeException();
        }

        if (filesManager.filesNotExists(path)) {
            if (filesManager.createDirectories(path)) {
                logger.logln("directories was created");
            }
        }
    }

    private void configure() {
        logger.log("configuration");

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            logger.input("path");
            input = scanner.nextLine();

            if (input.isBlank() || filesManager.isInvalidPath(input)) {
                logger.errorln("path is invalid");
                continue;
            }
            break;
        }

        setConfig(input);
        logger.logln("done.");
    }
}
