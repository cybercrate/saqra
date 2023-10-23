package com.wingmann.saqra.config;

public interface Config {
    String get();
    void set(String path);
    Config load();
    Config setup();
}
