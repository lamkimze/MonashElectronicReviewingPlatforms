package com.example.myapplication;

public class Position {
    private int positionID;
    private int UserID;
    private int businessID;
    private String positionName;


    public Position(int positionID, int UserID, int businessID, String positionName) {
        this.positionID = positionID;
        this.UserID = UserID;
        this.businessID = businessID;
        this.positionName = positionName;
    }


    public int getPositionID() {
        return positionID;
    }

    public void setPositionID(int positionID) {
        this.positionID = positionID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getBusinessID() {
        return businessID;
    }

    public void setBusinessID(int businessID) {
        this.businessID = businessID;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
