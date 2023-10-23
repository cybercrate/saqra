package com.wingmann.saqra.generator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import com.wingmann.saqra.config.Config;
import com.wingmann.saqra.io.FilesManager;
import com.wingmann.saqra.io.OutputFilesManager;
import com.wingmann.saqra.io.ConsoleInputManager;
import com.wingmann.saqra.io.InputManager;
import com.wingmann.saqra.log.ConsoleLogger;
import com.wingmann.saqra.log.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

public class QrImageBuilder implements ImageBuilder {
    private final Config config;
    private final Logger logger;
    private final FilesManager filesManager;
    private final InputManager inputManager;

    public QrImageBuilder(Config config) {
        this.config = config;
        this.logger = new ConsoleLogger();
        this.filesManager = new OutputFilesManager();
        this.inputManager = new ConsoleInputManager();
    }

    @Override
    public Optional<ImageCache> build() {
        String path = config.load().get();
        File file = filesManager.createFile(path);

        String text = input();
        handle(text);

        return generateFrom(text).map(data -> new QrImageCache(data, file));
    }

    private void handle(String text) {
        if (text.equalsIgnoreCase("/exit")) {
            logger.log("exit", false);
            System.exit(0);
        }
    }

    private String input() {
        String input;

        while (true) {
            input = inputManager.read("text").getData();

            if (input.isBlank()) {
                logger.error("input is blank", true);
                continue;
            }
            return input;
        }
    }

    private Optional<BufferedImage> generateFrom(String text) {
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = createHintMap();
        Optional<BitMatrix> matrix = createMatrix(text, hintMap);

        if (matrix.isPresent()) {
            Optional<BufferedImage> image = Optional.of(new BufferedImage(
                    matrix.get().getWidth(),
                    matrix.get().getHeight(),
                    BufferedImage.TYPE_INT_RGB));

            createGraphics(image.get(), matrix.get());
            return image;
        }
        return Optional.empty();
    }

    private Map<EncodeHintType, ErrorCorrectionLevel> createHintMap() {
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        return hintMap;
    }

    private Optional<BitMatrix> createMatrix(
            String text,
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap) {
        try {
            return Optional.of(new QRCodeWriter()
                    .encode(text, BarcodeFormat.QR_CODE, 256, 256, hintMap));
        } catch (WriterException e) {
            logger.error(e.getMessage(), false);
            return Optional.empty();
        }
    }

    private void createGraphics(BufferedImage image, BitMatrix matrix) {
        Graphics2D graphics = image.createGraphics();
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        graphics.setColor(Color.BLACK);

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (matrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
    }
}
