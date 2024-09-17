package com.example.myapplication;

public class Response {
    private int responseID;
    private int reviewID;
    private int userID;
    private String responseText;
    private String responseDate;

    public Response(int responseID, int reviewID, int userID, String responseText) {
        this.responseID = responseID;
        this.reviewID = reviewID;
        this.userID = userID;
        this.responseText = responseText;
    }

    public int getResponseID() {
        return responseID;
    }

    public void setResponseID(int responseID) {
        this.responseID = responseID;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }
}
