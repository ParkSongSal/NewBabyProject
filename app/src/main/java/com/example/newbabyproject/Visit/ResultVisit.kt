package com.example.newbabyproject.Visit

import java.io.Serializable

class ResultVisit : Serializable {
    var result: String? = null
    var userId: String? = null
    var userName: String? = null
    var userPhone: String? = null
    var babyName: String? = null
    var babyNum: String? = null
    var babyBirthDate: String? = null
    var babyBirthTime: String? = null
    var babyRelation: String? = null
    var parentId: String? = null
    var parentName: String? = null
    var visitNotice: String? = null
    var babyWeight: String? = null
    var babyLactation: String? = null
    var babyRequireItem: String? = null
    var babyEtc: String? = null
    var boardConfirm: String? = null
    var writeDate: String = ""
    var tempYn : String? = ""
    var reserveDate : String? = ""
    var replyCnt: String? = null
    var path: String? = null
    var insertDate: String? = null

    constructor(
        userId: String?,
        userName: String?,
        userPhone: String?,
        babyName: String?,
        babyNum: String?,
        babyRelation: String?
    ) {
        this.userId = userId
        this.userName = userName
        this.userPhone = userPhone
        this.babyName = babyName
        this.babyNum = babyNum
        this.babyRelation = babyRelation
    }

    constructor(
        parentId: String?,
        parentName: String?,
        visitNotice: String?,
        babyWeight: String?,
        babyLactation: String?,
        babyRequireItem: String?,
        babyEtc: String?,
        writeDate: String,
        boardConfirm: String?,
        tempYn : String?,
        reserveDate : String?,
        path: String?,
        replyCnt: String?,
        insertDate: String?,
        babyName : String?
    ) {
        this.parentId = parentId
        this.parentName = parentName
        this.visitNotice = visitNotice
        this.babyWeight = babyWeight
        this.babyLactation = babyLactation
        this.babyRequireItem = babyRequireItem
        this.babyEtc = babyEtc
        this.writeDate = writeDate
        this.boardConfirm = boardConfirm
        this.tempYn = tempYn
        this.reserveDate = reserveDate
        //this.path = path;
        //this.replyCnt = replyCnt;
        this.insertDate = insertDate
        this.babyName = babyName
    }

    override fun toString(): String {
        return "ResultVisit{" +
                "result='" + result + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", babyName='" + babyName + '\'' +
                ", babyNum='" + babyNum + '\'' +
                ", babyRelation='" + babyRelation + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentName='" + parentName + '\'' +
                ", visitNotice='" + visitNotice + '\'' +
                ", babyWeight='" + babyWeight + '\'' +
                ", babyLactation='" + babyLactation + '\'' +
                ", babyRequireItem='" + babyRequireItem + '\'' +
                ", babyEtc='" + babyEtc + '\'' +
                ", boardConfirm='" + boardConfirm + '\'' +
                ", writeDate='" + writeDate + '\'' +
                ", replyCnt='" + replyCnt + '\'' +
                ", path='" + path + '\'' +
                ", insertDate='" + insertDate + '\'' +
                '}'
    }
}