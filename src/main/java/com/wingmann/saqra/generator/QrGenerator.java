package com.wingmann.saqra.generator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import com.wingmann.saqra.config.Config;
import com.wingmann.saqra.files.FilesManager;
import com.wingmann.saqra.files.OutputFilesManager;
import com.wingmann.saqra.log.ConsoleLogger;
import com.wingmann.saqra.log.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class QrGenerator implements Generator {
    private final Config config;
    private final Logger logger;
    private final FilesManager filesManager;

    public QrGenerator(Config config) {
        this.config = config;
        logger = new ConsoleLogger();
        filesManager = new OutputFilesManager();
    }

    @Override
    public boolean build() {
        String path = config.loadConfig();
        File file = filesManager.createFile(path);
        String text = readLine();

        if (text.equalsIgnoreCase("/exit")) {
            logger.log("exit");
            return false;
        }
        Optional<BufferedImage> image = generate(text);
        image.ifPresent(bufferedImage -> write(bufferedImage, file));

        logger.logln("done.");
        return true;
    }

    private String readLine() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            logger.input("text");
            input = scanner.nextLine();

            if (input.isBlank()) {
                logger.errorln("input is blank");
                continue;
            }
            return input;
        }
    }

    private Optional<BufferedImage> generate(String text) {
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = createHintMap();
        Optional<BitMatrix> matrix = generateMatrix(text, hintMap);

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

    private Optional<BitMatrix> generateMatrix(
            String text,
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap) {
        try {
            return Optional.of(new QRCodeWriter()
                    .encode(text, BarcodeFormat.QR_CODE, 256, 256, hintMap));
        } catch (WriterException e) {
            logger.error(e.getMessage());
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

    private void write(BufferedImage image, File file) {
        String path = config.loadConfig();

        if (filesManager.filesNotExists(path)) {
            filesManager.createDirectories(path);
        }

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}