package com.ryszka.imageRestApi.viewModels.response;

import java.io.Serializable;
import java.util.List;

public class Alresponse implements Serializable {
    private List<String> response;

    public Alresponse() {
    }

    public Alresponse(List<String> response) {
        this.response = response;
    }

    public List<String> getResponse() {
        return response;
    }

    public void setResponse(List<String> response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Body{" +
                "response=" + response +
                '}';
    }
}
