package com.psmStudio.newbabyproject.Notice;

public class ResultNotice {

    public String result;

    public int seq;
    private String title;
    private String content;
    private String insertId;
    private String insertDate;
    private String updateId;
    private String updateDate;


    public ResultNotice(int seq, String title, String content, String insertId, String insertDate, String updateId, String updateDate) {
        this.seq = seq;
        this.title = title;
        this.content = content;
        this.insertId = insertId;
        this.insertDate = insertDate;
        this.updateId = updateId;
        this.updateDate = updateDate;

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInsertId() {
        return insertId;
    }

    public void setInsertId(String insertId) {
        this.insertId = insertId;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }


}
