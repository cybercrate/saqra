package com.wingmann.saqra;

public class Logger {
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

    private static void message(PrintType type, String message, boolean line) {
        System.out.printf("[%s]: %s%s", type.get(), message, line ? "\n\n" : "\n");
    }

    private static void inputMessage(String message) {
        System.out.printf("[%s]: ", message);
    }

    public static void log(String message) {
        message(PrintType.LOG, message, false);
    }

    public static void logln(String message) {
        message(PrintType.LOG, message, true);
    }

    public static void error(String message) {
        message(PrintType.ERROR, message, false);
    }

    public static void errorln(String message) {
        message(PrintType.ERROR, message, true);
    }

    public static void input(String message) {
        inputMessage(message);
    }
}
