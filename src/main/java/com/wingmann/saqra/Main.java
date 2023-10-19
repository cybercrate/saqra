package com.wingmann.saqra;

public class Main {
    public static void main(String[] args) {
        System.out.print("Saqra v1.0.0 (qr code generator)\n\n");

        ApplicationConfigs.setup();

        while (true) {
            if (Generator.start()) {
                continue;
            }
            break;
        }
    }
}
