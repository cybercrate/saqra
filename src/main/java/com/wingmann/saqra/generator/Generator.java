package com.wingmann.saqra.generator;


import java.util.Optional;

public interface Generator {
    Optional<ImageCache> build();
}
