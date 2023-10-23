package com.wingmann.saqra.config;

import com.wingmann.saqra.io.ConsoleInputManager;
import com.wingmann.saqra.io.InputManager;
import com.wingmann.saqra.log.ConsoleLogger;
import com.wingmann.saqra.io.FilesManager;
import com.wingmann.saqra.log.Logger;
import com.wingmann.saqra.io.OutputFilesManager;

import java.io.*;

public class ApplicationConfig implements Config {
    private final Logger logger;
    private final FilesManager filesManager;
    private final InputManager inputManager;
    private final String path;
    private String data;

    public ApplicationConfig(String path) {
        this.logger = new ConsoleLogger();
        this.filesManager = new OutputFilesManager();
        this.inputManager = new ConsoleInputManager();
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
        try (FileWriter writer = new FileWriter(this.path)) {
            writer.write(path);
        } catch (IOException e) {
            logger.error(e.getMessage(), false);
        }
    }

    @Override
    public Config load() {
        try {
            File config = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(config));
            String path = reader.readLine();

            reader.close();
            data = path;
        } catch (IOException e) {
            logger.error(e.getMessage(), false);
        }
        return this;
    }

    @Override
    public Config setup() {
        if (filesManager.filesNotExists(path)) {
            configure();
        }
        load();

        if (filesManager.isInvalidPath(data)) {
            logger.error("path is not correct", true);
            throw new RuntimeException();
        }

        if (filesManager.filesNotExists(data)) {
            if (filesManager.createDirectories(data)) {
                logger.log("directories was created", true);
            }
        }
        return this;
    }

    private void configure() {
        logger.log("configuration", false);
        String input;

        while (true) {
            input = inputManager.read("path").getData();

            if (input.isBlank() || filesManager.isInvalidPath(input)) {
                logger.error("path is invalid", true);
                continue;
            }
            break;
        }

        set(input);
        logger.log("done.", true);
    }
}
