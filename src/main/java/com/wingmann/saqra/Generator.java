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
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class Generator {
    private static final int WIDTH = 256;
    private static final int HEIGHT = 256;

    public static boolean start() {
        var file = ApplicationConfigs.createFile();
        var text = readLine();

        if (text.equalsIgnoreCase("/exit")) {
            System.out.print("[command]: exit");
            return false;
        }

        try {
            var image = generate(text);
            try {
                write(image, file);
            } catch (IOException e) {
                System.err.println("[error]: io exception");
            }
        } catch (WriterException e) {
            System.err.println("[error]: writer exception");
        }

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

    private static BufferedImage generate(String text) throws WriterException {
        var hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        var matrix = new QRCodeWriter()
                .encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hintMap);

        var image = new BufferedImage(
                matrix.getWidth(),
                matrix.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        createGraphics(image, matrix);

        return image;
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

    private static void write(BufferedImage image, java.io.File file) throws IOException {
        ImageIO.write(image, "png", file);
    }
}
