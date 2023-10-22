package com.wingmann.saqra;

import com.wingmann.saqra.config.ApplicationConfig;
import com.wingmann.saqra.config.Config;
import com.wingmann.saqra.generator.Generator;
import com.wingmann.saqra.generator.QrGenerator;

public class Main {
    public static void main(String[] args) {
        System.out.print("Saqra v1.0.0 (qr code generator)\n\n");

        Config config = new ApplicationConfig("./config.txt");
        Generator generator = new QrGenerator(config);

        while (true) {
            if (generator.build()) {
                continue;
            }
            break;
        }
    }
}
