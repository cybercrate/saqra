package com.wingmann.saqra.log;

public interface Logger {
    void log(String message);
    void logln(String message);
    void error(String message);
    void errorln(String message);
    void input(String message);
}
