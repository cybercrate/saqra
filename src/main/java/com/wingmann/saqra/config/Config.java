package com.wingmann.saqra.config;

public interface Config {
    void setConfig(String path);
    String loadConfig();
    void setup();
}
