package com.wingmann.saqra.io;

import com.wingmann.saqra.generator.QrImageCache;

public interface Writer {
    void write(QrImageCache image);
}
