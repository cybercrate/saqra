package com.wingmann.saqra;

import com.wingmann.saqra.config.ApplicationConfig;
import com.wingmann.saqra.config.Config;
import com.wingmann.saqra.generator.ImageBuilder;
import com.wingmann.saqra.generator.ImageCache;
import com.wingmann.saqra.generator.QrImageBuilder;
import com.wingmann.saqra.io.ImageWriter;
import com.wingmann.saqra.io.Writer;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.print("Saqra v1.0.0 (qr code generator)\n\n");

        Config config = new ApplicationConfig("./config.txt");

        ImageBuilder builder = new QrImageBuilder(config);
        Writer writer = new ImageWriter(config);

        Optional<ImageCache> result;

        while (true) {
            result = builder.build();

            if (result.isPresent()) {
                writer.write(result.get());
                continue;
            }
            break;
        }
    }
}
