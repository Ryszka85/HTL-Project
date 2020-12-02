package com.ryszka.imageRestApi.security;

import java.util.List;

public enum AppPossibleLibraryResolutions {
    WIDTH_LIST(List.of(1920, 1280, 640)),
    HEIGHT_LIST(List.of(1080, 720, 480));

    private final List<Integer> resolutions;

    AppPossibleLibraryResolutions(List<Integer> resolutions) {
        this.resolutions = resolutions;
    }

    public List<Integer> getResolutions() {
        return resolutions;
    }
}
