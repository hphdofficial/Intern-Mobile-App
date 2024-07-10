package com.android.mobile.models;

public class ReviewModel {
    private String userName;
    private String reviewDate;
    private String reviewContent;
    private float rating;

    public ReviewModel(String userName, String reviewDate, String reviewContent, float rating) {
        this.userName = userName;
        this.reviewDate = reviewDate;
        this.reviewContent = reviewContent;
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public float getRating() {
        return rating;
    }
}
