package com.example.newbabyproject.Visit;

public class ResultVisit {

    public String result;

    public String userId;
    public String userName;
    public String userPhone;
    public String babyName;
    public String babyNum;
    public String babyRelation;

    private String parentId;
    private String parentName;
    private String visitNotice;
    private String babyWeight;
    private String babyLactation;
    private String babyRequireItem;
    private String babyEtc;
    private String boardConfirm;
    private String writeDate;
    private String replyCnt;
    private String path;
    private String insertDate;
    public ResultVisit(String userId, String userName, String userPhone, String babyName, String babyNum, String babyRelation) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.babyName = babyName;
        this.babyNum = babyNum;
        this.babyRelation = babyRelation;
    }

    public ResultVisit(String parentId, String parentName, String visitNotice, String babyWeight, String babyLactation, String babyRequireItem, String babyEtc,  String writeDate, String boardConfirm, String path, String replyCnt, String insertDate) {
        this.parentId = parentId;
        this.parentName = parentName;
        this.visitNotice = visitNotice;
        this.babyWeight = babyWeight;
        this.babyLactation = babyLactation;
        this.babyRequireItem = babyRequireItem;
        this.babyEtc = babyEtc;
        this.writeDate = writeDate;
        this.boardConfirm = boardConfirm;
        //this.path = path;
        //this.replyCnt = replyCnt;
        this.insertDate = insertDate;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getVisitNotice() {
        return visitNotice;
    }

    public void setVisitNotice(String visitNotice) {
        this.visitNotice = visitNotice;
    }


    public String getBabyWeight() {
        return babyWeight;
    }

    public void setBabyWeight(String babyWeight) {
        this.babyWeight = babyWeight;
    }

    public String getBabyLactation() {
        return babyLactation;
    }

    public void setBabyLactation(String babyLactation) {
        this.babyLactation = babyLactation;
    }

    public String getBabyRequireItem() {
        return babyRequireItem;
    }

    public void setBabyRequireItem(String babyRequireItem) {
        this.babyRequireItem = babyRequireItem;
    }

    public String getBabyEtc() {
        return babyEtc;
    }

    public void setBabyEtc(String babyEtc) {
        this.babyEtc = babyEtc;
    }

    public String getBoardConfirm() {
        return boardConfirm;
    }

    public void setBoardConfirm(String boardConfirm) {
        this.boardConfirm = boardConfirm;
    }

    public String getReplyCnt() {
        return replyCnt;
    }

    public void setReplyCnt(String replyCnt) {
        this.replyCnt = replyCnt;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }
}
