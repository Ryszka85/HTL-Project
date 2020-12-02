package com.ryszka.imageRestApi.util;

import com.ryszka.imageRestApi.security.AppConfigProperties;

public class PathGenerator {
    public static String generateFileAccessLink(String path) {
        return path != null ?
                AppConfigProperties.FILE_PATH  + path :
                null;
    }

    public static String generateGalleryFileAccessLink(String path) {
        return path != null ?
                AppConfigProperties.FILE_PATH + "gallery/"  + path :
                null;
    }
}
