package com.wingmann.saqra;

public class Logger {
    private enum PrintType {
        LOG,
        INPUT,
        ERROR;

        public String get() {
            switch (this) {
                case LOG -> {
                    return "exec";
                }
                case INPUT -> {
                    return "input";
                }
                case ERROR -> {
                    return "error";
                }
                default -> {
                    return "";
                }
            }
        }
    }

    private static void print(PrintType type, String message, boolean line) {
        String str;

        if (type == PrintType.INPUT) {
            str = "";
        } else {
            str = line ? "\n\n" : "\n";
        }
        System.out.printf("[%s]: %s%s", type.get(), message, str);
    }

    public static void log(String message, boolean newLine) {
        print(PrintType.LOG, message, newLine);
    }

    public static void input() {
        print(PrintType.INPUT, "", false);
    }

    public static void error(String message, boolean newLine) {
        print(PrintType.ERROR, message, newLine);
    }
}
