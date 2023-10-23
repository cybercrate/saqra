package com.wingmann.saqra.generator;

import java.awt.image.BufferedImage;
import java.io.File;

public class QrImageCache implements ImageCache {
    private final BufferedImage data;
    private final File target;

    public QrImageCache(BufferedImage data, File target) {
        this.data = data;
        this.target = target;
    }

    @Override
    public BufferedImage getData() {
        return data;
    }

    @Override
    public File getTarget() {
        return target;
    }
}
