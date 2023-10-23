package com.wingmann.saqra.io;

import java.io.File;

public interface FilesManager {
    File createFile(String path);
    boolean isInvalidPath(String path);
    boolean filesNotExists(String path);
    boolean createDirectories(String path);
}
