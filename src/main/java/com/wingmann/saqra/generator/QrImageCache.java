package com.wingmann.saqra.generator;

import java.awt.image.BufferedImage;
import java.io.File;

public class QrImageCache {
    public BufferedImage data;
    public File file;

    public QrImageCache(BufferedImage data, File file) {
        this.data = data;
        this.file = file;
    }
}
