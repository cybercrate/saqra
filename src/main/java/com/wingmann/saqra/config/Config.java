package com.wingmann.saqra.config;

public interface Config {
    String get();
    void set(String path);
    void load();
    void setup();
}
