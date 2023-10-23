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
        String formattedString = "[%s]: %s%s";
        String end = emptyLine ? "\n\n" : "\n";
        
        if (type == PrintType.LOG) {
            System.out.printf(formattedString, type.get(), message, end);
        } else {
            System.err.printf(formattedString, type.get(), message, end);
        }
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
