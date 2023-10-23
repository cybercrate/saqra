package com.wingmann.saqra.io;

import java.util.Scanner;

public class ConsoleInputManager implements InputManager {
    private final Scanner scanner;
    private String data;

    public ConsoleInputManager() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public InputManager read(String message) {
        printMessage(message);
        data = scanner.nextLine();

        return this;
    }

    private void printMessage(String message) {
        System.out.printf("[%s]: ", message);
    }
}
