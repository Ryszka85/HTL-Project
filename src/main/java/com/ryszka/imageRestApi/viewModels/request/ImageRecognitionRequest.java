package com.ryszka.imageRestApi.viewModels.request;

public class ImageRecognitionRequest {
    private byte[] img;

    public ImageRecognitionRequest() { }

    public ImageRecognitionRequest(byte[] img) {
        this.img = img;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
