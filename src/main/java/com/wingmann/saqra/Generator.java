package com.wingmann.saqra;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Optional;
import java.util.Scanner;

public class Generator {
    public static boolean start() {
        var path = ApplicationConfigs.loadConfig();
        var file = Directory.createFile(path);
        var text = readLine();

        if (text.equalsIgnoreCase("/exit")) {
            Logger.log("exit", false);
            return false;
        }

        var image = generate(text);
        image.ifPresent(bufferedImage -> write(bufferedImage, file));

        Logger.log("done.", true);
        return true;
    }

    private static String readLine() {
        var scanner = new Scanner(System.in);
        String input;

        while (true) {
            Logger.input();
            input = scanner.nextLine();

            if (input.isBlank()) {
                Logger.error("input is blank", true);
                continue;
            }
            return input;
        }
    }

    private static Optional<BufferedImage> generate(String text) {
        var hintMap = createHintMap();
        var matrix = generateMatrix(text, hintMap);

        if (matrix.isPresent()) {
            var image = Optional.of(new BufferedImage(
                    matrix.get().getWidth(),
                    matrix.get().getHeight(),
                    BufferedImage.TYPE_INT_RGB));

            createGraphics(image.get(), matrix.get());

            return image;
        }
        return Optional.empty();
    }

    private static Hashtable<EncodeHintType, ErrorCorrectionLevel> createHintMap() {
        var hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        return hintMap;
    }

    private static Optional<BitMatrix> generateMatrix(
            String text,
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap) {
        try {
            return Optional.of(new QRCodeWriter()
                    .encode(text, BarcodeFormat.QR_CODE, 256, 256, hintMap));
        } catch (WriterException e) {
            Logger.error(e.getMessage(), false);
            return Optional.empty();
        }
    }

    private static void createGraphics(BufferedImage image, BitMatrix matrix) {
        var graphics = image.createGraphics();
        var width = matrix.getWidth();
        var height = matrix.getHeight();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        graphics.setColor(Color.BLACK);

        for (var i = 0; i < width; ++i) {
            for (var j = 0; j < height; ++j) {
                if (matrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
    }

    private static void write(BufferedImage image, File file) {
        var path = ApplicationConfigs.loadConfig();

        if (Directory.filesNotExists(path)) {
            Directory.createDirectories(path);
        }

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            Logger.error(e.getMessage(), false);
        }
    }
}
