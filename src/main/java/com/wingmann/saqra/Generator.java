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
        var file = ApplicationConfigs.createFile();
        var text = readLine();

        if (text.equalsIgnoreCase("/exit")) {
            System.out.print("[command]: exit");
            return false;
        }

        var image = generate(text);
        image.ifPresent(bufferedImage -> write(bufferedImage, file));

        System.out.print("[exec]: done.\n\n");
        return true;
    }

    private static String readLine() {
        var scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("[input]: ");
            input = scanner.nextLine();

            if (input.isBlank()) {
                System.err.print("[error]: input is blank\n\n");
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
            System.err.printf("[error]: %s%n", e.getMessage());
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

        if (ApplicationConfigs.filesNotExists(path)) {
            ApplicationConfigs.createDirectories(path);
        }

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            System.err.printf("[error]: %s%n", e.getMessage());
        }
    }
}
