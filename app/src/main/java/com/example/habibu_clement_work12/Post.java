package com.example.habibu_clement_work12;

public class Post {
    private String userName;
    private String userAvatar;
    private String postTime;
    private String postText;
    private String postImageUrl;
    private int likeCount;
    private int commentCount;
    private int shareCount;
    private boolean isLiked;

    public Post(String userName, String userAvatar, String postTime, String postText, 
                String postImageUrl, int likeCount, int commentCount, int shareCount, boolean isLiked) {
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.postTime = postTime;
        this.postText = postText;
        this.postImageUrl = postImageUrl;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.isLiked = isLiked;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getPostText() {
        return postText;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}









