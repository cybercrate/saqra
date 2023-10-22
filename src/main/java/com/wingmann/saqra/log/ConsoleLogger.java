package com.wingmann.saqra.log;

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

    private void message(PrintType type, String message, boolean line) {
        System.out.printf("[%s]: %s%s", type.get(), message, line ? "\n\n" : "\n");
    }

    private void inputMessage(String message) {
        System.out.printf("[%s]: ", message);
    }

    @Override
    public void log(String message) {
        message(PrintType.LOG, message, false);
    }

    @Override
    public void logln(String message) {
        message(PrintType.LOG, message, true);
    }

    @Override
    public void error(String message) {
        message(PrintType.ERROR, message, false);
    }

    @Override
    public void errorln(String message) {
        message(PrintType.ERROR, message, true);
    }

    @Override
    public void input(String message) {
        inputMessage(message);
    }
}
