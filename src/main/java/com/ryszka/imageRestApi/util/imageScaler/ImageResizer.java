package com.ryszka.imageRestApi.util.imageScaler;

import java.io.IOException;

public interface ImageResizer {
    byte[] resize() throws IOException;
}
