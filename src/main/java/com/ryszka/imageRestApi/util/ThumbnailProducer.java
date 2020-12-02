package com.ryszka.imageRestApi.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ThumbnailProducer {
    public static void createThumbnail(InputStream inputStream) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Thumbnails.of(inputStream)
                .size(300, 300)
                .outputFormat("jpg")
                .toOutputStream(os);
    }
}
