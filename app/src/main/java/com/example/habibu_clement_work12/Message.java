package com.example.habibu_clement_work12;

public class Message {
    private String senderName;
    private String messagePreview;
    private String time;
    private String avatarUrl;
    private boolean hasUnread;

    public Message(String senderName, String messagePreview, String time, String avatarUrl, boolean hasUnread) {
        this.senderName = senderName;
        this.messagePreview = messagePreview;
        this.time = time;
        this.avatarUrl = avatarUrl;
        this.hasUnread = hasUnread;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessagePreview() {
        return messagePreview;
    }

    public void setMessagePreview(String messagePreview) {
        this.messagePreview = messagePreview;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean hasUnread() {
        return hasUnread;
    }

    public void setHasUnread(boolean hasUnread) {
        this.hasUnread = hasUnread;
    }
}




