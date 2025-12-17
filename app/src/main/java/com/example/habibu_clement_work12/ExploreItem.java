package com.example.habibu_clement_work12;

public class ExploreItem {
    private String title;
    private String description;
    private String imageUrl;
    private String category;
    private int engagementCount;

    public ExploreItem(String title, String description, String imageUrl, String category, int engagementCount) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.engagementCount = engagementCount;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public int getEngagementCount() {
        return engagementCount;
    }
}




