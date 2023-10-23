package com.wingmann.saqra.log;

public interface Logger {
    void log(String message, boolean emptyLine);
    void error(String message, boolean emptyLine);
}
