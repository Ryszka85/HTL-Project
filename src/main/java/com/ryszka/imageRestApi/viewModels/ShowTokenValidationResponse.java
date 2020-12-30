package com.ryszka.imageRestApi.viewModels;

public class ShowTokenValidationResponse {
    private boolean status;
    private boolean alreadyProcessed;

    public ShowTokenValidationResponse() {
    }

    public ShowTokenValidationResponse(boolean status) {
        this.status = status;
    }

    public ShowTokenValidationResponse(boolean status, boolean alreadyProcessed) {
        this.status = status;
        this.alreadyProcessed = alreadyProcessed;
    }

    public boolean isAlreadyProcessed() {
        return alreadyProcessed;
    }

    public void setAlreadyProcessed(boolean alreadyProcessed) {
        this.alreadyProcessed = alreadyProcessed;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
