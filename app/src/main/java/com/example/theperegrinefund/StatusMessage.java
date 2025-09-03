package com.example.theperegrinefund;

public class StatusMessage {
    private int idStatusMessage;
    private String status;

    public StatusMessage() {
    }

    public StatusMessage(int idStatusMessage, String status) {
        this.idStatusMessage = idStatusMessage;
        this.status = status;
    }

    public int getIdStatusMessage() {
        return idStatusMessage;
    }

    public void setIdStatusMessage(int idStatusMessage) {
        this.idStatusMessage = idStatusMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
