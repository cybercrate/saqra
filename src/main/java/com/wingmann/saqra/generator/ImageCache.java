package com.wingmann.saqra.generator;

import java.awt.image.BufferedImage;
import java.io.File;

public interface ImageCache {
    BufferedImage getData();
    File getTarget();
}
