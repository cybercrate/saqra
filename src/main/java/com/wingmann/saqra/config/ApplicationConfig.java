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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.errorLine("path is not correct");
            throw new RuntimeException();
        }

        if (filesManager.filesNotExists(data)) {
            if (filesManager.createDirectories(data)) {
                logger.logLine("directories was created");
            }
        }
        return this;
    }

    private void configure() {
        logger.log("configuration");
        String input;

        while (true) {
            input = inputManager.read("path").getData();

            if (input.isBlank() || filesManager.isInvalidPath(input)) {
                logger.errorLine("path is invalid");
                continue;
            }
            break;
        }
        set(input);
        logger.logLine("done.");
    }
}
