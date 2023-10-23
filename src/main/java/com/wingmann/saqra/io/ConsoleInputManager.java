package com.wingmann.saqra.io;

import com.wingmann.saqra.log.ConsoleLogger;
import com.wingmann.saqra.log.Logger;

import java.util.Scanner;

public class ConsoleInputManager implements InputManager {
    private final Logger logger;
    private final Scanner scanner;
    private String data;

    public ConsoleInputManager() {
        this.logger = new ConsoleLogger();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public InputManager read(String message) {
        logger.input(message);
        data = scanner.nextLine();

        return this;
    }
}
