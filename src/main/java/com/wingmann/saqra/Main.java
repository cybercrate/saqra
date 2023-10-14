package com.wingmann.saqra;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class Main {
    public static void main(String[] args) throws WriterException, IOException {
        System.out.print("Saqra v1.0.0 (qr code generator)\n\n");

        while (true) {
            var file = createFile();
            var text = getData();
            var image = generate(text, 256);

            write(image, "PNG", file);
            System.out.print("[exec]: done.\n\n");
        }
    }

    private static String getData() {
        System.out.print("[input]: ");
        return new Scanner(System.in).nextLine();
    }

    private static File createFile() {
        return new File(generateFileName());
    }

    private static String generateFileName() {
        var formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
        var formattedDateTime = LocalDateTime.now().format(formatter);
        var filename = String.format("qr-%s", formattedDateTime);

        return String.format("./%s.png", filename);
    }

    private static BufferedImage generate(String text, int size) throws WriterException, IOException {
        var hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        var writer = new QRCodeWriter();
        var bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hintMap);

        var matrixWidth = bitMatrix.getWidth();
        var matrixHeight = bitMatrix.getHeight();

        var image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        var graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixHeight);
        graphics.setColor(Color.BLACK);

        for (var i = 0; i < matrixWidth; ++i) {
            for (var j = 0; j < matrixHeight; ++j) {
                if (bitMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        return image;
    }

    private static void write(BufferedImage image, String fileType, File file) throws IOException {
        ImageIO.write(image, fileType, file);
    }
}