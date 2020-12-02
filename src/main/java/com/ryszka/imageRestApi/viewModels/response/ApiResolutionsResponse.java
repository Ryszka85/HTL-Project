package com.ryszka.imageRestApi.viewModels.response;

import java.util.List;

public class ApiResolutionsResponse {
    private List<Integer> widthList, heightList;

    public ApiResolutionsResponse() {
        this.widthList = List.of(1920, 1280, 640);
        this.heightList = List.of(1080, 720, 480);
    }

    public List<Integer> getWidthList() {
        return widthList;
    }

    public void setWidthList(List<Integer> widthList) {
        this.widthList = widthList;
    }

    public List<Integer> getHeightList() {
        return heightList;
    }

    public void setHeightList(List<Integer> heightList) {
        this.heightList = heightList;
    }
}
