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

    private void message(PrintType type, String message, boolean emptyLine) {
        System.out.printf("[%s]: %s%s", type.get(), message, emptyLine ? "\n\n" : "\n");
    }

    @Override
    public void log(String message, boolean emptyLine) {
        message(PrintType.LOG, message, emptyLine);
    }

    @Override
    public void error(String message, boolean emptyLine) {
        message(PrintType.ERROR, message, emptyLine);
    }
}
