package com.wingmann.saqra.io;

import com.wingmann.saqra.config.Config;
import com.wingmann.saqra.generator.ImageCache;
import com.wingmann.saqra.log.ConsoleLogger;
import com.wingmann.saqra.log.Logger;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ImageWriter implements Writer {
    private final Config config;
    private final Logger logger;
    private final FilesManager filesManager;

    public ImageWriter(Config config) {
        this.config = config;
        this.logger = new ConsoleLogger();
        this.filesManager = new OutputFilesManager();
    }

    @Override
    public void write(ImageCache image) {
        config.load();
        String path = config.get();

        if (filesManager.filesNotExists(path)) {
            filesManager.createDirectories(path);
        }

        try {
            ImageIO.write(image.getData(), "png", image.getTarget());
        } catch (IOException e) {
            logger.error(e.getMessage(), false);
        }
    }
}
