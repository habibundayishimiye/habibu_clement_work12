package com.example.habibu_clement_work12;

public class Video {
    private String title;
    private String creator;
    private String creatorAvatar;
    private String thumbnailUrl;
    private String duration;
    private int viewCount;
    private String timeAgo;

    public Video(String title, String creator, String creatorAvatar, String thumbnailUrl, 
                 String duration, int viewCount, String timeAgo) {
        this.title = title;
        this.creator = creator;
        this.creatorAvatar = creatorAvatar;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.viewCount = viewCount;
        this.timeAgo = timeAgo;
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatorAvatar() {
        return creatorAvatar;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getDuration() {
        return duration;
    }

    public int getViewCount() {
        return viewCount;
    }

    public String getTimeAgo() {
        return timeAgo;
    }
}









