package com.example.theperegrinefund;

public class HistoryItem {
    private String title;
    private boolean isSelected;
    private int messageId;

    public HistoryItem(String title, boolean isSelected, int messageId) {
        this.title = title;
        this.isSelected = isSelected;
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}