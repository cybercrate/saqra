package com.wingmann.saqra.config;

import com.wingmann.saqra.log.ConsoleLogger;
import com.wingmann.saqra.files.FilesManager;
import com.wingmann.saqra.log.Logger;
import com.wingmann.saqra.files.OutputFilesManager;

import java.io.*;
import java.util.Scanner;

public class ApplicationConfig implements Config {
    private final Logger logger;
    private final FilesManager filesManager;
    private final String path;
    private String data;

    public ApplicationConfig(String path) {
        this.logger = new ConsoleLogger();
        this.filesManager = new OutputFilesManager();
        this.path = path;
        this.data = "";

        setup();
    }

    @Override
    public String get() {
        return data;
    }

    @Override
    public void set(String path) {
        try (FileWriter writer = new FileWriter(this.path);) {
            writer.write(path);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void load() {
        try {
            File config = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(config));
            String path = reader.readLine();

            reader.close();
            data = path;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void setup() {
        if (filesManager.filesNotExists(path)) {
            configure();
        }
        load();

        if (filesManager.isInvalidPath(data)) {
            logger.errorln("path is not correct");
            throw new RuntimeException();
        }

        if (filesManager.filesNotExists(data)) {
            if (filesManager.createDirectories(data)) {
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

        set(input);
        logger.logln("done.");
    }
}
