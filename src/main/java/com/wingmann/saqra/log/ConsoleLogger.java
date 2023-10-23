package com.wingmann.saqra.log;

import java.io.PrintStream;

public class ConsoleLogger implements Logger {
    private enum PrintType {
        LOG,
        ERROR;

        public String get() {
            if (this == PrintType.LOG) {
                return "exec";
            }
            return "error";
        }
    }

    private void message(
            PrintStream stream,
            PrintType type,
            String message,
            boolean emptyLine) {
        stream.printf("[%s]: %s%s", type.get(), message, emptyLine ? "\n\n" : "\n");
    }

    @Override
    public void log(String message) {
        message(System.out, PrintType.LOG, message, false);
    }

    @Override
    public void logLine(String message) {
        message(System.out, PrintType.LOG, message, true);
    }

    @Override
    public void error(String message) {
        message(System.err, PrintType.ERROR, message, false);
    }

    @Override
    public void errorLine(String message) {
        message(System.err, PrintType.ERROR, message, true);
    }
}
