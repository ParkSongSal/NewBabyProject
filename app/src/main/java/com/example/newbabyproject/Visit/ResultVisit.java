package com.example.newbabyproject.Visit;

public class ResultVisit {

    public String result;

    public String userId;
    public String userName;
    public String userPhone;
    public String babyName;
    public String babyNum;
    public String babyRelation;

    public ResultVisit(String userId, String userName, String userPhone, String babyName, String babyNum, String babyRelation) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.babyName = babyName;
        this.babyNum = babyNum;
        this.babyRelation = babyRelation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getBabyNum() {
        return babyNum;
    }

    public void setBabyNum(String babyNum) {
        this.babyNum = babyNum;
    }

    public String getBabyRelation() {
        return babyRelation;
    }

    public void setBabyRelation(String babyRelation) {
        this.babyRelation = babyRelation;
    }
}
