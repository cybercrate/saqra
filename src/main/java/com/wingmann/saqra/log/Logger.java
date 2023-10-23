package com.wingmann.saqra.log;

public interface Logger {
    void log(String message);
    void logLine(String message);
    void error(String message);
    void errorLine(String message);
}
